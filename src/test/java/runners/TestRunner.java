package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import java.io.File;

@CucumberOptions(
        features = "classpath:features",
        glue = {"stepdefinitions", "hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

        static {
                // Ignore any stale external Cucumber feature override and use the
                // feature files packaged under src/test/resources instead.
                System.clearProperty("cucumber.features");
        }

        @AfterSuite
        public void printReportPath() {
                String reportPath = System.getProperty("user.dir") + "/target/cucumber-reports/cucumber.html";
                File reportFile = new File(reportPath);

                System.out.println("\n========================================================================");
                System.out.println("EXECUTION COMPLETE! CLICK LINK TO VIEW NATIVE REPORT:");
                System.out.println("file:///" + reportFile.getAbsolutePath().replace("\\", "/"));
                System.out.println("========================================================================\n");
        }
}
