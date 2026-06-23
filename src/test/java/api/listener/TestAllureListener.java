package api.listener;

import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestAllureListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestAllureListener.class);

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        log.info("Suite STARTED: {}", iTestContext.getName());
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        log.info("Suite FINISHED: {} — passed: {}, failed: {}, skipped: {}",
                iTestContext.getName(),
                iTestContext.getPassedTests().size(),
                iTestContext.getFailedTests().size(),
                iTestContext.getSkippedTests().size());
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        log.info("Test STARTED: {}", getTestMethodName(iTestResult));
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        log.info("Test PASSED: {}", getTestMethodName(iTestResult));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        log.error("Test FAILED: {} — reason: {}",
                getTestMethodName(iTestResult),
                iTestResult.getThrowable() != null ? iTestResult.getThrowable().getMessage() : "unknown");
        saveTextLog(getTestMethodName(iTestResult) + " failed: "
                + (iTestResult.getThrowable() != null ? iTestResult.getThrowable().getMessage() : "unknown"));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        log.warn("Test SKIPPED: {}", getTestMethodName(iTestResult));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        log.warn("Test FAILED within success ratio: {}", getTestMethodName(iTestResult));
    }
}
