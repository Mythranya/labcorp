package steps;

import hooks.Hooks;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.CareerPage;
import pages.HomePage;
import pages.JobDetailsPage;
import utils.DriverFactory;

/**
 * Encapsulates the reusable career workflow and assertions.
 */
public class CareerWorkflow {

    private WebDriver driver;
    private HomePage homePage;
    private CareerPage careerPage;
    private JobDetailsPage jobDetailsPage;
    private String searchedJobTitle;
    private String jobTitleOnDetailsPage;
    private String jobIdOnDetailsPage;

    /**
     * Starts the browser and creates page objects before each scenario.
     */
    public void setUp() {
        DriverFactory.initDriver();
        driver = DriverFactory.getDriver();
        homePage = new HomePage(driver);
        careerPage = new CareerPage(driver);
        jobDetailsPage = new JobDetailsPage(driver);
    }

    /**
     * Closes the browser after each scenario.
     */
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    /**
     * Opens the Labcorp website.
     */
    public void userLaunchesLabcorpWebsite() {
        homePage.openLabcorpWebsite();
        Hooks.logStep("User launches Labcorp website");
    }

    /**
     * Clicks the Careers link.
     */
    public void userClicksCareers() {
        homePage.clickCareersLink();
        Hooks.logStep("User clicks Careers");
    }

    /**
     * Searches for the required job title.
     *
     * @param jobTitle job title from the feature file
     */
    public void userSearchesForJobTitle(String jobTitle) {
        searchedJobTitle = jobTitle;
        careerPage.searchForJobTitle(jobTitle);
        Hooks.logStep("User searches for: " + jobTitle);
    }

    /**
     * Opens the job posting from search results.
     */
    public void userOpensTheJobPosting() {
        careerPage.openSelectedJobFromSearchResults(searchedJobTitle);
        Hooks.logStep("User opens the job posting");
    }

    /**
     * Verifies that the job title is visible.
     */
    public void verifyJobTitle() {
        jobTitleOnDetailsPage = jobDetailsPage.getJobTitle();
        assertNotBlank(jobTitleOnDetailsPage, "Job Title");
    }

    /**
     * Verifies that the job location is visible.
     */
    public void verifyJobLocation() {
        String jobLocation = jobDetailsPage.getJobLocation();
        assertNotBlank(jobLocation, "Job Location");
    }

    /**
     * Verifies that the job identifier is visible.
     */
    public void verifyJobId() {
        jobIdOnDetailsPage = jobDetailsPage.getJobID();
        assertNotBlank(jobIdOnDetailsPage, "Job ID");
    }

    /**
     * Verifies that the job responsibilities section contains the required keyword.
     */
    public void verifyJobResponsibilitiesContains(String keyword) {
        String responsibilities = jobDetailsPage.getJobResponsibilitiesText();
        assertContains(responsibilities, keyword, "Job Responsibilities contains keyword");
    }

    /**
     * Verifies that the preferred qualifications section contains the required text.
     */
    public void verifyPreferredQualificationsContains(String expectedText) {
        String preferredQualifications = jobDetailsPage.getPreferredQualificationsText();
        assertContains(preferredQualifications, expectedText, "Preferred Qualifications contains text");
    }

    /**
     * Verifies that the fifth bullet under Additional Job Standards matches the expected text.
     */
    public void verifyFifthAdditionalJobStandardsBulletEquals(String expectedText) {
        String actualBullet = jobDetailsPage.getFifthAdditionalJobStandardsBullet();
        assertEquals(actualBullet, expectedText, "5th Additional Job Standards bullet");
    }

    /**
     * Clicks the Apply Now button and captures the details page values for comparison.
     */
    public void userClicksApplyNow() {
        jobTitleOnDetailsPage = jobDetailsPage.getJobTitle();
        jobIdOnDetailsPage = jobDetailsPage.getJobID();
        jobDetailsPage.clickApplyNow();
        Hooks.logStep("User clicks Apply Now");
    }

    /**
     * Verifies that the Apply page shows the same job title.
     */
    public void verifyApplyPageShowsSameJobTitle() {
        String applyPageContent = jobDetailsPage.getPageSource();
        assertContains(applyPageContent, jobTitleOnDetailsPage, "Apply page Job Title");
    }

    /**
     * Verifies that the Apply page shows the same job ID.
     */
    public void verifyApplyPageShowsSameJobId() {
        String applyPageContent = jobDetailsPage.getPageSource();
        assertContains(applyPageContent, jobIdOnDetailsPage, "Apply page Job ID");
    }

    /**
     * Clicks Return to Job Search after validating the Apply page.
     */
    public void userClicksReturnToJobSearch() {
        jobDetailsPage.clickReturnToJobSearch();
        Hooks.logStep("User clicks Return to Job Search");
    }

    /**
     * Verifies that the user is back on the Job Search page.
     */
    public void verifyUserIsNavigatedBackToJobSearchPage() {
        assertTrue(careerPage.isJobSearchPageDisplayed(), "User is navigated back to Job Search page.");
    }

    /**
     * Asserts that a value is not blank and prints the validation result to the console.
     *
     * @param value value to validate
     * @param label field name being asserted
     */
    private void assertNotBlank(String value, String label) {
        if (value != null && !value.isBlank()) {
            System.out.println("[ASSERT PASS] " + label + " = " + value);
            Hooks.logAssertionPass(label + " = " + value);
            return;
        }

        String message = label + " should not be blank.";
        System.out.println("[ASSERT FAIL] " + message);
        Hooks.logAssertionFail(message);
        Assert.fail(message);
    }

    /**
     * Asserts that the actual text contains the expected text.
     *
     * @param actual actual text
     * @param expected expected text
     * @param label assertion label
     */
    private void assertContains(String actual, String expected, String label) {
        if (actual != null && actual.contains(expected)) {
            System.out.println("[ASSERT PASS] " + label + " contains: " + expected);
            Hooks.logAssertionPass(label + " contains: " + expected);
            return;
        }

        String message = label + " expected to contain [" + expected + "] but found [" + actual + "]";
        System.out.println("[ASSERT FAIL] " + message);
        Hooks.logAssertionFail(message);
        Assert.fail(message);
    }

    /**
     * Asserts that the actual text matches the expected text exactly.
     *
     * @param actual actual text
     * @param expected expected text
     * @param label assertion label
     */
    private void assertEquals(String actual, String expected, String label) {
        if (expected != null && expected.equals(actual)) {
            System.out.println("[ASSERT PASS] " + label + " = " + actual);
            Hooks.logAssertionPass(label + " = " + actual);
            return;
        }

        String message = label + " expected [" + expected + "] but found [" + actual + "]";
        System.out.println("[ASSERT FAIL] " + message);
        Hooks.logAssertionFail(message);
        Assert.fail(message);
    }

    /**
     * Asserts that a condition is true and prints the validation result to the console.
     *
     * @param condition condition to validate
     * @param message assertion message
     */
    private void assertTrue(boolean condition, String message) {
        if (condition) {
            System.out.println("[ASSERT PASS] " + message);
            Hooks.logAssertionPass(message);
            return;
        }

        String failureMessage = message + " failed.";
        System.out.println("[ASSERT FAIL] " + failureMessage);
        Hooks.logAssertionFail(failureMessage);
        Assert.fail(failureMessage);
    }
}
