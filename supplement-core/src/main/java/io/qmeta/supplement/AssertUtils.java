package io.qmeta.supplement;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import javax.validation.Validation;
import javax.validation.Validator;

/** 断言工具类 */
public class AssertUtils {
  private static Validator validator;

  static {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  //	/**
  //	 * 自定义校验
  //	 * {@code true}，触发异常
  //	 * {@code false}，无操作
  //	 *
  //	 * @param predicate  自定义函数
  //	 * @param resultCode 结果状态码
  //	 */
  //	public static <T> void validate(T t, Predicate<T> predicate, IResultCode resultCode) {
  //		if (predicate.test(t)) {
  //			ExceptionUtils.exception(resultCode);
  //		}
  //	}
  //
  //	/**
  //	 * 校验对象
  //	 *
  //	 * @param object 待校验对象
  //	 * @param groups 待校验的组
  //	 */
  //	public static void validateEntity(Object object, Class<?>... groups) {
  //		validateEntity(object, CommonResultCode.PARAM_VALID_ERROR, groups);
  //	}
  //
  //	/**
  //	 * 校验对象
  //	 *
  //	 * @param object     待校验对象
  //	 * @param resultCode 结果状态码
  //	 * @param groups     待校验的组
  //	 */
  //	public static void validateEntity(Object object, IResultCode resultCode, Class<?>... groups) {
  //		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
  //		if (!constraintViolations.isEmpty()) {
  //			StringBuilder msg = new StringBuilder();
  //			for (ConstraintViolation<Object> constraint : constraintViolations) {
  //				msg.append(constraint.getMessage()).append("<br>");
  //			}
  //			ExceptionUtils.exception(resultCode, msg.toString());
  //		}
  //	}
  //
  //	/**
  //	 * 数值是否在范围内
  //	 *
  //	 * @param number     待校验数值
  //	 * @param start      起始值
  //	 * @param end        终止值
  //	 * @param resultCode 结果状态码
  //	 */
  //	public static void isNotBetween(Number number, Number start, Number end, IResultCode
  // resultCode) {
  //		if (NumberUtil.sub(number, start).compareTo(BigDecimal.ZERO) < 0 || NumberUtil.sub(number,
  // end).compareTo(BigDecimal.ZERO) > 0) {
  //			ExceptionUtils.exception(resultCode);
  //		}
  //	}

  /**
   * 数值是否在范围内
   *
   * @param number 待校验数值
   * @param start 起始值
   * @param end 终止值
   * @param message 异常消息
   */
  public static void isNotBetween(Number number, Number start, Number end, String message) {
    if (NumberUtil.sub(number, start).compareTo(BigDecimal.ZERO) < 0
        || NumberUtil.sub(number, end).compareTo(BigDecimal.ZERO) > 0) {
      ExceptionUtils.exception(message);
    }
  }

  //	/**
  //	 * 字符串是否为空
  //	 *
  //	 * @param str        待校验字符串
  //	 * @param resultCode 结果状态码
  //	 */
  //	public static void isBlank(String str, IResultCode resultCode) {
  //		if (StrUtil.isBlank(str)) {
  //			ExceptionUtils.exception(resultCode);
  //		}
  //	}

  /**
   * 字符串是否为空
   *
   * @param str 待校验字符串
   * @param message 异常消息
   */
  public static void isBlank(String str, String message) {
    if (StringUtils.isBlank(str)) {
      ExceptionUtils.exception(message);
    }
  }
  //
  //	/**
  //	 * 对象是否为空
  //	 *
  //	 * @param object     待校验对象
  //	 * @param resultCode 结果状态码
  //	 */
  //	public static void isNull(Object object, IResultCode resultCode) {
  //		if (ObjectUtil.isNull(object)) {
  //			ExceptionUtils.exception(resultCode);
  //		}
  //	}
  //
  /**
   * 对象是否为空
   *
   * @param object 待校验对象
   * @param message 异常消息
   */
  public static void isNull(Object object, String message) {
    if (ObjectUtil.isNull(object)) {
      ExceptionUtils.exception(message);
    }
  }
  //
  //	/**
  //	 * 集合是否为空
  //	 *
  //	 * @param collection 待校验对象
  //	 * @param resultCode 结果状态码
  //	 */
  //	public static void isEmpty(Collection<?> collection, IResultCode resultCode) {
  //		if (CollUtil.isEmpty(collection)) {
  //			ExceptionUtils.exception(resultCode);
  //		}
  //	}
  //
  //	/**
  //	 * 集合是否为空
  //	 *
  //	 * @param collection 待校验对象
  //	 * @param message    异常消息
  //	 */
  //	public static void isEmpty(Collection<?> collection, String message) {
  //		if (CollUtil.isEmpty(collection)) {
  //			ExceptionUtils.exception(message);
  //		}
  //	}
  //
  //	/**
  //	 * map是否为空
  //	 *
  //	 * @param map        待校验对象
  //	 * @param resultCode 结果状态码
  //	 */
  //	public static void isEmpty(Map<?, ?> map, IResultCode resultCode) {
  //		if (MapUtil.isEmpty(map)) {
  //			ExceptionUtils.exception(resultCode);
  //		}
  //	}

  /**
   * map是否为空
   *
   * @param map 待校验对象
   * @param message 异常消息
   */
  public static void isEmpty(Map<?, ?> map, String message) {
    if (MapUtil.isEmpty(map)) {
      ExceptionUtils.exception(message);
    }
  }

  //	/**
  //	 * 集合是否不为空
  //	 *
  //	 * @param collection 待校验对象
  //	 * @param resultCode 结果状态码
  //	 */
  //	public static void isNotEmpty(Collection<?> collection, IResultCode resultCode) {
  //		if (CollUtil.isNotEmpty(collection)) {
  //			ExceptionUtils.exception(resultCode);
  //		}
  //	}

  /**
   * 集合是否不为空
   *
   * @param collection 待校验对象
   * @param message 异常消息
   */
  public static void isNotEmpty(Collection<?> collection, String message) {
    if (CollUtil.isNotEmpty(collection)) {
      ExceptionUtils.exception(message);
    }
  }
}
