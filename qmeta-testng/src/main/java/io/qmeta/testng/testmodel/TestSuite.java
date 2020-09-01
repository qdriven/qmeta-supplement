package io.qmeta.testng.testmodel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by patrick on 15/3/16.
 * test suite数据
 *
 * @version $Id$
 */

public class TestSuite {
    private String suiteName;
    private String description;
    private int cases;
    //    private List<TestCase> testCasesHolder = new ArrayList<>();
    private transient Map<TestCase, String> testCasesHolder = Maps.newConcurrentMap();
    private List<TestCase> testCases= Lists.newArrayList();
    private transient  List<TestCase> passedTestCases = new ArrayList<>();
    private transient  List<TestCase> failedTestCases = new ArrayList<>();
    private int passed;
    private int failed;
    private Date startedDate;
    private Date endDate;
    private boolean isPassedSuite;

    public TestSuite(String suiteName) {
        this.suiteName = suiteName;
    }

    public TestSuite(Date startedDate, String suiteName) {
        this.startedDate = new Date(startedDate.getTime());
        this.suiteName = suiteName;
    }

    public TestSuite() {
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Date getStartedDate() {
        return new Date(startedDate.getTime());
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = new Date(startedDate.getTime());
    }

    public Date getEndDate() {
        return new Date(endDate.getTime());
    }

    public void setEndDate(Date endDate) {
        this.endDate = new Date(endDate.getTime());
    }

    public void addTestCase(TestCase... testCases) {
        for (TestCase testCase : testCases) {
            if(testCase.getTestDescription()==null) testCase.setTestDescription("");
            this.testCasesHolder.put(testCase, testCase.getTestDescription());
        }
    }

    public TestSuite generateTemplateData(){
        isTestSuiteFailed();
        this.passed = passedTestCases.size();
        this.failed= failedTestCases.size();
        this.cases = testCasesHolder.size();
        this.isPassedSuite = failedTestCases.isEmpty();
        for (Map.Entry<TestCase, String> entry : testCasesHolder.entrySet()) {
            this.testCases.add(entry.getKey());
        }
        return this;
    }
    /**
     * 判断测试套件是否失败，考虑了重试case的情况
     *
     * @return
     */
    public boolean isTestSuiteFailed() {
        Map<String, TestCase> passedResult = new HashMap<>();
        Map<String, TestCase> failedResult = new HashMap<>();
        for (TestCase testCase : testCasesHolder.keySet()) {
            String uniqueTestedMethod = testCase.getTestClassName() + testCase.getTestMethodName() + testCase.getParameters();
            Integer status = testCase.getStatus();
            //already have failed result
            if (failedResult.get(uniqueTestedMethod) != null) {
                if (status == 1) { //remove the failed case and add passed case,and retry passed
                    failedResult.remove(uniqueTestedMethod);
                    passedResult.put(uniqueTestedMethod, testCase);
                }// else do nothing,keep it there
            } else {
                if (passedResult.get(uniqueTestedMethod) == null) {
                    if (status == 1) {
                        passedResult.put(uniqueTestedMethod, testCase);
                    } else if (status == 2) failedResult.put(uniqueTestedMethod, testCase);
                }
            }
        }
        passedTestCases.addAll(passedResult.values().stream().collect(Collectors.toList()));
        failedTestCases.addAll(failedResult.values().stream().collect(Collectors.toList()));
        if (failedTestCases.isEmpty()) isPassedSuite = true;
        else isPassedSuite = false;
        return !isPassedSuite;

    }

    public List<TestCase> getPassedTestCases() {
        return passedTestCases;
    }


    public List<TestCase> getFailedTestCases() {
        return failedTestCases;
    }


    public boolean getIsPassedSuite() {
        return isPassedSuite;
    }


    public boolean isPassedSuite() {
        return isPassedSuite;
    }

    public void setIsPassedSuite(boolean isPassedSuite) {
        this.isPassedSuite = isPassedSuite;
    }

    @Override
    public String toString() {
        return "TestSuite{" +
                "suiteName='" + suiteName + '\'' +
                ", description='" + description + '\'' +
                ", testCasesHolder=" + testCasesHolder +
                ", passedTestCases=" + passedTestCases +
                ", failedTestCases=" + failedTestCases +
                ", startedDate=" + startedDate +
                ", endDate=" + endDate +
                ", isPassedSuite=" + isPassedSuite +
                '}';
    }

    public Map<TestCase, String> getTestCasesHolder() {
        return testCasesHolder;
    }

    public int getPassed() {
        return passed;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }
}
