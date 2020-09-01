package io.qmeta.testng.listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Created by patrick on 15/3/5.
 *
 * @version $Id$
 */


public class SimpleRetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private int maxRetryCount = 2;
    @Override
    public boolean retry(ITestResult result) {
        if(retryCount<maxRetryCount){
            System.out.println("retrying testing "+result.getName()+" why:  "
                    +getResultStatusName(result.getStatus()));
            retryCount++;
            result.setStatus(1); //modify to true,if failed, it will reset to false
            return true;
        }

        return false;
    }

    public String getResultStatusName(int status){
        String resultName = null;
        if(status==1)
            resultName = "SUCCESS";
        if(status==2)
            resultName = "FAILURE";
        if(status==3)
            resultName = "SKIP";
        return resultName;
    }
}
