package io.qmeta.testng.util;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * map 工具类
 */
@SuppressWarnings("unchecked")
public final class MapsHelper {
    private static final Logger logger = LogManager.getLogger(MapsHelper.class.getName());

    private MapsHelper() {
    }

    /**
     * put key/value into map
     *
     * @param source
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     */
    public static <K, V> void put(Map<K, List<V>> source, K key, V value) {
        if (source.get(key) == null) {
            source.put(key, Lists.newArrayList(value));
        } else {
            source.get(key).add(value);
        }
    }

    /**
     * 讲Java 类转化为Map
     *
     * @param instance
     * @param <T>
     * @return
     */
    public static <T> Map instanceToMap(T instance) {

        try {
            return BeanUtils.describe(instance);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.warn("ignored_error={}", e);
        }

        return Maps.newConcurrentMap();
    }

    /**
     * 根据多个key值获取此key值对应的Entry, 返回是个Map
     *
     * @param sources 源Map
     * @param keys
     * @return Map with the give keys
     */
    public static <T> Map<String, T> getMultiple(Map<String, T> sources, String... keys) {
        if (keys.length == 0) return sources;
        Map<String, T> result = Maps.newHashMap();
        for (String key : keys) {
            result.put(key, sources.get(key));
        }
        return result;
    }

    /**
     * 根据多个key值获取此key值对应的Entry, 返回是个Map
     *
     * @param sources
     * @param keys
     * @return Map with the give keys
     */
    public static <T> Map<String, T> getMultiple(Map<String, T> sources
            , List<String> keys) {
        if (keys.size() == 0) return sources;
        Map<String, T> result = Maps.newHashMap();
        for (String key : keys) {
            result.put(key, sources.get(key));
        }
        return result;
    }

    /**
     * 根据多个key值已经转换函数获取此key值对应的Entry, 返回是个Map
     *
     * @param sources
     * @param keys
     * @return Map with the give keys
     */
    public static <T> Map<String, T> getMultiple(Map<String, T> sources
            , List<String> keys, Function<Object, T> function) {
        Map<String, T> result = Maps.newHashMap();
        for (String key : keys) {
            T value = function.apply(sources.get(key));
            result.put(key, value);
        }
        return result;
    }

    /**
     * @param source:       原始Map
     * @param key:          获取值的key
     * @param errorMessage, 如果获取是Null时,抛出的异常
     * @param <K>
     * @param <V>
     * @return V
     */
    public static <K, V> V getNotNullObject(Map<K, V> source, K key, String errorMessage) {
        V result = source.get(key);
        if (result == null) {
            throw new RuntimeException(errorMessage);
        }

        return result;
    }

}
