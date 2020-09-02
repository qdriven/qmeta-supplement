package io.qmeta.testng.util;

import io.ift.automation.logging.TestResultLogger;
import io.ift.automation.tm.TestNGTestContextAware;
import com.google.common.collect.Lists;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by patrick on 15/3/13.
 *
 * @version $Id$
 */


public class TemplateReportHelper {
    private static final String SIMPLE_REPORT_TEMPLATE_DIR = "templates/reports/";
    private static final String CODES_TEMPLATE_DIR = "/templates/codes/";
    private static final String JAR_TEMPLATE_DIR = "templates/codes/";
    private static final String SIMPLE_REPORT_TEMPLATE_FILE = "testresult.ftl";
    private static final String SIMPLE_REPORT_DIR = "simple-report/";
    private static final String SIMPLE_REPORT_FILE = "testresult.html";
    private static String classPath = TemplateReportHelper.class.getClassLoader().getSystemResource("").getPath();

//    private String outputPath;

    private TemplateReportHelper() {
    }

    public static void generateTestNGVueReport(TestNGTestContextAware.VueData data){
        List<String> fileLists = Lists.newArrayList("bootstrap-theme.min.css",
                "bootstrap.js",
                "bootstrap.min.css",
                "bootstrap.min.js",
                "dashboard.css",
                "jquery.min.js",
                "testresult.html",
                "vue.min.js");

        File simpleReportDir = new File(classPath+SIMPLE_REPORT_DIR);
        if(!simpleReportDir.exists()) simpleReportDir.mkdir();

        for (String file : fileLists) {
            String origin= FileHelper.readFileToString(SIMPLE_REPORT_DIR+file);
            String path = FileHelper.createFileInClassPath(SIMPLE_REPORT_DIR,file).getAbsolutePath();
            FileHelper.writeToFile(path,origin,false);
        }

        String path = FileHelper.createFileInClassPath(SIMPLE_REPORT_DIR,"testresult.js").getAbsolutePath();
        FileHelper.writeToFile(path,String.format("new Vue(%s);",JSONHelper.toJSONString(data)),false);

    }
    /**
     * 生成测试报告, test result is refactored
     * @param testResultData
     * @throws IOException
     * @throws TemplateException
     */
    @Deprecated
    public static void processSimpleReport(Map testResultData) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        File templateFolder = new File(classPath + SIMPLE_REPORT_TEMPLATE_DIR);
        if(templateFolder.exists()) {
            TestResultLogger.log("url={}", templateFolder);
            cfg.setDirectoryForTemplateLoading(new File(classPath + SIMPLE_REPORT_TEMPLATE_DIR));
        } else {
            cfg.setClassForTemplateLoading(TemplateReportHelper.class, "/");
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        Writer out =null ;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(FileHelper.createFileInClassPath(SIMPLE_REPORT_DIR, SIMPLE_REPORT_FILE)),
                    "UTF-8"));
            Template template ;
            if(templateFolder.exists()) {
                template = cfg.getTemplate(SIMPLE_REPORT_TEMPLATE_FILE);
            } else {
                template = cfg.getTemplate(SIMPLE_REPORT_TEMPLATE_DIR + SIMPLE_REPORT_TEMPLATE_FILE);
            }
            template.process(testResultData, out);
        }finally{
            if(null!=out){
                out.close();
            }

        }
    }

    /**
     * 根据模版生成代码
     * @param dataSet
     * @param templateLocation
     * @param generatedFileLocation
     * @return
     * @throws Exception
     */
    public static File generateAPICodes(Map dataSet,String templateLocation,String generatedFileLocation) throws Exception {

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        File templateFolder = new File(classPath+CODES_TEMPLATE_DIR);
        if(templateFolder.exists()){
            TestResultLogger.log("url={}",templateFolder);
            cfg.setDirectoryForTemplateLoading(new File(classPath + CODES_TEMPLATE_DIR));
        }else{
            cfg.setClassForTemplateLoading(TemplateReportHelper.class,"/");
        }

        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        Writer out = null;
        File file = FileHelper.createFile(generatedFileLocation);
        try {
            //out = new FileWriter(file);
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF-8"));
            Template template;
            if(templateFolder.exists()){
                template= cfg.getTemplate(templateLocation);
            }else{

                template= cfg.getTemplate(JAR_TEMPLATE_DIR+templateLocation);
            }

            template.process(dataSet, out);
        }finally{
            if(null!=out){
                out.close();
            }

        }

        return file;
    }
}
