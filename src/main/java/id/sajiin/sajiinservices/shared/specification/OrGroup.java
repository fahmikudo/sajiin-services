package id.sajiin.sajiinservices.shared.specification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to group multiple fields with OR logic
 * Fields in the same OR group will be combined with OR instead of AND
 *
 * Example:
 * @QueryField(orGroup = "search")
 * private String name;
 *
 * @QueryField(orGroup = "search", operator = QueryOperator.LIKE)
 * private String description;
 *
 * This generates: (name = ? OR description LIKE ?)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrGroup {
    /**
     * The name of the OR group. Fields with the same group name will be combined with OR logic.
     */
    String value();
}
