package io.qmeta.testng.util;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.qmeta.testng.TestNGTestContextAware;
import io.qmeta.testng.listener.TestResultLogger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
    private static final String classPath = TemplateReportHelper.class.getClassLoader().getSystemResource("").getPath();

//    private String outputPath;

    private TemplateReportHelper() {
    }
    public static class ScreenShotUtils {

        private ScreenShotUtils() {
        }

        private static final String SCREENSHOT_PATH = "target/screenshots/";
        private static final String TEST_SCREENSHOT_PATH = "simple-report/screenshots/";
        private static final String PIC_SUFFIX = ".jpg";
        private static String classPath = ScreenShotUtils.class.getClassLoader().getResource("").getPath();

        @Deprecated
        public static String takeScreenshot(WebDriver driver) {
            File file = FileHelper.createFile(SCREENSHOT_PATH, generateFileName());
            File pic = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(pic, file);
            } catch (IOException e) {

            }
            return file.getAbsolutePath();
        }

        public static void takeScreenshot(WebDriver driver, String path) {
            File file = FileHelper.createFile(path);
            File pic = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(pic, file);
            } catch (Exception e) {
                //ignore this exception
            }
        }

        public static String takeScreenshotForSimpleReport(WebDriver driver) {
            String fileName = generateFileName();
            FileHelper.createDir(classPath + TEST_SCREENSHOT_PATH);
            String jpgPath = classPath + TEST_SCREENSHOT_PATH + fileName;
            takeScreenshot(driver, jpgPath);
            return "screenshots/" + fileName;
        }

        private static String generateFileName() {
            return "screenshot-" + System.currentTimeMillis() + PIC_SUFFIX;
        }
    }

    public static void openReport() {
        // 判断当前系统是否支持Java AWT Desktop扩展
        String flag = "true";
        if (flag == null || flag.equalsIgnoreCase("false")) return;
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                // 创建一个URI实例
                String classLoaderPath = TemplateReportHelper.class.getClassLoader().getResource("").getPath();
                String reportPath = "file:///" + classLoaderPath + "simple-report/testresult.html";
                java.net.URI uri = java.net.URI.create(reportPath);
                // 获取当前系统桌面扩展
                java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                // 判断系统桌面是否支持要执行的功能
                if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    // 获取系统默认浏览器打开链接
                    dp.browse(uri);
                }
            } catch (Exception e) {
                // 此为uri为空时抛出异常
                TestResultLogger.error(e);
            }
        }
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
        FileHelper.writeToFile(path,String.format("new Vue(%s);", JSONUtil.toJsonPrettyStr(data)),false);

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
                    StandardCharsets.UTF_8));
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
                    new FileOutputStream(file), StandardCharsets.UTF_8));
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
