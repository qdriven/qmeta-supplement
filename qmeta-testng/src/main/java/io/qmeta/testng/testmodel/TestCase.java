package io.qmeta.testng.testmodel;


import org.apache.tools.ant.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by patrick on 15/3/16.
 *  获取testng 测试数据
 * @version $Id$
 */


public class TestCase {

    private String testCaseId;
    private String testClassName;
    private String testRealClassName;
    private String testMethodName;
    private String testDescription;
    private String priority;
    private int status;
    private Throwable errors ;
    private String errorMessage;
    private long startedMillis;
    private long endMillis;
    private boolean isSkipped;
    private String parameters ;
    private List<String> stepScreenshotPath = new ArrayList<>();
    private List<String> screenShots =new ArrayList<>();
    private List<String> logs =new ArrayList<>();
    private boolean isCompleted;

    public String getTestClassName() {
        return testClassName;
    }

    public void setTestClassName(String testClassName) {
        this.testClassName = testClassName;
    }

    public String getTestMethodName() {
        return testMethodName;
    }

    public void setTestMethodName(String testMethodName) {
        this.testMethodName = testMethodName;
    }

    public boolean isSkipped() {
        return isSkipped;
    }

    public void setSkipped(boolean isSkipped) {
        this.isSkipped = isSkipped;
    }

    public Long getEndMillis() {
        return endMillis;
    }

    public void setEndMillis(Long endMillis) {
        this.endMillis = endMillis;
    }

    public long getStartedMillis() {
        return startedMillis;
    }

    public void setStartedMillis(long startedMillis) {
        this.startedMillis = startedMillis;
    }


    public void setEndMills(long endMills) {
        this.endMillis = endMills;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Throwable getErrors() {
        return errors;
    }

    public void setErrors(Throwable errors) {
        this.errors = errors;
        generateErrorMessage();
    }

    public String getTestDescription() {
        return testDescription;
    }

    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }

    public List<String> getStepScreenshotPath() {
        return stepScreenshotPath;
    }

    public void setStepScreenshotPath(List<String> stepScreenshotPath) {
        this.stepScreenshotPath = stepScreenshotPath;
    }

    public List<String> getScreenShots() {
        return screenShots;
    }

    public void setScreenShots(List<String> screenShots) {
        this.screenShots = screenShots;
    }

    public void addScreenShots(String... path){
        Collections.addAll(this.screenShots,path);
    }


    private void generateErrorMessage(){
        if(errors==null) return ;
        StringBuilder sb= new StringBuilder();
        sb.append(errors.getMessage());
        sb.append("----------------------\n");
        sb.append("\n");
        sb.append("cause:");
        sb.append("----------------------\n");
        sb.append("\n");
        sb.append(errors.getCause());
        sb.append("----------------------\n");
        sb.append("\n");
        for (StackTraceElement stackTraceElement : errors.getStackTrace()) {
            sb.append(stackTraceElement);
            sb.append("\n");
        }

        this.errorMessage=sb.toString();
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {

        if(parameters==null) this.parameters= "";
        StringBuilder sb = new StringBuilder();
        for (Object o : parameters) {
            sb.append(o.toString());
            sb.append(";");
        }
        this.parameters = sb.toString();
    }

    public String getTestRealClassName() {
        return testRealClassName;
    }

    public void setTestRealClassName(String testRealClassName) {
        this.testRealClassName = testRealClassName;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "TestCase{" +
                "testCaseId='" + testCaseId + '\'' +
                ", testClassName='" + testClassName + '\'' +
                ", testRealClassName='" + testRealClassName + '\'' +
                ", testMethodName='" + testMethodName + '\'' +
                ", testDescription='" + testDescription + '\'' +
                ", priority='" + priority + '\'' +
                ", status=" + status +
                ", errors=" + errors +
                ", errorMessage='" + errorMessage + '\'' +
                ", startedMillis=" + startedMillis +
                ", endMillis=" + endMillis +
                ", isSkipped=" + isSkipped +
                ", parameters='" + parameters + '\'' +
                ", stepScreenshotPath=" + stepScreenshotPath +
                ", screenShots=" + screenShots +
                ", logs=" + logs +
                '}';
    }

    public void setIsSkipped(boolean isSkipped) {
        this.isSkipped = isSkipped;
    }


    public boolean isCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean isPass(){
        return status==2?false:true;
    }
}
