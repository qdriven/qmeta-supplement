package io.qmeta.supplement;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/** @author: patrick on 2020/1/19 @Description: */
class ArithUtilsTest {

  @Test
  void addTest() {
    double result = ArithmeticUtils.add(1.00, 3.09);
    Assertions.assertThat(result).isEqualTo(4.09);
  }

  @Test
  void testSub() {
    double result = ArithmeticUtils.sub(1.00, 3.09);
    Assertions.assertThat(result).isEqualTo(-2.09);
  }

  @Test
  void testMul() {
    double result = ArithmeticUtils.mul(1.03, 3.09);
    assertThat(result).isEqualTo(3.1827);
  }

  @Test
  void testDiv() {
    double result = ArithmeticUtils.div(1.03, 3.09);
    assertThat(result).isEqualTo(0.3333333333);
  }

  @Test
  void testDivWithScale() {
    double result = ArithmeticUtils.div(1.03, 3.09, 4);
    assertThat(result).isEqualTo(0.3333);
  }

  @Test
  void testRound() {
    double result = ArithmeticUtils.round(1.039980, 4);
    assertThat(result).isEqualTo(1.0400);
  }
}
