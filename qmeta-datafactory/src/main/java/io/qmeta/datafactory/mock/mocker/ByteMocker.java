package io.qmeta.datafactory.mock.mocker;


import io.qmeta.datafactory.mock.core.DataConfig;
import io.qmeta.datafactory.mock.core.Mocker;
import io.qmeta.datafactory.mock.util.RandomUtils;
import io.qmeta.supplement.StringUtils;

/**
 * Byte对象模拟器
 */
public class ByteMocker implements Mocker<Byte> {

    @Override
    public Byte mock(DataConfig mockConfig) {
        /**
         * 若根据正则模拟
         */
        if (StringUtils.isNotEmpty(mockConfig.numberRegex())) {
            return RandomUtils.nextNumberFromRegex(mockConfig.numberRegex()).byteValue();
        }
        return (byte) RandomUtils.nextInt(mockConfig.byteRange()[0], mockConfig.byteRange()[1]);
    }

}
