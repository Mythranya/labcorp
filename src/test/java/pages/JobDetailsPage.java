package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Page object for the selected job details and apply flow pages.
 */
public class JobDetailsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private String parentWindowHandle;

    /**
     * Creates the job details page object for the current browser session.
     *
     * @param driver active WebDriver instance
     */
    public JobDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /**
     * Returns the job title shown on the page.
     *
     * @return visible job title text
     */
    public String getJobTitle() {
        String jobTitle = readFirstText(
                By.cssSelector("h1"),
                By.xpath("//h2[normalize-space()]"),
                By.xpath("//*[self::h1 or self::h2][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'developer')]")
        );

        if (!jobTitle.isBlank()) {
            return jobTitle;
        }

        String pageTitle = driver.getTitle();
        if (!pageTitle.isBlank()) {
            return pageTitle.trim();
        }

        return "";
    }

    /**
     * Returns the job location shown on the page.
     *
     * @return visible job location text
     */
    public String getJobLocation() {
        String bodyText = getBodyText();
        Matcher matcher = Pattern.compile("(?is)Location\\s*\\R?\\s*(.*?)\\s*Job ID\\s*:").matcher(bodyText);
        if (matcher.find()) {
            return matcher.group(1).replaceAll("\\s+", " ").trim();
        }

        List<String> lines = getBodyLines();
        int locationIndex = findLineIndexContaining(lines, "Location");
        if (locationIndex >= 0 && locationIndex + 1 < lines.size()) {
            String candidate = lines.get(locationIndex + 1).replaceAll("(?i)\\s*Job ID\\s*:.*$", "");
            return candidate.replaceAll("\\s+", " ").trim();
        }

        return "";
    }

    /**
     * Returns the job identifier shown on the page.
     *
     * @return visible job identifier text
     */
    public String getJobID() {
        String bodyText = getBodyText();
        Matcher matcher = Pattern.compile("(?i)job\\s*id\\s*:?\\s*([A-Z0-9-]+)").matcher(bodyText);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return "";
    }

    /**
     * Returns the job responsibilities section text.
     *
     * @return job responsibilities text
     */
    public String getJobResponsibilitiesText() {
        return extractSectionText("Job Responsibilities", "Minimum Qualifications");
    }

    /**
     * Returns the preferred qualifications section text.
     *
     * @return preferred qualifications text
     */
    public String getPreferredQualificationsText() {
        return extractSectionText("Preferred Qualifications", "Additional Job Standards");
    }

    /**
     * Returns the fifth bullet under Additional Job Standards.
     *
     * @return fifth job standards bullet text
     */
    public String getFifthAdditionalJobStandardsBullet() {
        List<String> bullets = extractSectionLines(
                "Additional Job Standards",
                Set.of(
                        "Labcorp is proud to be an Equal Opportunity Employer:",
                        "Labcorp is proud to be an Equal Opportunity Employer",
                        "We encourage all to apply",
                        "Join Our Talent Network."
                )
        );

        if (bullets.size() >= 5) {
            return bullets.get(4);
        }

        return "";
    }

    /**
     * Clicks the Apply Now control and waits for the apply flow to open.
     */
    public void clickApplyNow() {
        By applyNow = By.xpath("//a[@aria-label='Apply Now for COVAGLOBAL2616590EXTERNALENGLOBAL']");

        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(applyNow));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);
        parentWindowHandle = driver.getWindowHandle();
        int currentWindowCount = driver.getWindowHandles().size();
        button.click();

        wait.until(driverInstance -> driverInstance.getWindowHandles().size() >= currentWindowCount);
        if (driver.getWindowHandles().size() > currentWindowCount) {
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(parentWindowHandle)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
        }

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        wait.until(driverInstance -> "complete".equals(
                ((JavascriptExecutor) driverInstance).executeScript("return document.readyState").toString()
        ));
    }

    /**
     * Clicks the return navigation from the apply page back to the job search page.
     */
    public void clickReturnToJobSearch() {
        if (parentWindowHandle != null && driver.getWindowHandles().contains(parentWindowHandle)) {
            driver.switchTo().window(parentWindowHandle);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            return;
        }

        driver.navigate().back();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    /**
     * Returns the raw page source for advanced validation.
     *
     * @return page source
     */
    public String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * Returns the current body text.
     *
     * @return body text
     */
    private String getBodyText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body"))).getText();
    }

    /**
     * Returns the visible page text split into cleaned lines.
     *
     * @return non-empty body lines
     */
    private List<String> getBodyLines() {
        return Arrays.stream(getBodyText().split("\\R"))
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .toList();
    }

    /**
     * Reads the first visible text from the supplied locators.
     *
     * @param locators candidate locators
     * @return first non-empty visible text, or an empty string when none is found
     */
    private String readFirstText(By... locators) {
        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    String text = element.getText().trim();
                    if (!text.isEmpty()) {
                        return text;
                    }
                }
            }
        }
        return "";
    }

    /**
     * Extracts section text between two section headers.
     *
     * @param sectionHeader section title
     * @param nextHeader next section title
     * @return extracted section text
     */
    private String extractSectionText(String sectionHeader, String nextHeader) {
        List<String> lines = getBodyLines();
        int startIndex = findLineIndexContaining(lines, sectionHeader);
        if (startIndex < 0) {
            return "";
        }

        int endIndex = lines.size();
        for (int index = startIndex + 1; index < lines.size(); index++) {
            if (containsIgnoreCase(lines.get(index), nextHeader)) {
                endIndex = index;
                break;
            }
        }

        return String.join(" ", lines.subList(startIndex + 1, endIndex)).replaceAll("\\s+", " ").trim();
    }

    /**
     * Extracts section lines between a header and a stop set.
     *
     * @param sectionHeader section title
     * @param stopMarkers markers that end the section
     * @return extracted section lines
     */
    private List<String> extractSectionLines(String sectionHeader, Set<String> stopMarkers) {
        List<String> lines = getBodyLines();
        int startIndex = findLineIndexContaining(lines, sectionHeader);
        if (startIndex < 0) {
            return List.of();
        }

        List<String> sectionLines = new java.util.ArrayList<>();
        for (int index = startIndex + 1; index < lines.size(); index++) {
            String line = lines.get(index);
            if (stopMarkers.stream().anyMatch(marker -> containsIgnoreCase(line, marker))) {
                break;
            }
            sectionLines.add(line);
        }

        return sectionLines;
    }

    /**
     * Finds the first line that contains the requested text.
     *
     * @param lines source lines
     * @param match text to find
     * @return index of the matching line, or -1 when absent
     */
    private int findLineIndexContaining(List<String> lines, String match) {
        for (int index = 0; index < lines.size(); index++) {
            if (containsIgnoreCase(lines.get(index), match)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Checks whether one string contains another using case-insensitive comparison.
     *
     * @param value value to inspect
     * @param expected expected text
     * @return true when the text is present
     */
    private boolean containsIgnoreCase(String value, String expected) {
        return value != null
                && expected != null
                && value.toLowerCase(Locale.ROOT).contains(expected.toLowerCase(Locale.ROOT));
    }
}
