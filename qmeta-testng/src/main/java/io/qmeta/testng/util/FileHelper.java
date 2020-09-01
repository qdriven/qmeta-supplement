package io.qmeta.testng.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 文件操作工具类
 */
public class FileHelper {

    private static final Logger logger = LogManager.getLogger(FileHelper.class.getName());
    private static final String classPath = getClassPath();
    public static final String separatorChar = File.separatorChar=='\\' ? "\\\\" : File.separator;
    private static final String SOURCEBASEPATH = classPath.replaceAll("target/classes/","")
            .replaceAll("target/test-classes/", "");

    private FileHelper() {
    }

    private static String getClassPath(){
        try {
            return java.net.URLDecoder.decode(FileHelper.class.getClassLoader().getResource("").getPath(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("error+{}",e);
        }

        throw new RuntimeException("可能你使用了中文名字作为了项目名称,或者在你的路径中");
    }

    public static String getTestSrcPath(){
        return SOURCEBASEPATH+ File.separator + "src/test/java/";
    }

    public static String getSrcPath(){
        return SOURCEBASEPATH+ "src/main/java/";
    }

    public static String getResourcePath() {
        return SOURCEBASEPATH + "src/main/resources/";
    }

    public static String getDirPathInResourcePath(String resourcePath) {
        return SOURCEBASEPATH +  "src/main/resources/"+resourcePath;
    }
    public static String getTestResourcePath(){
        return SOURCEBASEPATH + "src/test/resources/";
    }

    public static String getDirPathInTestResource(String resourcePath) {
        return SOURCEBASEPATH + "src/test/resources/"+resourcePath;
    }
    /**
     * package转换为路径格式
     */
    public static String packageToDir(String packageName) {
        String[] array = packageName.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (String str : array) {
            sb.append(str).append(File.separator);
        }
        return sb.toString();
    }

    public static Map<String,List<String>> getResourceDirFileMap(String dirPath) {
        Map<String,List<String>> maps = Maps.newHashMap();// {dir_name, list<file_name>}
        File dir = new File(dirPath);
        if(dir.isDirectory()) {
            File[] files = dir.listFiles();
            if(files==null) throw new RuntimeException("输入路径错误，请检查输入路径");
            for (File f : files) {
                if (f.isDirectory()) {
                    if(maps.get(dir.getName())==null){
                        maps.putAll(getResourceDirFileMap(dirPath + "/"+f.getName()));
                    }else{
                        maps.get(dir.getName()).addAll(
                                getResourceDirFileMap(dirPath + "/" + f.getName()).get(dirPath));
                    }
                } else {
                    if(maps.get(dir.getName())==null){
                        maps.put(dir.getName(), Lists.newArrayList(f.getName()));
                    }else{
                        maps.get(dir.getName()).add(f.getName());
                    }
                }
            }
        }
        return maps;
    }
    /**
     * 数据写入到文件
     * @param path
     * @param text
     * @throws java.io.IOException
     */
    public static File writeToFile(String path,String text) {

        File file = new File(path);
        if(!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("create file failed for "+file.getName());
        }
        ArrayList<String> texts = Lists.newArrayList(text);
        try {
            FileUtils.writeLines(file,"utf-8", texts, true);
        } catch (IOException e) {
            throw new RuntimeException("write file failed for "+file.getName()+"error="+e.getCause());
        }

        return file;
    }

    public static File writeToFile(String path,String text,boolean isAppend) {

        File file = new File(path);
        if(!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("create file failed for "+file.getName());
        }
        ArrayList<String> texts = Lists.newArrayList(text);
        try {
            FileUtils.writeLines(file,"utf-8",texts, isAppend);
        } catch (IOException e) {
            throw new RuntimeException("write file failed for "+file.getName()+"error="+e.getCause());
        }

        return file;
    }


    /**
     * 删除文件
     * @param path
     */
    public static boolean deleteFile(String path){
        File file = new File(path);
        return file.delete();

    }


    /**
     * Read file from class Path
     * @param pathInClassPath
     * @return string value of the file
     */
    public static String readFileToString(String pathInClassPath) {

        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(pathInClassPath);
            return IOUtils.toString(is, Charset.defaultCharset());
        } catch (Exception e) {
           logger.error("result={}",e.getStackTrace());
            throw new RuntimeException(pathInClassPath+" is not existing");
        }

    }

    public static File createFileInClassPath(String dir, String fileName) {

        createDir(classPath+dir+File.separator);
        return createFile(classPath+dir+fileName);
    }

    /**
     *
     * @param dir
     * @param fileName
     * @return
     */
    public static File createFile(String dir, String fileName) {
        createDir(dir);
        return createFile(dir+fileName);
    }

    public static File createFile(String fileName) {
        File file = new File(fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("error_exception={}", e);
            }
        }
        return file;
    }

    /**
     * check file if an appropriate file with correct extension name
     * @param path
     * @param fileSuffix
     */

    public static void checkIfSuitableFile(String path, String ...fileSuffix){

        for (String s : fileSuffix) {
            if(path.endsWith(s)) return;
        }
        throw  new RuntimeException(path+" ,File suffix is not correct,please check the file");

    }


    /**
     * 创建目录
     * @param dir  目录路径
     * @return 目录的文件实例
     */
    public static File createDir(String dir) {

        File d = new File(dir);
        if(!d.exists()){
            if(d.mkdir()) return d;
        }
        return d;
    }

    /**
     * create dir recursive
     * @param baseDir
     * @param appendDir
     * @return
     */
    public static File createDir(String baseDir,String appendDir) {
        String reformatDirs = appendDir.replaceAll("/",".").replaceAll(Pattern.quote("\\"),".");
        String[] appends = reformatDirs.split("\\.");

        File d = null;
        StringBuilder sb = new StringBuilder();
        sb.append(baseDir);
        for (String append : appends) {
            sb.append(append).append(File.separator);

            d=new File(sb.toString());
            if(!d.exists()){
                d.mkdir();
            }
        }
        return d;
    }
    /**
     * 是否存在于classPath
     * @param fileName
     * @return
     */
    public static boolean existsInClasspath(final String fileName) {
        return getResourceFromClasspath(fileName) != null;
    }

    /**
     * 获取文件的URL,包括从Jar包中获取
     * @param fileName
     * @return
     */
    public static URL getResourceFromClasspath(final String fileName) {

        return ClassLoader.getSystemClassLoader().getResource(fileName);
    }

    /**
     * 获取文件的路径
     * @param fileName
     * @return
     */
    public static String getFilePath(final String fileName) {
        if (existsInClasspath(fileName)) {
            return new File(getResourceFromClasspath(fileName).getPath()).getAbsolutePath();
        }
        return getPathForSystemFile(fileName);
    }

    /**
     * 获取系统文件的路径
     * @param fileName
     * @return
     */
    public static String getPathForSystemFile(final String fileName) {
        File file = new File(fileName);
        return file.getAbsolutePath();
    }

}
