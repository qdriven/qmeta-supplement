package io.qmeta.testng.listener;


import io.qmeta.testng.TestNGTestContextAware;
import io.qmeta.testng.util.TemplateReportHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.util.List;

/**
 * Created by patrick on 15/3/16.
 *
 * @version $Id$
 */

@SuppressWarnings("unchecked")
public class SimpleWebDriverScreenShotTestListener extends AbstractWebDriverEventListener
        implements ITestListener, IReporter{

    private TestNGTestContextAware testContextAware = new TestNGTestContextAware();
    private ThreadLocal<ITestContext> testngContext = new ThreadLocal<>();
    private ThreadLocal<ITestResult> currentResult = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        currentResult.set(result);
        testContextAware.initTestCase(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testContextAware.completeTestCaseLog(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {

        TestResultLogger.error(result.getThrowable());
        testContextAware.completeTestCaseLog(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testContextAware.completeTestCaseLog(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        testContextAware.completeTestCaseLog(result);
    }

    @Override
    public void onStart(ITestContext context) {
        testngContext.set(context);
        testContextAware.initTestSuite(context);

    }

    @Override
    public void onFinish(ITestContext context) {
        testContextAware.completeTestSuite(context);
    }

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

         testContextAware.generateTestResult();
         TemplateReportHelper.openReport();
    }


    @Override
    public void onException(Throwable throwable, WebDriver driver) {

        String errorStringShotPath = TemplateReportHelper.ScreenShotUtils.takeScreenshotForSimpleReport(driver);
        TestResultLogger.warn(throwable);
        testContextAware.addScreenShot(currentResult.get(), errorStringShotPath);
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        String screenshots= TemplateReportHelper.ScreenShotUtils.takeScreenshotForSimpleReport(driver);
        testContextAware.addScreenShot(currentResult.get(),screenshots);
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {

    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {

    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
    }
}
