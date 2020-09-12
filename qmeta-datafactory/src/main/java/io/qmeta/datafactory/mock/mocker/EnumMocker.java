package io.qmeta.datafactory.mock.mocker;

import cn.hutool.core.util.StrUtil;
import io.qmeta.datafactory.mock.core.DataConfig;
import io.qmeta.datafactory.mock.core.MockException;
import io.qmeta.datafactory.mock.core.Mocker;
import io.qmeta.datafactory.mock.util.RandomUtils;
import java.lang.reflect.Field;

/**
 * Enum对象模拟器
 */
public class EnumMocker<T extends Enum> implements Mocker<Object> {

  private Class<?> clazz;

  public EnumMocker(Class<?> clazz) {
    this.clazz = clazz;
  }

  @Override
  public T mock(DataConfig mockConfig) {

    Enum[] enums = mockConfig.globalConfig().getcacheEnum(clazz.getName());
    if (enums == null) {
      //  Field field = clazz.getDeclaredField("$VALUES");
       // field.setAccessible(true);
        enums =(Enum[]) clazz.getEnumConstants();
        if (enums.length == 0) {
          throw new MockException("空的enum不能模拟");
        }
        mockConfig.globalConfig().cacheEnum(clazz.getName(), enums);
    }
    return (T) enums[RandomUtils.nextInt(0, enums.length)];
  }

}
