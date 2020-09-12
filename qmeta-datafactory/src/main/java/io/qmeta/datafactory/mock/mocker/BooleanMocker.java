package io.qmeta.datafactory.mock.mocker;


import io.qmeta.datafactory.mock.core.DataConfig;
import io.qmeta.datafactory.mock.core.Mocker;
import io.qmeta.datafactory.mock.util.RandomUtils;

/**
 * Boolean对象模拟器
 */
public class BooleanMocker implements Mocker<Boolean> {

  @Override
  public Boolean mock(DataConfig mockConfig) {
    boolean[] booleanSeed = mockConfig.booleanSeed();
    return booleanSeed[RandomUtils.nextInt(0, booleanSeed.length)];
  }

}
