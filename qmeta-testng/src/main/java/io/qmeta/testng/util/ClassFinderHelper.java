package io.qmeta.testng.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by patrick on 15/8/28.
 * 查找本地的Class文件
 */
public class ClassFinderHelper {
    private static final Logger logger = LogManager.getLogger(ClassFinderHelper.class.getName());

    private static final String PACKAGE_SEPARATOR = "\\.";

    private enum FileProtocol {
        file, jar
    }

    private ClassFinderHelper() {

    }

    /**
     * 获取类名列表
     *
     * @param pkgName     包名
     * @param isRecursive 是否递归
     * @return
     */
    public static List<Class> getClassListByPackageName(String pkgName, boolean isRecursive) {
        List<Class> classList = Lists.newArrayList();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            String pkgFolderName = pkgName.replaceAll(PACKAGE_SEPARATOR, File.separator);
            Enumeration<URL> urls = loader.getResources(pkgFolderName);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (null != url) {
                    if (FileProtocol.file.name().equalsIgnoreCase(url.getProtocol())) {
                        String pkgPath = url.getPath();
                        classList.addAll(getLocalClassList(pkgName, pkgPath, isRecursive));
                    } else if (FileProtocol.jar.name().equalsIgnoreCase(url.getProtocol())) {
                        classList.addAll(getJarClassListByName(pkgName, url, isRecursive));
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.error("error_result={}", e);
        }
        return classList;

    }

    /**
     * 获取本地的类名列表
     *
     * @param pkgName     包名
     * @param pkgPath     包路径
     * @param isRecursive 是否递归
     * @return
     * @throws ClassNotFoundException
     */
    private static List<Class> getLocalClassList(String pkgName, String pkgPath,
                                                 boolean isRecursive)
            throws ClassNotFoundException {

        List<Class> result = Lists.newArrayList();
        File[] files = findFilesInPackage(pkgPath);
        for (File file : files) {
            if (file.isFile()) {
                logger.info("start getting class {}", file.getName());
                result.add(Class.forName(getClassName(pkgName, file.getName())));
            } else {
                if (isRecursive) {
                    String subPkgPath = pkgPath + File.separator + file.getName();
                    String subPkgName = pkgName + "." + file.getName();
                    result.addAll(getLocalClassList(subPkgName, subPkgPath, isRecursive));
                }
            }
        }
        return result;
    }

    private static File[] findFilesInPackage(String pkgPath) {
        //hard code to filter different class here
        return new File(pkgPath).listFiles(item ->
                (item.isFile() && item.getName().endsWith(".class")
                        && !item.getName().contains("$")
                        && !item.getName().contains("Test")) || item.isDirectory()
        );
    }

    private static String getClassName(String packageName, String fileName) {
        int endIndex = fileName.lastIndexOf(".");
        String className = packageName;
        if (endIndex >= 0) {
            className = packageName + "." +
                    fileName.substring(0, endIndex);
        } else {
            logger.warn("invalid_file={}", fileName);
        }

        return className;
    }

    /**
     * todo: remove isRecursive
     *
     * @param pkgName
     * @param url
     * @param isRecursive
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<Class> getJarClassListByName(String pkgName
            , URL url, boolean isRecursive) throws IOException, ClassNotFoundException {
        List<Class> result = Lists.newArrayList();
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry entry = jarEntries.nextElement();
            String name = entry.getName();
            String pkgPath = pkgName.replaceAll(PACKAGE_SEPARATOR, File.separator);
            if (name.startsWith(pkgPath) && name.endsWith(".class")
                    && !name.contains("$")) {
                String separatorChar = File.separatorChar == '\\' ? "\\\\" : File.separator;
                String className = name.replaceAll(separatorChar, PACKAGE_SEPARATOR);
                int endIndex = className.lastIndexOf(".");
                String clazzName = className.substring(0, endIndex);
                result.add(Class.forName(clazzName));
            }

        }

        return result;

    }

    /**
     * only for writing markdown documents
     *
     * @param pkgName
     * @param isRecursive: pull the parent class methods
     * @return
     */
    @Deprecated
    public static Map<String, String> pullMethods(String pkgName, boolean isRecursive) {
        List<Class> classes = getClassListByPackageName(pkgName, isRecursive);
        Map<String, String> methods = Maps.newHashMap();
        for (Class aClass : classes) {
            StringBuilder sb = new StringBuilder();
            Method[] declaredClassMethods = aClass.getDeclaredMethods();
            for (Method declaredClassMethod : declaredClassMethods) {
                sb.append(declaredClassMethod.getName()).append(",");
            }
            methods.put(aClass.getSimpleName(), sb.toString());
        }
        System.out.println("|控件|默认操作|可用操作|说明|");
        System.out.println("|---- |------|---- |-----|");
        for (Map.Entry<String, String> entry : methods.entrySet()) {
            String mask = "|%s|默认操作|%s|说明|";
            System.out.println(String.format(mask, entry.getKey(), entry.getValue()));
        }

        return methods;
    }

}
