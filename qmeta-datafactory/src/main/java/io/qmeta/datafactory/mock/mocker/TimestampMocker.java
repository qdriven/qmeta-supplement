package io.qmeta.datafactory.mock.mocker;

import io.qmeta.datafactory.mock.core.DataConfig;
import io.qmeta.datafactory.mock.core.Mocker;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

/**
 * Timestamp对象模拟器
 */
public class TimestampMocker implements Mocker<Timestamp> {
  private DateMocker dateMocker = new DateMocker();
  @Override
  public Timestamp mock(DataConfig mockConfig) {
     Date date = dateMocker.mock(mockConfig);
     return Timestamp.from(Instant.ofEpochMilli(date.getTime()));
  }
}
