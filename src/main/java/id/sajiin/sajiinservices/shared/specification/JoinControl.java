package id.sajiin.sajiinservices.shared.specification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to control dynamic joins based on boolean field values.
 * When applied to a boolean field, the join will only be applied if the field value is true.
 *
 * Example:
 * <pre>
 * {@code
 * @JoinControl(path = "role", type = JoinType.LEFT, fetch = false)
 * private boolean includeRole;
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinControl {
    /**
     * The path to join (e.g., "role" or "role.permissions")
     */
    String path();

    /**
     * The type of join to perform
     */
    JoinType type() default JoinType.LEFT;

    /**
     * Whether to use fetch join (eager loading)
     */
    boolean fetch() default false;
}

