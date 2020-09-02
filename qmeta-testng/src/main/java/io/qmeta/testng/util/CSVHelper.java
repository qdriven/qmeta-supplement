package io.qmeta.testng.util;

import au.com.bytecode.opencsv.CSVReader;

import com.google.common.collect.Lists;
import jodd.bean.BeanUtil;
import org.apache.commons.beanutils.BeanUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 将CSV文件内容复制到的java bean 类
 */
public final class CSVHelper {

    private CSVHelper() {
    }

    private static Object[] convertToObjectArray(String[] header, String[] row, Map<String, Class> clazzMap)
            throws Exception {
        return ExcelHelper.convertToObjectArray(Lists.newArrayList(header),
                Lists.newArrayList(row), clazzMap);
    }

    /**
     * 将csv数据转换成testng Data provider的类型Iterator<Object[]>
     *
     * @param filePath
     * @param clazzMap
     * @return Iterator<Object[]>
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public static Iterator<Object[]> loadCSVtoIterator(String filePath, Map<String, Class> clazzMap) throws Exception {
        String path = CSVHelper.class.getResource("/").getPath() + filePath;
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
        List<String[]> inputs = reader.readAll();
        String[] header = inputs.get(0); //get header
        inputs.remove(0);
        List<Object[]> values = new ArrayList<>();
        for (String[] input : inputs) {
            values.add(convertToObjectArray(header, input, clazzMap));
        }
        return values.iterator();
    }


}
