package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page object for Labcorp career search and search result handling.
 */
public class CareerPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    /**
     * Creates the career page object for the current browser session.
     *
     * @param driver active WebDriver instance
     */
    public CareerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /**
     * Searches for a job title using the page search control.
     *
     * @param jobTitle job title to search for
     */
    public void searchForJobTitle(String jobTitle) {
        WebElement searchInput = locateSearchInput();
        searchInput.clear();
        searchInput.sendKeys(jobTitle);
        searchInput.sendKeys(Keys.ENTER);
        wait.until(driverInstance -> !driverInstance.getCurrentUrl().isBlank());
    }

    /**
     * Opens the first matching job result from the search results.
     *
     * @param jobTitle job title used to identify the result
     */
    public void openSelectedJobFromSearchResults(String jobTitle) {
        By resultLink = By.xpath(
                "//a[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '"
                        + jobTitle.toLowerCase()
                        + "')]");

        List<WebElement> candidates = driver.findElements(resultLink);
        if (!candidates.isEmpty()) {
            WebElement firstMatch = wait.until(ExpectedConditions.elementToBeClickable(candidates.get(0)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", firstMatch);
            firstMatch.click();
            return;
        }

        WebElement fallback = wait.until(ExpectedConditions.elementToBeClickable(resultLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", fallback);
        fallback.click();
    }

    /**
     * Locates the search input using a set of resilient selectors.
     *
     * @return visible search input element
     */
    private WebElement locateSearchInput() {
        WebElement input = locateSearchInputOrNull();
        if (input != null) {
            return wait.until(ExpectedConditions.visibilityOf(input));
        }

        throw new NoSuchElementException("Unable to locate the career search input.");
    }

    /**
     * Returns true when the career search page input is visible.
     *
     * @return true if the search page is displayed
     */
    public boolean isJobSearchPageDisplayed() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(45)).until(driverInstance -> {
                WebElement input = locateSearchInputOrNull();
                if (input != null) {
                    return true;
                }

                String source = driverInstance.getPageSource().toLowerCase();
                String body = driverInstance.findElement(By.tagName("body")).getText().toLowerCase();
                return source.contains("search job title or location")
                        || body.contains("search job title or location")
                        || source.contains("featured careers")
                        || body.contains("featured careers")
                        || source.contains("saved jobs")
                        || body.contains("saved jobs");
            });
        } catch (TimeoutException ex) {
            return false;
        }
    }

    /**
     * Locates the search input without throwing if it is missing.
     *
     * @return search input element or null when not found
     */
    private WebElement locateSearchInputOrNull() {
        List<By> candidates = List.of(
                By.cssSelector("input[type='search']"),
                By.cssSelector("input[placeholder*='Search']"),
                By.cssSelector("input[name*='search']"),
                By.cssSelector("input[id*='search']")
        );

        for (By candidate : candidates) {
            List<WebElement> elements = driver.findElements(candidate);
            if (!elements.isEmpty()) {
                WebElement element = elements.get(0);
                if (element.isDisplayed()) {
                    return wait.until(ExpectedConditions.visibilityOf(element));
                }
            }
        }

        return null;
    }
}
