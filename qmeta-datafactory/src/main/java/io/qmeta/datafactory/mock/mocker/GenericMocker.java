package io.qmeta.datafactory.mock.mocker;

import io.qmeta.datafactory.mock.core.DataConfig;
import io.qmeta.datafactory.mock.core.Mocker;
import java.lang.reflect.ParameterizedType;

/**
 * 模拟泛型
 */
public class GenericMocker implements Mocker<Object> {

  private ParameterizedType type;

  GenericMocker(ParameterizedType type) {
    this.type = type;
  }

  @Override
  public Object mock(DataConfig mockConfig) {
    return new BaseMocker(type.getRawType(), type.getActualTypeArguments()).mock(mockConfig);
  }

}
