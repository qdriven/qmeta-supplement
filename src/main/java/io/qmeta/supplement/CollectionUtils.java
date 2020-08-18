package io.qmeta.supplement;

import cn.hutool.core.util.ArrayUtil;



public class CollectionUtils extends cn.hutool.core.collection.CollUtil {
    /**
     * 数组中是否包含元素
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @param value 被检查的元素
     * @return 是否包含
     */
    public static <T> boolean contains(T[] array, T value) {
        return ArrayUtil.isNotEmpty(array) && ArrayUtil.contains(array, value);
    }
}
