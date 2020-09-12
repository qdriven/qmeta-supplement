package io.qmeta.datafactory.mock.mocker;

import io.qmeta.datafactory.mock.core.DataConfig;
import io.qmeta.datafactory.mock.core.Mocker;
import java.math.BigDecimal;


/**
 * BigDecimal对象模拟器
 */
public class BigDecimalMocker implements Mocker<BigDecimal> {

  @Override
  public BigDecimal mock(DataConfig mockConfig) {
    return BigDecimal.valueOf(mockConfig.globalConfig().getMocker(Double.class).mock(mockConfig));
  }

}
