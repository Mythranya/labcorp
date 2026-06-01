package listeners;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class NativeEmailableReportLinkListener implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        Path reportPath = Paths.get(outputDirectory, "emailable-report.html")
                .toAbsolutePath()
                .normalize();

        System.out.println("Native TestNG emailable report: " + reportPath.toUri());
    }
}
