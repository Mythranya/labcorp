package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page object for the Labcorp home page.
 */
public class HomePage {

    private static final String LABCORP_URL = "https://www.labcorp.com/";

    private final WebDriver driver;
    private final WebDriverWait wait;

    /**
     * Creates the home page object for the current browser session.
     *
     * @param driver active WebDriver instance
     */
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /**
     * Opens the Labcorp website.
     */
    public void openLabcorpWebsite() {
        driver.get(LABCORP_URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        acceptCookiesIfPresent();
    }

    /**
     * Clicks the Careers link from the home page.
     */
    public void clickCareersLink() {
        acceptCookiesIfPresent();

        By careersLink = By.xpath(
                "//div[@id='text-a63751913f']");
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(careersLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", link);
        link.click();
    }

    /**
     * Accepts the cookie banner if the consent button is shown.
     */
    private void acceptCookiesIfPresent() {
        By acceptCookiesButton = By.id("onetrust-accept-btn-handler");

        try {
            WebElement button = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(acceptCookiesButton));
            try {
                button.click();
            } catch (ElementNotInteractableException ex) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            }
        } catch (TimeoutException ignored) {
            // The banner is not always present, so continue when it does not appear.
        }
    }
}
