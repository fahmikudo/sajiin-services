package id.sajiin.sajiinservices.shared.specification;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to define a join configuration
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Join {

    /**
     * Path to join (e.g., "category", "category.parent")
     */
    String path();

    /**
     * Type of join
     */
    JoinType type() default JoinType.LEFT;

    /**
     * Whether to fetch the joined entity
     */
    boolean fetch() default true;

}
