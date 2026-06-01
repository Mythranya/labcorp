package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.DriverFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Cucumber hooks for logging and screenshot capture without third-party reporting.
 */
public class Hooks {

    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");
    private static Scenario currentScenario;

    /**
     * Prepares the current scenario context.
     *
     * @param scenario current scenario
     */
    @Before
    public void beforeScenario(Scenario scenario) {
        currentScenario = scenario;
    }

    /**
     * Clears the current scenario context.
     *
     * @param scenario current scenario
     */
    @After
    public void afterScenario(Scenario scenario) {
        currentScenario = null;
    }

    /**
     * Logs a step to the console and stores a screenshot.
     *
     * @param message step message
     */
    public static void logStep(String message) {
        System.out.println("[STEP] " + message);
        attachScreenshot(message);
    }

    /**
     * Logs a passing assertion to the console and stores a screenshot.
     *
     * @param message assertion message
     */
    public static void logAssertionPass(String message) {
        System.out.println("[ASSERT PASS] " + message);
        attachScreenshot(message);
    }

    /**
     * Logs a failing assertion to the console and stores a screenshot.
     *
     * @param message assertion message
     */
    public static void logAssertionFail(String message) {
        System.out.println("[ASSERT FAIL] " + message);
        attachScreenshot(message);
    }

    /**
     * Captures and stores a screenshot for the current run.
     *
     * @param label screenshot label
     */
    private static void attachScreenshot(String label) {
        try {
            byte[] screenshot = DriverFactory.takeScreenshot();
            if (screenshot.length == 0) {
                return;
            }

            Files.createDirectories(SCREENSHOT_DIR);
            Path screenshotPath = SCREENSHOT_DIR.resolve(safeFileName(label) + "_" + timestamp() + ".png");
            Files.write(screenshotPath, screenshot);

            if (currentScenario != null) {
                currentScenario.attach(screenshot, "image/png", label);
            }

            System.out.println("[SCREENSHOT] file:///" + screenshotPath.toAbsolutePath().toString().replace("\\", "/"));
        } catch (Exception ignored) {
            // Screenshot capture is best effort.
        }
    }

    /**
     * Produces a filesystem-safe name.
     *
     * @param input raw label
     * @return safe label
     */
    private static String safeFileName(String input) {
        return input == null ? "screenshot" : input.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }

    /**
     * Returns a timestamp usable in filenames.
     *
     * @return timestamp string
     */
    private static String timestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
    }
}
