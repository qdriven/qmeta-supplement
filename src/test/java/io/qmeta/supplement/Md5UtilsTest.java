package io.qmeta.supplement;

import org.junit.jupiter.api.Test;

/** @author: patrick on 2020/1/19 @Description: */
class Md5UtilsTest {

  @Test
  void testMd5() {
    System.out.println(Md5Utils.md5("test"));
  }

  @Test
  void hash() {
    System.out.println(Md5Utils.hash("test"));
  }
}
