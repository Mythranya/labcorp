package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * Creates and manages the shared WebDriver instance for the test run.
 */
public final class DriverFactory {

    private static WebDriver driver;

    /**
     * Prevents external instantiation.
     */
    private DriverFactory() {
        // Utility class.
    }

    /**
     * Initializes ChromeDriver once per test session.
     */
    public static synchronized void initDriver() {
        if (driver != null) {
            return;
        }

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
    }

    /**
     * Returns the active WebDriver instance, creating it if needed.
     *
     * @return active WebDriver instance
     */
    public static synchronized WebDriver getDriver() {
        if (driver == null) {
            initDriver();
        }
        return driver;
    }

    /**
     * Closes the browser and releases the driver reference.
     */
    public static synchronized void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    /**
     * Takes a screenshot from the current browser session.
     *
     * @return screenshot bytes
     */
    public static synchronized byte[] takeScreenshot() {
        if (driver == null) {
            return new byte[0];
        }
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
