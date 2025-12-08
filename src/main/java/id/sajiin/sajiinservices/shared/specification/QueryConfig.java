package id.sajiin.sajiinservices.shared.specification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to configure query behavior at the class level
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryConfig {

    /**
     * Whether to apply DISTINCT to the query
     */
    boolean distinct() default false;

    /**
     * Fields to join (fetch) in the query
     * Use dot notation for nested joins: "category.parent"
     */
    Join[] joins() default {};

}
