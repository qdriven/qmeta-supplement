package io.qmeta.supplement.constants;

public interface QMetaConstants {
  /** 项目名称常量 */
  String PROJECT_NAME = "QMETA";
  /** 注解 @Secure AOP顺序 */
  int AOP_ORDER_SECURE = 0;

  /** 注解 @Log AOP顺序 */
  int AOP_ORDER_LOG = AOP_ORDER_SECURE + 1;

  /** web 请求 AOP 顺序 */
  int AOP_ORDER_REQUEST_LOG = AOP_ORDER_SECURE - 1;
}
