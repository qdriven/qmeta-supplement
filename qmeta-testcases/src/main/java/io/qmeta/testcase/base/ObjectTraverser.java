package io.qmeta.testcase.base;

import java.util.List;
import java.util.Map;


public abstract class ObjectTraverser<T> {

    private T object;

    public ObjectTraverser(T object) {
        this.object = object;
    }

    public abstract boolean visit(Object object);

    public T transve() {
        return transve(object);
    }

    private T transve(Object target) {
        if (null == target || !visit(target)) {
            return object;
        }

        if (target instanceof Map) {
            Map<?, ?> map = (Map) target;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                transve(entry.getValue());
            }
        } else if (target instanceof List) {
            List list = (List) target;
            for (Object value : list) {
                transve(value);
            }
        }
        return object;
    }
}
