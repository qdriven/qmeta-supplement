package io.qmeta.datafactory.mock.mocker;

import io.qmeta.datafactory.mock.core.DataConfig;
import io.qmeta.datafactory.mock.core.Mocker;
import io.qmeta.datafactory.mock.util.RandomUtils;
import io.qmeta.supplement.StringUtils;

/**
 * 模拟Short对象
 */
public class ShortMocker implements Mocker<Short> {

  @Override
  public Short mock(DataConfig mockConfig) {
    /**
     * 若根据正则模拟
     */
    if(StringUtils.isNotEmpty(mockConfig.numberRegex())){
      return RandomUtils.nextNumberFromRegex(mockConfig.numberRegex()).shortValue();
    }
    return (short) RandomUtils.nextInt(mockConfig.shortRange()[0], mockConfig.shortRange()[1]);
  }

}
