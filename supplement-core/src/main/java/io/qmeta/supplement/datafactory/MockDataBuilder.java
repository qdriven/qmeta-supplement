package io.qmeta.supplement.datafactory;

import com.github.houbb.data.factory.core.util.DataUtil;

public class MockDataBuilder {

    public <T> T buildData(Class<?> type){
        return (T) DataUtil.build(type);
    }
}
