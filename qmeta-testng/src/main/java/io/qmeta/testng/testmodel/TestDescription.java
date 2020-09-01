package io.qmeta.testng.testmodel;

import io.ift.automation.helpers.ExcelHelper;
import io.ift.automation.helpers.StringHelper;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by patrick on 15/6/8.
 *
 * @version $Id: TestDescription.java 2837 2016-04-05 09:21:06Z wuke $
 */


public class TestDescription {
    private String testCaseId;
    private String testMethodName;
    private String testDescription;
    private String priority;
    private String checkPointClasses;
    private List<Class> checkPoints = Lists.newArrayList();
    private static final Logger logger = LogManager.getLogger(TestDescription.class.getName());

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getTestMethodName() {
        return testMethodName;
    }

    public void setTestMethodName(String testMethodName) {
        this.testMethodName = testMethodName;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("testCaseId", testCaseId)
                .add("testMethodName", testMethodName)
                .add("testDescription", testDescription)
                .add("priority", priority)
                .toString();
    }

    public static Iterator<Object[]> filterByMethod(String testCasePath,Method m, Map<String, Class> clazzMap) {
        Iterator<Object[]> source = null;
        try {
            source = ExcelHelper.build(testCasePath).ToIteratorInColMode(clazzMap);
        } catch (Exception e) {
            logger.error("unexpected error happened ,can't process test anymore, {}", e);
            throw new RuntimeException(e);
        }
        List<Object[]> data = Lists.newArrayList();
        while (source.hasNext()){
            Object[] objects = source.next();
            for (Object object : objects) {
                if(object instanceof TestDescription){
                    if(((TestDescription) object).getTestMethodName()==null){
                        continue;
                    }
                    if(((TestDescription)object).getTestMethodName().equalsIgnoreCase(m.getName())){
                        data.add(objects);
                    }else{
                        logger.info("数据"+ StringHelper.join(objects,",")+"被过滤了....");
                    }
                    break;
                }
            }
        }

        return data.iterator();
    }

    public String getCheckPointClasses() {
        return checkPointClasses;
    }

    //todo need to add bdd style
    public void initCheckPointClasses(String checkPointClasses) throws ClassNotFoundException {

        this.checkPointClasses = checkPointClasses;
        if(checkPointClasses==null||!checkPointClasses.startsWith("com")) throw  new RuntimeException(checkPointClasses+"检查点输入错误，请校验");
        String[] classes = checkPointClasses.split("-");
        for (String aClass : classes) {
            checkPoints.add(Class.forName(aClass.trim()));
        }
    }

    public List<Class> getCheckPoints() {
        return checkPoints;
    }

    public void setCheckPoints(List<Class> checkPoints) {
        this.checkPoints = checkPoints;
    }
}
