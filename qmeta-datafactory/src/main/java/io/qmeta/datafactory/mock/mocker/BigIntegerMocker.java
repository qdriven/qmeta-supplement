package io.qmeta.datafactory.mock.mocker;



import io.qmeta.datafactory.mock.core.DataConfig;
import io.qmeta.datafactory.mock.core.Mocker;
import java.math.BigInteger;

/**
 * BigInteger对象模拟器
 */
public class BigIntegerMocker implements Mocker<BigInteger> {
  @Override
  public BigInteger mock(DataConfig mockConfig) {
   return BigInteger.valueOf(mockConfig.globalConfig().getMocker(Long.class).mock(mockConfig));
  }

}
