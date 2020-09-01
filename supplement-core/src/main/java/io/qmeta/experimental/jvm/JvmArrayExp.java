package io.qmeta.experimental.jvm;

import org.openjdk.jol.info.ClassLayout;

public class JvmArrayExp {

    public static void main(String[] args) {
        int[] ints = new int[42];
        System.out.println(ClassLayout.parseInstance(ints).toPrintable());

    }
}
