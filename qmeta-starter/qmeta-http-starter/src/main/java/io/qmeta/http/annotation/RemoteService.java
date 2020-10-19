package io.qmeta.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * indicate Remote Service
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RemoteService {
    /**
     * Service Bean Name
     * @return
     */
    String name() default "";

    /**
     * service name
     * @return
     */
    String value() default "default";

    /**
     * service name of remote service
     * @return
     */
    String serviceName() default "";
}
