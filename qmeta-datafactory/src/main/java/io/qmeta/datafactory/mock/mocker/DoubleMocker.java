package io.qmeta.datafactory.mock.mocker;

import cn.hutool.core.util.StrUtil;
import io.qmeta.datafactory.mock.core.DataConfig;
import io.qmeta.datafactory.mock.core.Mocker;
import io.qmeta.datafactory.mock.util.RandomUtils;

import java.math.BigDecimal;

/**
 * Double对象模拟器
 */
public class DoubleMocker implements Mocker<Double> {

  @Override
  public Double mock(DataConfig mockConfig) {
    /**
     * 若根据正则模拟
     */
    if(StrUtil.isNotEmpty(mockConfig.numberRegex())){
      return RandomUtils.nextNumberFromRegex(mockConfig.numberRegex()).setScale(mockConfig.decimalScale(),BigDecimal.ROUND_FLOOR).doubleValue();
    }
    return new BigDecimal(RandomUtils.nextDouble(mockConfig.doubleRange()[0], mockConfig.doubleRange()[1])).setScale(mockConfig.decimalScale(),BigDecimal.ROUND_FLOOR).doubleValue();
  }
}
