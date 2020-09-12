package io.qmeta.datafactory.mock.mocker;

import io.qmeta.datafactory.mock.core.DataConfig;
import io.qmeta.datafactory.mock.core.Mocker;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * LocalDateTime对象模拟器
 */
public class LocalDateTimeMocker implements Mocker<LocalDateTime> {
  private DateMocker dateMocker = new DateMocker();
  @Override
  public LocalDateTime mock(DataConfig mockConfig) {
     Date date = dateMocker.mock(mockConfig);
     return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
  }
}
