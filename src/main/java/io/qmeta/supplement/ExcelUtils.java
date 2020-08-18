package io.qmeta.supplement;

import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import java.util.List;

/** @author: patrick on 2020/1/19 @Description: */
public class ExcelUtils extends ExcelUtil {

  public static List<?> readExcelToObjects(String filePath, Class<?> toType) {
    return EasyExcel.read(filePath, toType, null).sheet().doReadSync();
  }

  public static void writeObjectsToExcel(String filePath, List<?> rows, Class<?> toType) {
    EasyExcel.write(filePath, toType).sheet().doWrite(rows);
  }
}
