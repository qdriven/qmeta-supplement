package io.qmeta.supplement;

import cn.hutool.core.map.MapUtil;
import java.util.Map;

public class MapUtils extends MapUtil {
  public static Object getProperty(Map<?, ?> map, Object qualifiedKey) {
    if (map != null && !map.isEmpty() && qualifiedKey != null) {
      String input = String.valueOf(qualifiedKey);
      if (!"".equals(input)) {
        if (input.contains(".")) {
          int index = input.indexOf(".");
          String left = input.substring(0, index);
          String right = input.substring(index + 1, input.length());
          return getProperty((Map<?, ?>) map.get(left), right);
        } else if (map.containsKey(input)) {
          return map.get(input);
        } else {
          return null;
        }
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public static void setProperty(Map<?, ?> map, Object qualifiedKey, Object value) {
    if (map != null && !map.isEmpty() && qualifiedKey != null) {
      String input = String.valueOf(qualifiedKey);
      if (!input.equals("")) {
        if (input.contains(".")) {
          int index = input.indexOf(".");
          String left = input.substring(0, index);
          String right = input.substring(index + 1, input.length());
          setProperty((Map<?, ?>) map.get(left), right, value);
        } else {
          ((Map<Object, Object>) map).put(qualifiedKey, value);
        }
      }
    }
  }
}
