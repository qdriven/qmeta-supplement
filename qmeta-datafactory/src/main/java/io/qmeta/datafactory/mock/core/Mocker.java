package io.qmeta.datafactory.mock.core;

/**
 * 模拟器接口
 */
public interface Mocker<T> {


  T mock(DataConfig mockConfig);

}
