package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Cucumber glue class that maps Gherkin steps to the reusable career workflow.
 */
public class CareerSteps {

    private final steps.CareerWorkflow steps = new steps.CareerWorkflow();

    /**
     * Starts the browser and creates page objects before each scenario.
     */
    @Before
    public void setUp() {
        steps.setUp();
    }

    /**
     * Closes the browser after each scenario.
     */
    @After
    public void tearDown() {
        steps.tearDown();
    }

    /**
     * Opens the Labcorp website.
     */
    @Given("User launches Labcorp website")
    public void userLaunchesLabcorpWebsite() {
        steps.userLaunchesLabcorpWebsite();
    }

    /**
     * Clicks the Careers link.
     */
    @When("User clicks Careers")
    public void userClicksCareers() {
        steps.userClicksCareers();
    }

    /**
     * Searches for the required job title.
     *
     * @param jobTitle job title from the feature file
     */
    @And("User searches for {string}")
    public void userSearchesForJobTitle(String jobTitle) {
        steps.userSearchesForJobTitle(jobTitle);
    }

    /**
     * Opens the job posting from search results.
     */
    @And("User opens the job posting")
    public void userOpensTheJobPosting() {
        steps.userOpensTheJobPosting();
    }

    /**
     * Verifies that the job title is visible.
     */
    @Then("Verify Job Title")
    public void verifyJobTitle() {
        steps.verifyJobTitle();
    }

    /**
     * Verifies that the job location is visible.
     */
    @And("Verify Job Location")
    public void verifyJobLocation() {
        steps.verifyJobLocation();
    }

    /**
     * Verifies that the job identifier is visible.
     */
    @And("Verify Job ID")
    public void verifyJobId() {
        steps.verifyJobId();
    }

    /**
     * Verifies that the job responsibilities section contains the required keyword.
     */
    @And("Verify Job Responsibilities contains {string}")
    public void verifyJobResponsibilitiesContains(String keyword) {
        steps.verifyJobResponsibilitiesContains(keyword);
    }

    /**
     * Verifies that the preferred qualifications section contains the required text.
     */
    @And("Verify Preferred Qualifications contains {string}")
    public void verifyPreferredQualificationsContains(String expectedText) {
        steps.verifyPreferredQualificationsContains(expectedText);
    }

    /**
     * Verifies that the fifth bullet under Additional Job Standards matches the expected text.
     */
    @And("Verify 5th Additional Job Standards bullet equals {string}")
    public void verifyFifthAdditionalJobStandardsBulletEquals(String expectedText) {
        steps.verifyFifthAdditionalJobStandardsBulletEquals(expectedText);
    }

    /**
     * Clicks the Apply Now button and captures the details page values for comparison.
     */
    @When("User clicks Apply Now")
    public void userClicksApplyNow() {
        steps.userClicksApplyNow();
    }

    /**
     * Verifies that the Apply page shows the same job title.
     */
    @Then("Verify Apply page shows same Job Title")
    public void verifyApplyPageShowsSameJobTitle() {
        steps.verifyApplyPageShowsSameJobTitle();
    }

    /**
     * Verifies that the Apply page shows the same job ID.
     */
    @And("Verify Apply page shows same Job ID")
    public void verifyApplyPageShowsSameJobId() {
        steps.verifyApplyPageShowsSameJobId();
    }

    /**
     * Clicks Return to Job Search after validating the Apply page.
     */
    @When("User clicks Return to Job Search")
    public void userClicksReturnToJobSearch() {
        steps.userClicksReturnToJobSearch();
    }

    /**
     * Verifies that the user is back on the Job Search page.
     */
    @Then("Verify user is navigated back to Job Search page")
    public void verifyUserIsNavigatedBackToJobSearchPage() {
        steps.verifyUserIsNavigatedBackToJobSearchPage();
    }
}
