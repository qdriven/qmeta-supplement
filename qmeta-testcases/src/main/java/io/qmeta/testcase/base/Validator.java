package io.qmeta.testcase.base;


import java.util.Iterator;


public class Validator {

    public static void assertNotNull(Object object, String message) {
        if (null == object)
            throw new IllegalArgumentException(message);
    }

    public static void assertHasNext(Iterator<?> iterator, String message) {
        if (!iterator.hasNext())
            throw new RuntimeException(message);
    }
}
