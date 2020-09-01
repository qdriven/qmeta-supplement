package io.qmeta.testng.testng;

import cn.hutool.core.util.StrUtil;
import io.qmeta.testng.testmodel.*;
import org.testng.ITestResult;

/**
 * Created by patrick on 15/3/16.
 *
 * @version $Id$
 */


public class TestngAdaptor {

    private TestngAdaptor() {
    }

    public static TestCase covertToTestCase(TestCase tc, ITestResult result) {
        tc.setStartedMillis(result.getStartMillis());
        tc.setEndMills(result.getEndMillis());
        tc.setParameters(result.getParameters());
        tc.setTestClassName(result.getTestClass().getRealClass().getSimpleName());
        tc.setTestRealClassName(result.getTestClass().getRealClass().getName());
        tc.setStatus(result.getStatus());
        tc.setTestMethodName(result.getMethod().getMethodName());
        tc.setErrors(result.getThrowable());
        setTestDescription(tc,result);
        return tc;
    }

    public static void updateResult(TestCase tc,ITestResult result){
        tc.setStatus(result.getStatus());
        tc.setErrors(result.getThrowable());
    }

    private static void setTestDescription(TestCase tc, ITestResult result) {
        String des = null;
        for (Object o : result.getParameters()) {
            if (o instanceof TestDescription) {
                if (StrUtil.isNotBlank(((TestDescription) o).getTestDescription())) {
                    des = ((TestDescription) o).getTestDescription();
                    break;
                }
            }
        }
        if (null == des) des = result.getMethod().getDescription();
        if(StrUtil.isNotBlank(des)){
            tc.setTestDescription(des);
        }else{
            tc.setTestDescription(result.getMethod().getMethodName());
        }

    }
}
