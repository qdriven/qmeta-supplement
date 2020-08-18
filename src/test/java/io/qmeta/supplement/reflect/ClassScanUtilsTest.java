package io.qmeta.supplement.reflect;

import org.junit.jupiter.api.Test;

import java.util.Set;

public class ClassScanUtilsTest {

    @Test
    public void testScanClass_inClassPath() {
        Set<Class<?>> result = ClassScanUtils.scanPackage("io.qmeta.supplement");
        for (Class<?> clz : result) {
            System.out.println(clz.getName());
        }
    }

    @Test
    public void testScanClass_InJar() {
        Set<Class<?>> result = ClassScanUtils.scanPackage("cn.hutool");
        for (Class<?> clz : result) {
            System.out.println(clz);
        }
    }
}
