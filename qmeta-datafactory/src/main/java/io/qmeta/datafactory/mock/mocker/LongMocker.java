package io.qmeta.datafactory.mock.mocker;

import io.qmeta.datafactory.mock.core.DataConfig;
import io.qmeta.datafactory.mock.core.Mocker;
import io.qmeta.datafactory.mock.util.RandomUtils;
import io.qmeta.supplement.StringUtils;

/**
 * 模拟Long对象
 */
public class LongMocker implements Mocker<Long> {

  @Override
  public Long mock(DataConfig mockConfig) {
    /**
     * 若根据正则模拟
     */
    if(StringUtils.isNotEmpty(mockConfig.numberRegex())){
      return RandomUtils.nextNumberFromRegex(mockConfig.numberRegex()).longValue();
    }
    return RandomUtils.nextLong(mockConfig.longRange()[0], mockConfig.longRange()[1]);
  }

}
