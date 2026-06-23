package api.listener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(ExtentReportListener.class);

    private static final String OUTPUT_FOLDER = "./reports/";
    private static final String FILE_NAME = "TestExecutionReport.html";

    private static ExtentReports extent = init();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ExtentReports extentReports;

    private static ExtentReports init() {
        Path path = Paths.get(OUTPUT_FOLDER);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        extentReports = new ExtentReports();
        ExtentSparkReporter reporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);
        reporter.config().setReportName("Open Cart Automation Test Results");
        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("System", "MAC");
        extentReports.setSystemInfo("Author", "Naveen AutomationLabs");
        extentReports.setSystemInfo("Build#", "1.1");
        extentReports.setSystemInfo("Team", "OpenCart QA Team");
        extentReports.setSystemInfo("ENV NAME", System.getProperty("env"));
        return extentReports;
    }

    @Override
    public synchronized void onStart(ITestContext context) {
        log.info("Suite STARTED: {}", context.getName());
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        log.info("Suite FINISHED: {} — passed: {}, failed: {}, skipped: {}",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
        extent.flush();
        test.remove();
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        String qualifiedName = result.getMethod().getQualifiedName();
        int last = qualifiedName.lastIndexOf(".");
        int mid = qualifiedName.substring(0, last).lastIndexOf(".");
        String className = qualifiedName.substring(mid + 1, last);

        log.info("Test STARTED: {}", result.getMethod().getMethodName());
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(),
                result.getMethod().getDescription());
        extentTest.assignCategory(result.getTestContext().getSuite().getName());
        extentTest.assignCategory(className);
        test.set(extentTest);
        test.get().getModel().setStartTime(getTime(result.getStartMillis()));
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        log.info("Test PASSED: {}", result.getMethod().getMethodName());
        test.get().pass("Test passed");
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        log.error("Test FAILED: {} — reason: {}",
                result.getMethod().getMethodName(),
                result.getThrowable() != null ? result.getThrowable().getMessage() : "unknown");
        test.get().fail("Test failed: "
                + (result.getThrowable() != null ? result.getThrowable().getMessage() : "unknown"));
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        log.warn("Test SKIPPED: {}", result.getMethod().getMethodName());
        test.get().skip("Test skipped");
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("Test FAILED within success ratio: {}", result.getMethod().getMethodName());
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}