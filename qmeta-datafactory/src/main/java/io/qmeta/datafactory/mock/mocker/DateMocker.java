package io.qmeta.datafactory.mock.mocker;

import io.qmeta.datafactory.mock.core.DataConfig;
import io.qmeta.datafactory.mock.core.MockException;
import io.qmeta.datafactory.mock.core.Mocker;
import io.qmeta.datafactory.mock.util.DateUtils;
import io.qmeta.datafactory.mock.util.RandomUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Date对象模拟器
 */
public class DateMocker implements Mocker<Date> {
  @Override
  public Date mock(DataConfig mockConfig) {
    try {
      long startTime = DateUtils.getString2DateAuto(mockConfig.dateRange()[0]).getTime();
      long endTime = DateUtils.getString2DateAuto(mockConfig.dateRange()[1]).getTime();
      return new Date(RandomUtils.nextLong(startTime,endTime));
    } catch (ParseException e) {
      throw new MockException("不支持的日期格式，或者使用了错误的日期", e);
    }
  }

}
