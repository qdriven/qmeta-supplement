package io.qmeta.experimental.jvm;

import io.qmeta.experimental.jvm.demo.SimpleInt;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

//https://www.baeldung.com/java-memory-layout
//https://www.baeldung.com/java-heap-memory-api
//https://www.baeldung.com/2048-java-solver
public class JvmDetails {
    public static void main(String[] args) {
        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseClass(SimpleInt.class).toPrintable());
        SimpleInt instance = new SimpleInt();
        System.out.println(ClassLayout.parseInstance(instance).toPrintable());
        System.out.println("The identity hash code is " + System.identityHashCode(instance));
        System.out.println(ClassLayout.parseInstance(instance).toPrintable());
    }
}
