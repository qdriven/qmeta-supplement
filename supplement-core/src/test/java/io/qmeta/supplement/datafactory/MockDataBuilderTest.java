package io.qmeta.supplement.datafactory;

import org.junit.jupiter.api.Test;

class MockDataBuilderTest {

    @Test
    void buildData() {
        MockDataBuilder builder = new MockDataBuilder();
        MockUser user = builder.buildData(MockUser.class);
        System.out.println(user);
    }
}