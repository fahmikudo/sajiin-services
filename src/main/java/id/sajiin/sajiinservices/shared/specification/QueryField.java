package id.sajiin.sajiinservices.shared.specification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark fields for dynamic query building
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryField {
    /**
     * Path to the field in the entity. If empty, uses the field name.
     * Supports nested paths like "category.name"
     */
    String path() default "";
    /**
     * Query operator to use for this field
     */
    QueryOperator operator() default QueryOperator.EQUALS;
    /**
     * Whether to ignore case for string comparisons
     */
    boolean ignoreCase() default false;
    /**
     * OR group name. Fields with the same orGroup will be combined with OR logic.
     * If empty, the field is combined with AND logic with other conditions.
     * Example: orGroup = "search" will combine all fields with orGroup="search" using OR
     */
    String orGroup() default "";
}
