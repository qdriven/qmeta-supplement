package io.qmeta.datafactory.mock.mocker;


import io.qmeta.datafactory.mock.core.DataConfig;
import io.qmeta.datafactory.mock.core.Mocker;
import io.qmeta.datafactory.mock.util.RandomUtils;
import io.qmeta.supplement.StringUtils;

/**
 * Integer对象模拟器
 */
public class IntegerMocker implements Mocker<Integer> {

  @Override
  public Integer mock(DataConfig mockConfig) {
    /**
     * 若根据正则模拟
     */
    if(StringUtils.isNotEmpty(mockConfig.numberRegex())){
      return RandomUtils.nextNumberFromRegex(mockConfig.numberRegex()).intValue();
    }
    return RandomUtils.nextInt(mockConfig.intRange()[0], mockConfig.intRange()[1]);
  }

}
