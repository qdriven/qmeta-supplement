package io.qmeta.testng;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.qmeta.testng.listener.TestResultLogger;
import io.qmeta.testng.testmodel.TestCase;
import io.qmeta.testng.testmodel.TestSuite;
import io.qmeta.testng.testng.TestStepLoggerListener;
import io.qmeta.testng.testng.TestngAdaptor;
import io.qmeta.testng.util.TemplateReportHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by patrick on 15/11/16.
 */
public class TestNGTestContextAware {
    private static final Logger logger = LogManager.getLogger(TestNGTestContextAware.class.getName());

    private Map<String, TestSuite> testSuiteMap = Maps.newConcurrentMap();

    public TestCase initTestCase(ITestResult result) {
        TestCase testCase = TestngAdaptor.covertToTestCase(new TestCase(), result);
        getCurrentTestSuite(result).addTestCase(testCase);
        initTestCaseLog(testCase);
        TestResultLogger.log("开始测试:" + testCase.getParameters());
        return testCase;
    }

    private void initTestCaseLog(TestCase testCase) {
        TestStepLoggerListener.getInstance().initTestCaseLogger(testCase);
    }

    public TestSuite initTestSuite(ITestContext context) {
        TestSuite ts = testSuiteMap.get(context.getSuite().getName());
        if (ts == null) {
            ts = new TestSuite(context.getStartDate(), context.getSuite().getName());
            testSuiteMap.put(context.getSuite().getName(), ts);
        }
        return ts;
    }

    private synchronized TestSuite getCurrentTestSuite(ITestResult result) {

        if (testSuiteMap.get(result.getTestContext().getSuite().getName()) != null) {
            return testSuiteMap.get(result.getTestContext().getSuite().getName());
        } else {
            TestSuite ts = new TestSuite(result.getTestContext().getStartDate(),
                    result.getTestContext().getSuite().getName());
            logger.info("创建新的TestSuite");
            testSuiteMap.put(result.getTestContext().getSuite().getName(), ts);
            return ts;
        }
    }

    private synchronized TestCase getCurrentTestCase(ITestResult result) {
        TestSuite suite = getCurrentTestSuite(result);
        logger.info("test result: {}", result);
        if (null != suite && result != null) {
            Set<TestCase> cases = suite.getTestCasesHolder().keySet();
            for (TestCase tc : cases) {
                if (
                        tc.getTestClassName().equalsIgnoreCase(result.getMethod().getRealClass().getSimpleName())
                                && tc.getTestMethodName().equalsIgnoreCase(result.getMethod().getMethodName())
                                && tc.getStartedMillis() == result.getStartMillis()) {
                    return tc;
                }
            }
        }
        TestCase currentCase = TestngAdaptor.covertToTestCase(new TestCase(), result);
        suite.addTestCase(currentCase);
        return currentCase;
    }

    public void addScreenShot(ITestResult result, String filePath) {
        getCurrentTestCase(result).addScreenShots(filePath);
    }

    public void completeTestCaseLog(ITestResult result) {
        TestCase currentCase = getCurrentTestCase(result);
        TestngAdaptor.updateResult(currentCase, result);
        currentCase.setIsCompleted(true);
        currentCase.setEndMills(System.currentTimeMillis());
    }

    public void completeTestSuite(ITestContext context) {
        testSuiteMap.get(context.getSuite().getName()).setEndDate(context.getEndDate());
        logger.info("test suite map size is {}", testSuiteMap.size());
    }

    public Map<String, TestSuite> getTestSuiteMap() {
        return testSuiteMap;
    }

    public void generateTestResult(VueData data) {
        TemplateReportHelper.generateTestNGVueReport(data);
    }

    public void generateTestResult() {
        VueData data = new VueData();
        TestSuites ts = new TestSuites();
        for (Map.Entry<String, TestSuite> entry : testSuiteMap.entrySet()) {
            ts.getTestsuites().add(entry.getValue().generateTemplateData());
        }
        data.setData(ts);
        TemplateReportHelper.generateTestNGVueReport(data);
    }

    public static class TestSuites {
        List<TestSuite> testsuites = Lists.newArrayList();

        public List<TestSuite> getTestsuites() {
            return testsuites;
        }

        public void setTestsuites(List<TestSuite> testsuites) {
            this.testsuites = testsuites;
        }
    }

    public static class VueData {
        private String el = "#testresult";
        private TestSuites data = new TestSuites();

        public String getEl() {
            return el;
        }

        public void setEl(String el) {
            this.el = el;
        }

        public TestSuites getData() {
            return data;
        }

        public void setData(TestSuites data) {
            this.data = data;
        }
    }

}
