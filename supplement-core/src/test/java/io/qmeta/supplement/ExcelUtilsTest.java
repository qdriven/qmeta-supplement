package io.qmeta.supplement;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.List;

import lombok.Data;
import lombok.ToString;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ExcelUtilsTest {
    @Data
    @ToString
    public static class DemoExcelRW {
        @ExcelProperty(value = "personal name")
        private String name = "name";

        @ExcelProperty(value = "personal age")
        private int age = 10;

        private String floatStr = "1.1000";
        private float floatNumber = (float) 1.1000;
        private double doubleNumber = 1.009;
    }

    @Test
    void testReadExcelToObjects() {
        List<DemoExcelRW> result =
                (List<DemoExcelRW>)
                        ExcelUtils.readExcelToObjects("demo_easy_excel.xlsx", DemoExcelRW.class);
        Assertions.assertThat(result.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void testWriteExcelToObjects() {
        DemoExcelRW rw = new DemoExcelRW();
        ExcelUtils.writeObjectsToExcel("demo_easy_excel.xlsx", List.of(rw), DemoExcelRW.class);
    }
}
