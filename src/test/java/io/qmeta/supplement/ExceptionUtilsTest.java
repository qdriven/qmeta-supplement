package io.qmeta.supplement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ExceptionUtilsTest {

  @Test
  void testGetStackTrace() {

    try {
      int a = 2 / 0;
    } catch (Exception e) {
      String stackTrace = ExceptionUtils.getStackTrace(e);
      System.out.println(stackTrace);
    }
  }
}
