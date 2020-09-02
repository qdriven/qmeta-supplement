package io.qmeta.testng.util;

import io.ift.automation.commonflows.models.EmployeeTestData;
import io.ift.automation.data.TestData;
import io.ift.automation.testscaffold.apitest.ServiceDescription;
import io.ift.automation.testscaffold.codegenerator.webui.WebElementDescription;
import io.ift.automation.tm.testmodel.TestDescription;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import jodd.bean.BeanUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by patrick on 15/3/10.
 * 目前此工具支持excel2007以前的版本，如果使用高级版本，请保存的时候
 * 保存为excel2003格式
 *
 * @version $Id$
 */

@SuppressWarnings("unchecked")
public class ExcelHelper {
    private static final String EXCELT_XLS = ".xls";
    private static final String EXCELT_XLSX = ".xlsx";
    private static final List<String> testDescriptionOutputKeys = Lists.newArrayList("testDescription"
            , "priority", "testMethodName");

    private ExcelHelper() {
    }

    private static final String EMPTY = "EMPTY";
    private HSSFSheet sheet;
    private static final Logger logger = LogManager.getLogger(ExcelHelper.class.getName());

    /**
     * @param path      文件路径
     * @param sheetName excel sheet 名字
     * @return ExcelHelper
     */
    public static ExcelHelper build(String path, String sheetName) {
        ExcelHelper helper = new ExcelHelper();
        helper.sheet = helper.getSheet(path, sheetName);
        return helper;
    }

    /**
     * 创建excelhelper 实例
     *
     * @param path
     * @return
     */
    public static ExcelHelper build(String path) {
        ExcelHelper helper = new ExcelHelper();
        helper.sheet = helper.getSheet(path, "");
        return helper;
    }

    public static ExcelHelper build() {
        return new ExcelHelper();
    }

    /**
     * 根据PageObject 写入excel
     *
     * @param elements
     * @param fileName
     * @param <T>
     * @return
     */
    public <T> File createWebFlowExcelFile(List<T> elements, String fileName) {
        TreeMap<String, String> headerValueMap = Maps.newTreeMap();
        headerValueMap.put("元素名称", "elementName");
        headerValueMap.put("元素变量名", "variableName");
        headerValueMap.put("元素类型", "elementType");
        headerValueMap.put("元素定位", "elementLocation");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("testflow");
        writeHeader(sheet, headerValueMap.keySet());
        writeContent(sheet, headerValueMap, elements);
        File file = FileHelper.createFile(fileName);
        FileOutputStream fOut = null;
        writeToExcelFile(fileName, workbook, fOut);

        return file;


    }

    /**
     * 将实例对象根据excel header和字段对应关系写入excel
     *
     * @param sheet
     * @param headerFieldMapping
     * @param elements
     * @param <T>
     */
    private <T> void writeContent(HSSFSheet sheet,
                                  Map<String, String> headerFieldMapping, List<T> elements) {
        int rowIndex = 1;
        for (T element : elements) {
            HSSFRow row = sheet.createRow(rowIndex);
            writeLine(row, headerFieldMapping, element);
            rowIndex++;
        }
    }


    /**
     * 写一行数据
     *
     * @param row
     * @param values
     */
    private static void writeLine(HSSFRow row, Set<String> values) {

        int cellIndex = 0;
        for (String header : values) {
            HSSFCell cell = row.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(header);
            cellIndex++;
        }
    }

    /**
     * 将实例对象根据excel header和字段对应关系写入excel
     *
     * @param row
     * @param headerFieldMapping
     * @param object
     * @param <T>
     */
    private <T> void writeLine(HSSFRow row,
                               Map<String, String> headerFieldMapping, final T object) {

        int cellIndex = 0;
        for (Map.Entry<String, String> entry : headerFieldMapping.entrySet()) {
            HSSFCell cell = row.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue((String) ReflectionHelper.getFieldValue(object, entry.getValue()));
            cellIndex++;
        }
    }

    /**
     * 写excel 头
     *
     * @param sheet
     * @param values
     */
    private void writeHeader(HSSFSheet sheet, Set<String> values) {
        HSSFRow row = sheet.createRow(0);
        writeLine(row, values);
    }

    /**
     * 创建API测试数据
     * //todo need refactor
     *
     * @param fileName
     * @param sd
     * @return
     */

    public File createTestCaseDataToExcel(String fileName, ServiceDescription sd, String apiName) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("testdata");
//        HSSFRow row = sheet.createRow(0);

        int rowCount = 0;
        HSSFRow row = sheet.createRow(rowCount++);
        HSSFCell cell = row.createCell(0, HSSFCell.CELL_TYPE_STRING);
        HSSFCell cell_sample = row.createCell(1, HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("statusCode");
        cell_sample.setCellValue("200");

        row = sheet.createRow(rowCount++);
        cell = row.createCell(0, HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("expectedBodyResult");
        row = sheet.createRow(rowCount++);
        cell = row.createCell(0, HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("RequestData.body");

        row = sheet.createRow(rowCount++);
        cell = row.createCell(0, HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("RequestData.bodyTemplate");

        row = sheet.createRow(rowCount++);
        cell = row.createCell(0, HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("RequestData.isAuth");

        for (String s : sd.getPathParameters()) {
            HSSFRow row1 = sheet.createRow(rowCount);
            HSSFCell cell1 = row1.createCell(0, HSSFCell.CELL_TYPE_STRING);
            cell1.setCellValue(String.format("RequestData.pathParameters(%s)", s));
            rowCount++;
        }

        for (String s : sd.getQueryParameters()) {

            HSSFRow row1 = sheet.createRow(rowCount);
            HSSFCell cell1 = row1.createCell(0, HSSFCell.CELL_TYPE_STRING);
            cell1.setCellValue(String.format("RequestData.queryParameters(%s)", s));
            rowCount++;
        }

        //write test description
        Map<String, String> testDesc;
        try {
            testDesc = BeanUtils.describe(new TestDescription());

            for (Map.Entry<String, String> entry : testDesc.entrySet()) {
                if (testDescriptionOutputKeys.contains(entry.getKey())) {
                    row = sheet.createRow(rowCount++);
                    cell = row.createCell(0, HSSFCell.CELL_TYPE_STRING);

                    cell.setCellValue("TestCase." + entry.getKey());
                    if (entry.getKey().equalsIgnoreCase("testMethodName")) {
                        cell_sample = row.createCell(1, HSSFCell.CELL_TYPE_STRING);
                        cell_sample.setCellValue("test" + StringHelper.capitalize(apiName));
                    }

                    if (entry.getKey().equalsIgnoreCase("priority")) {
                        cell_sample = row.createCell(1, HSSFCell.CELL_TYPE_STRING);
                        cell_sample.setCellValue("P1");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("未知错误" + e);
        }

        //write useCode

        try {
            testDesc = BeanUtils.describe(new EmployeeTestData());
            for (Map.Entry<String, String> entry : testDesc.entrySet()) {
                if ("class".equalsIgnoreCase(entry.getKey())) continue;
                if (entry.getKey().equalsIgnoreCase("userCode")) {
                    row = sheet.createRow(rowCount++);
                    cell = row.createCell(0, HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue("User." + entry.getKey());
                    break;
                }

            }
        } catch (Exception e) {
            throw new RuntimeException("未知错误" + e);
        }


        File file = FileHelper.createFile(fileName);
        FileOutputStream fOut = null;
        writeToExcelFile(fileName, workbook, fOut);

        return file;

    }

    /**
     * write to excel file
     *
     * @param fileName
     * @param workbook
     * @param fOut
     */
    private void writeToExcelFile(String fileName, HSSFWorkbook workbook, FileOutputStream fOut) {
        try {
            fOut = new FileOutputStream(fileName);
            workbook.write(fOut);
            fOut.flush();
        } catch (IOException e) {
            logger.error("error_exception={}", e);
        } finally {
            if (fOut != null) try {
                fOut.close();
            } catch (IOException e) {
                logger.error("error_exception={}", e);
            }
        }
    }


    /**
     * 转换excel数据到 object[],作为数据驱动入口
     *
     * @param header
     * @param data
     * @param clazzMap
     * @return Object[]
     * @throws Exception
     */
    public static Object[] convertToObjectArray(List<String> header, List<String> data,
                                                Map<String, Class> clazzMap) throws Exception {
        //可能的空行特殊处理
        List<String> realData = Lists.newArrayList();
        for (int i = 0; i < header.size(); i++) {
            realData.add(data.get(i));
        }

        if (header.size() != realData.size()) throw new RuntimeException(data
                + " 数据不对,请检查excel文件,数据的描述头和实际的数据不匹配");
        List<Object> objs = new ArrayList();

        Map<String, Object> mappedInstanceHolder
                = new HashMap<>();

        // 开始解析excel
        for (int i = 0; i < realData.size(); i++) {
            int pos = header.get(i).indexOf(".");
            String className, fieldName;
            if (pos > 0) {
                className = header.get(i).substring(0, pos);
                fieldName = header.get(i).substring(pos + 1);
                fieldName = fieldName.replaceAll("\\(", "[").replaceAll("\\)", "]");
                if (null == clazzMap.get(className)) { //no mapping class defined
                    objs.add(realData.get(i));
                } else { //mapping class defined
                    Object o = mappedInstanceHolder.get(className);
                    if (null == o) {
                        o = ReflectionHelper.newInstance(clazzMap.get(className));
                        mappedInstanceHolder.put(className, o);
                        objs.add(o);
                    }
                    if (StringHelper.isNotEmptyOrNotBlankString(realData.get(i))) {
                        if (EMPTY.equalsIgnoreCase(realData.get(i).trim())) {
                            BeanUtil.setPropertyForced(o, fieldName, "");
                        } else {
                            BeanUtil.setPropertyForced(o, fieldName, realData.get(i).trim());
                        }
                    }
                }
            } else {// no class defined
                objs.add(realData.get(i));
            }
        }
        //process after bean process
        objs.stream().filter(obj -> obj instanceof TestData).forEach(obj -> {
            ((TestData) obj).dataComposeAfter();
            //add test data to thread local web test context
//            DriverFactory.getThreadLevelTestContext().addTestData((TestData)obj);
        });
        return objs.toArray();
    }

    /**
     * testng 数据驱动入口
     *
     * @param clazzMap
     * @return Iterator<Object[]>
     * @throws Exception
     */
    @Deprecated
    public Iterator<Object[]> loadExcelDataToIterator(Map<String, Class> clazzMap) throws Exception {

        List<String> headers = getHeaders();//get header
        List<Object[]> values = new ArrayList<>();
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            values.add(convertToObjectArray(headers, getData(i), clazzMap));
        }
        return values.iterator();
    }

    /**
     * Excel的第一列表述字段描述
     * 第二列开始表示数据
     * testng 数据驱动入口
     *
     * @param classMap
     * @return
     * @throws Exception
     */
    public Iterator<Object[]> ToIteratorInColMode(Map<String, Class> classMap) throws Exception {
        List<String> headers = buildFirstColumnAsHeader();
        Map<Integer, List<String>> allData = getColumnData();
        List<Object[]> values = Lists.newArrayList();
        for (Map.Entry<Integer, List<String>> entry : allData.entrySet()) {
            values.add(convertToObjectArray(headers, entry.getValue(), classMap));
        }
        return values.iterator();
    }

    /**
     * 获取所有的测试数据
     *
     * @return
     */
    private Map<Integer, List<String>> getColumnData() {
        int size = sheet.getRow(0).getPhysicalNumberOfCells();
        if (size == 1) throw new RuntimeException("请确保您的第一行测试数据是有数据的");
        Map<Integer, List<String>> allData = Maps.newHashMap();
        int rowNum = sheet.getPhysicalNumberOfRows();
        for (int cellNumber = 0; cellNumber < size - 1; cellNumber++) {
            for (int rowNumber = 0; rowNumber < rowNum; rowNumber++) {
                MapsHelper.put(allData, cellNumber, getCellFormatValue(sheet.getRow(rowNumber).getCell(cellNumber + 1)));
            }
        }
        return allData;
    }

    /**
     * 获取列模式下的头
     *
     * @return
     */
    private List<String> buildFirstColumnAsHeader() {
        List<String> headers = Lists.newArrayList();
        for (int rowNumber = 0; rowNumber < sheet.getPhysicalNumberOfRows(); rowNumber++) {
            try {
                if (!StringHelper.isNoneContentString(getStringCellValue(sheet.getRow(rowNumber).getCell(0)))) {
                    headers.add(getStringCellValue(sheet.getRow(rowNumber).getCell(0)));
                }
            } catch (Exception e) {
                //donothing to avoid null exception for some empty row existing in excel
                logger.warn("可以忽略以下错误:" + e);
            }

        }
        return headers;
    }

    @Deprecated
    public List<Object[]> loadExcelDataToList(Map<String, Class> clazzMap) throws Exception {

        List<String> headers = getHeaders();//get header

        List<Object[]> values = new ArrayList<>();
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            values.add(convertToObjectArray(headers, getData(i), clazzMap));
        }
        return values;
    }

    /**
     * 获取header
     *
     * @return
     */
    private List<String> getHeaders() {
        return getData(0);
    }

    /**
     * 读取Excel某行数据
     *
     * @param rowNum
     * @return List<String>
     */
    private List<String> getData(int rowNum) {
        HSSFRow row = sheet.getRow(rowNum);
        List<String> data = Lists.newArrayList();
        for (int i = 0; i < row.getLastCellNum(); i++) {
            data.add(getCellFormatValue(row.getCell(i)));
        }

        return data;
    }

    /**
     * 获取excel sheet
     *
     * @param path
     * @param sheetName
     * @return HSSFSheet
     */
    private HSSFSheet getSheet(String path, String sheetName) {
        FileHelper.checkIfSuitableFile(path, EXCELT_XLS, EXCELT_XLSX);
        HSSFWorkbook wb;
        POIFSFileSystem fs;
        try {
            fs = new POIFSFileSystem(ClassLoader.getSystemResourceAsStream(path));
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException("EXCEL 文件有问题,请检查你的文件是否真的是EXCEL文件,EXCEL目前只支持2003,不支持XLSX格式,文件在:" + path);
        }

        if (StringHelper.isNotEmptyOrNotBlankString(sheetName)) {
            return wb.getSheet(sheetName);
        } else {
            return wb.getSheetAt(0);
        }
    }


    /**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(HSSFCell cell) {
        String strCell = StringHelper.EMPTY;
        if (cell == null) return strCell;
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = StringHelper.EMPTY;
                break;
            default:
                strCell = StringHelper.EMPTY;
                break;
        }
        return strCell.replaceAll("”", "\"").replaceAll("“", "\"").replaceAll("：", ":");
    }

    /**
     * 根据HSSFCell类型设置数据
     *
     * @param cell
     * @return
     */
    private String getCellFormatValue(HSSFCell cell) {
        if (cell == null) return StringHelper.EMPTY;
        String cellValue = StringHelper.EMPTY;

        if (cell.getCellType() == 0) cell.setCellType(1);
        // 判断当前Cell的Type
        switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式

                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellValue = sdf.format(date);
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case HSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellValue = cell.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            case HSSFCell.CELL_TYPE_BOOLEAN:
                cellValue = cell.getBooleanCellValue() ? "true" : "false";
                break;
            default:
                cellValue = StringHelper.EMPTY;
        }
//        return cellValue.replaceAll("”", "\"").replaceAll("“", "\"").replaceAll("：", ":");
        return cellValue;

    }

    /**
     * 创建测试用例,数据排列是横向的,Excel 的Row
     *
     * @param testCaseName
     * @param path
     * @param header
     * @param data
     */
    public static void createTestCaseData(String testCaseName, String path, String header, List<List<String>> data) {
        HSSFWorkbook workBook = createWorkBookWithHeader(testCaseName, header);
        HSSFSheet sheet = workBook.getSheetAt(0);
        //write test data
        for (int i = 0; i < data.size(); i++) {
            HSSFRow r = sheet.createRow(i + 1);
            Object[] testData = data.get(i).toArray();
            for (int d = 0; d < testData.length; d++) {
                HSSFCell cell = r.createCell(d, HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(testData[d].toString());
            }
        }

        writeExcel(path, workBook);
    }

    /**
     * 创建测试数据Excel列
     *
     * @param testCaseName
     * @param path
     * @param dataInstance
     * @param <T>
     */
    public static <T extends TestData> void createTestCaseData(String testCaseName, String path, T dataInstance) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(testCaseName);
        //todo add new filter
        List<Field> fields = Lists.newArrayList(dataInstance.getClass().getDeclaredFields());
        Map<String, List<Field>> maps = Maps.newHashMap();
        maps.put(dataInstance.getClass().getSimpleName(), fields);
        maps.put("TestDescription", CollectionsHelper.filterForList(TestDescription.class.getDeclaredFields(),
                field -> !Lists.newArrayList("checkPointClasses", "checkPoints", "logger")
                        .contains(field.getName())));
        HSSFRow row;
        HSSFCell cell;
        for (int i = 0; i < fields.size(); i++) {
            row = sheet.createRow(i);
            cell = row.createCell(0, HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(fields.get(i).getName());
        }
        writeExcel(path, workbook);

    }

    /**
     * 写excel的头
     *
     * @param testCaseName
     * @param header
     * @return
     */
    private static HSSFWorkbook createWorkBookWithHeader(String testCaseName, String header) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(testCaseName);
        String[] headers = header.split(",");
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i, HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(headers[i]);
        }

        return workbook;
    }

    /**
     * 写Excel 文件
     *
     * @param path
     * @param workbook
     */
    private static void writeExcel(String path, HSSFWorkbook workbook) {
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(path);
            workbook.write(fOut);
            fOut.flush();
        } catch (IOException e) {
            logger.error("error_exception={}", e);
        } finally {
            if (fOut != null) try {
                fOut.close();
            } catch (IOException e) {
                //do nothing
            }
        }
    }

    /**
     * 创建测试数据，每一行数据以逗号间隔传入，每行数据最为data数组的一条记录
     *
     * @param testCaseName
     * @param path
     * @param header
     * @param data
     */
    public static void createTestCaseData(String testCaseName, String path,
                                          String header, String... data) {
        HSSFWorkbook workBook = createWorkBookWithHeader(testCaseName, header);
        HSSFSheet sheet = workBook.getSheetAt(0);

        //write test data
        for (int i = 0; i < data.length; i++) {
            HSSFRow r = sheet.createRow(i + 1);
            String[] testData = data[i].split(",");
            for (int d = 0; d < testData.length; d++) {
                HSSFCell cell = r.createCell(d, HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(testData[d]);
            }
        }
        writeExcel(path, workBook);
    }


    /**
     * 读取excel：
     * 返回Map<K,V>: K: PageName，V：List of WebElement in this page
     * 处理过程：
     * 1. 获取excel头描述所在的index，返回类似于enumerator的map
     * 2. 根据enumerator的map获取cell的值
     *
     * @return
     */
    @Deprecated
    public Map<String, List<WebElementDescription>> readWebElementDescription() {
        Map<String, String> headerValueMap = Maps.newHashMap();
        headerValueMap.put("元素名称", "elementName");
        headerValueMap.put("元素变量名", "variableName");
        headerValueMap.put("元素类型", "elementType");
        headerValueMap.put("元素定位", "elementLocation");
        headerValueMap.put("页面", "pageName");

        HSSFRow header = sheet.getRow(0);
        Map<Integer, String> descriptionEnumerator = Maps.newHashMap();
        int cellIndex = 0;
        while (header.getCell(cellIndex) != null) {
            String cellValue = getStringCellValue(header.getCell(cellIndex));
            if (headerValueMap.containsKey(cellValue)) {
                descriptionEnumerator.put(cellIndex, headerValueMap.get(cellValue));
            }
            cellIndex++;
        }

        int rowIndex = 1;
        Map<String, List<WebElementDescription>> result = Maps.newHashMap();
        HSSFRow currentRow = sheet.getRow(rowIndex);
        while (currentRow.getCell(0) != null) {

            WebElementDescription d = new WebElementDescription();
            for (Map.Entry<Integer, String> entry : descriptionEnumerator.entrySet()) {
                ReflectionHelper.setFieldValue(d, entry.getValue(), getStringCellValue(
                        currentRow.getCell(entry.getKey().intValue())));
            }
            MapsHelper.put(result, d.getPageName(), d);
            rowIndex++;
            currentRow = sheet.getRow(rowIndex);
        }

        return result;
    }

    /**
     * read all excel file to List, excel row as an element in list
     *
     * @return
     */
    public List<List<String>> readAll() {
        List<List<String>> result = Lists.newArrayList();
        HSSFRow header = sheet.getRow(0);
        int cellIndex = 0;
        List<String> headerList = Lists.newArrayList();
        while (header.getCell(cellIndex) != null) {
            String cellValue = getStringCellValue(header.getCell(cellIndex));
            headerList.add(cellValue);
            cellIndex++;
        }
        result.add(headerList);

        int rowIndex = 1;
        HSSFRow currentRow = sheet.getRow(rowIndex);
        while (currentRow != null) {
            List<String> currentLine = Lists.newArrayList();
            for (int i = 0; i < headerList.size(); i++) {
                currentLine.add(getStringCellValue(currentRow.getCell(i)));
            }
            result.add(currentLine);
            currentRow = sheet.getRow(++rowIndex);
        }

        return result;

    }

}
