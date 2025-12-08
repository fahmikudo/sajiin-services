package id.sajiin.sajiinservices.shared.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

/**
 * Generic specification builder that creates JPA Specifications dynamically
 * based on annotated request objects
 *
 * @param <T> Entity type
 */
public class SpecificationBuilder <T> {

    /**
     * Build a JPA Specification from an annotated request object
     *
     * @param request Request object with @QueryField annotations
     * @return JPA Specification
     */
    public Specification<T> buildSpecification(Object request) {
        if (request == null) {
            return Specification.where((root, query, criteriaBuilder) -> null);
        }

        Specification<T> specification = Specification.where((root, query, criteriaBuilder) -> null);

        // Apply query configuration (distinct, joins)
        specification = specification.and(buildQueryConfig(request));

        // Build predicates from annotated fields
        specification = specification.and(buildPredicates(request));

        return specification;
    }

    /**
     * Build query configuration specification (distinct, joins)
     */
    private Specification<T> buildQueryConfig(Object request) {
        QueryConfig config = request.getClass().getAnnotation(QueryConfig.class);
        if (config == null) {
            return Specification.where((root, query, criteriaBuilder) -> null);
        }

        return (root, query, criteriaBuilder) -> {
            // Apply distinct
            if (config.distinct()) {
                query.distinct(true);
            }

            // Apply joins
            for (Join joinConfig : config.joins()) {
                applyJoin(root, query, joinConfig);
            }

            return null;
        };
    }

    /**
     * Apply a join configuration to the root
     */
    private void applyJoin(Root<T> root, CriteriaQuery<?> query, Join joinConfig) {
        if (joinConfig.path().isBlank()) {
            throw new IllegalArgumentException("Join path must not be blank");
        }

        String[] pathParts = joinConfig.path().split("\\.");
        jakarta.persistence.criteria.JoinType jpaJoinType = convertJoinType(joinConfig.type());
        boolean countQuery = isCountQuery(query);

        if (joinConfig.fetch() && !countQuery) {
            // Use fetch join
            FetchParent<?, ?> current = root;

            for (String part : pathParts) {
                current = current.fetch(part, jpaJoinType);
            }
        } else {
            // Use regular join
            From<?, ?> current = root;

            for (String part : pathParts) {
                current = current.join(part, jpaJoinType);
            }
        }
    }

    private jakarta.persistence.criteria.JoinType convertJoinType(JoinType joinType) {
        return switch (joinType) {
            case LEFT -> jakarta.persistence.criteria.JoinType.LEFT;
            case RIGHT -> jakarta.persistence.criteria.JoinType.RIGHT;
            case INNER -> jakarta.persistence.criteria.JoinType.INNER;
        };
    }

    private boolean isCountQuery(CriteriaQuery<?> query) {
        Class<?> resultType = query.getResultType();
        return resultType == Long.class || resultType == long.class;
    }

    /**
     * Build predicates from annotated fields
     */
    private Specification<T> buildPredicates(Object request) {
        List<Specification<T>> andSpecifications = new ArrayList<>();
        Map<String, List<Specification<T>>> orGroups = new HashMap<>();

        for (Field field : getAllFields(request.getClass())) {
            processField(field, request, andSpecifications, orGroups);
        }

        for (List<Specification<T>> orGroupSpecs : orGroups.values()) {
            Specification<T> orSpec = processOrGroup(orGroupSpecs);
            if (orSpec != null) {
                andSpecifications.add(orSpec);
            }
        }

        // Combine all specifications with AND logic
        return combineSpecifications(andSpecifications);
    }

    /**
     * Process a single field and add its specification to appropriate collection
     */
    private void processField(Field field, Object request, List<Specification<T>> andSpecifications,
                              Map<String, List<Specification<T>>> orGroups) {
        QueryField annotation = field.getAnnotation(QueryField.class);
        if (annotation == null) {
            return;
        }

        String orGroupName = annotation.orGroup();
        if (orGroupName == null || orGroupName.isBlank()) {
            OrGroup orGroup = field.getAnnotation(OrGroup.class);
            if (orGroup != null) {
                orGroupName = orGroup.value();
            }
        }

        try {
            if (!field.canAccess(request) && !field.trySetAccessible()) {
                throw new IllegalStateException("Cannot access field: " + field.getName());
            }
            Object value = field.get(request);
            if (value != null) {
                Specification<T> spec = buildPredicate(field, annotation, value);
                if (spec != null) {
                    addSpecificationToGroup(spec, orGroupName, andSpecifications, orGroups);
                }
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to access field: " + field.getName(), e);
        }
    }

    /**
     * Add specification to appropriate group (AND or OR)
     */
    private void addSpecificationToGroup(Specification<T> spec, String orGroupName,
                                         List<Specification<T>> andSpecifications,
                                         Map<String, List<Specification<T>>> orGroups) {
        if (spec == null) {
            return;
        }

        if (orGroupName != null && !orGroupName.isBlank()) {
            orGroups.computeIfAbsent(orGroupName, k -> new ArrayList<>()).add(spec);
        } else {
            andSpecifications.add(spec);
        }
    }

    /**
     * Process OR group and add combined specification to AND specifications
     */
    private Specification<T> processOrGroup(List<Specification<T>> orGroupSpecs) {
        if (orGroupSpecs.isEmpty()) {
            return null;
        }

        return orGroupSpecs.stream()
                .filter(Objects::nonNull)
                .reduce((spec1, spec2) -> (root, query, criteriaBuilder) ->
                        criteriaBuilder.or(spec1.toPredicate(root, query, criteriaBuilder),
                                spec2.toPredicate(root, query, criteriaBuilder)))
                .orElse(null);
    }

    /**
     * Combine all specifications with AND logic
     */
    private Specification<T> combineSpecifications(List<Specification<T>> specifications) {
        Specification<T> emptySpec = (root, query, criteriaBuilder) -> null;
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(emptySpec, Specification::and);
    }

    /**
     * Get all fields including inherited fields
     */
    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(List.of(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * Build a predicate for a single field
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Specification<T> buildPredicate(Field field, QueryField annotation, Object value) {
        String path = annotation.path().isEmpty() ? field.getName() : annotation.path();
        QueryOperator operator = annotation.operator();
        boolean ignoreCase = annotation.ignoreCase();

        return switch (operator) {
            case EQUALS -> buildEqualsSpecification(path, value, ignoreCase);
            case NOT_EQUALS -> buildNotEqualsSpecification(path, value);
            case LIKE -> buildLikeSpecification(path, value, ignoreCase, LikeType.CONTAINS);
            case LIKE_START -> buildLikeSpecification(path, value, ignoreCase, LikeType.STARTS_WITH);
            case LIKE_END -> buildLikeSpecification(path, value, ignoreCase, LikeType.ENDS_WITH);
            case IN -> buildCollectionSpecification(path, value, true);
            case NOT_IN -> buildCollectionSpecification(path, value, false);
            case GREATER_THAN -> buildComparableSpecification(path, value, ComparisonOperator.GREATER_THAN);
            case GREATER_THAN_OR_EQUAL -> buildComparableSpecification(path, value, ComparisonOperator.GREATER_THAN_OR_EQUAL);
            case LESS_THAN -> buildComparableSpecification(path, value, ComparisonOperator.LESS_THAN);
            case LESS_THAN_OR_EQUAL -> buildComparableSpecification(path, value, ComparisonOperator.LESS_THAN_OR_EQUAL);
            case IS_NULL -> (root, query, criteriaBuilder) -> criteriaBuilder.isNull(getPath(root, path));
            case IS_NOT_NULL -> (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(getPath(root, path));
            case BETWEEN -> buildBetweenSpecification(path, value);
            case OR -> throw new IllegalArgumentException(
                    "OR operator is not supported directly. Use orGroup in @QueryField or @OrGroup annotation.");
        };
    }

    private Specification<T> buildEqualsSpecification(String path, Object value, boolean ignoreCase) {
        Object sanitizedValue = sanitizeEqualsValue(value);
        if (sanitizedValue == null) {
            return null;
        }

        return (root, query, criteriaBuilder) -> {
            Expression<?> expression = getPath(root, path);
            boolean canIgnoreCase = ignoreCase && sanitizedValue instanceof String
                    && String.class.isAssignableFrom(expression.getJavaType());
            return buildEqualsPredicate(expression, sanitizedValue, canIgnoreCase, criteriaBuilder);
        };
    }

    private Specification<T> buildNotEqualsSpecification(String path, Object value) {
        Object sanitizedValue = sanitizeEqualsValue(value);
        if (sanitizedValue == null) {
            return null;
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(getPath(root, path), sanitizedValue);
    }

    private Specification<T> buildLikeSpecification(String path, Object value, boolean ignoreCase, LikeType likeType) {
        String strValue = requireStringValue(value, likeType.name());
        if (strValue.isBlank()) {
            return null;
        }
        String trimmedValue = strValue.trim();

        return (root, query, criteriaBuilder) -> {
            Expression<?> expression = getPath(root, path);
            return buildLikePredicate(expression, trimmedValue, ignoreCase, criteriaBuilder, likeType);
        };
    }

    private Specification<T> buildCollectionSpecification(String path, Object value, boolean positive) {
        Collection<?> collection = toCollection(value);
        if (collection.isEmpty()) {
            return null;
        }

        return (root, query, criteriaBuilder) -> {
            Expression<?> expression = getPath(root, path);
            Predicate inPredicate = expression.in(collection);
            return positive ? inPredicate : criteriaBuilder.not(inPredicate);
        };
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Specification<T> buildComparableSpecification(String path, Object value, ComparisonOperator operator) {
        Comparable<?> comparable = requireComparable(value, operator.name());

        return (root, query, criteriaBuilder) -> {
            Expression<Comparable> expression = getPath(root, path).as(Comparable.class);
            Expression<Comparable> literal = criteriaBuilder.literal((Comparable) comparable);
            return switch (operator) {
                case GREATER_THAN -> criteriaBuilder.greaterThan(expression, literal);
                case GREATER_THAN_OR_EQUAL -> criteriaBuilder.greaterThanOrEqualTo(expression, literal);
                case LESS_THAN -> criteriaBuilder.lessThan(expression, literal);
                case LESS_THAN_OR_EQUAL -> criteriaBuilder.lessThanOrEqualTo(expression, literal);
            };
        };
    }

    private Specification<T> buildBetweenSpecification(String path, Object value) {
        if (!(value instanceof List<?> list) || list.size() != 2) {
            throw new IllegalArgumentException("BETWEEN operator requires a List with exactly 2 elements");
        }

        Object start = list.get(0);
        Object end = list.get(1);
        if (!(start instanceof Comparable<?> startComparable) || !(end instanceof Comparable<?> endComparable)) {
            throw new IllegalArgumentException("BETWEEN boundaries must be Comparable");
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.between(
                getPath(root, path).as(Comparable.class),
                (Comparable) startComparable,
                (Comparable) endComparable
        );
    }

    private Object sanitizeEqualsValue(Object value) {
        if (value instanceof String stringValue) {
            String trimmed = stringValue.trim();
            return trimmed.isEmpty() ? null : trimmed;
        }
        return value;
    }

    private String requireStringValue(Object value, String operatorName) {
        if (value instanceof String str) {
            return str;
        }
        throw new IllegalArgumentException(operatorName + " operator requires a String value");
    }

    private Comparable<?> requireComparable(Object value, String operatorName) {
        if (value instanceof Comparable<?> comparable) {
            return comparable;
        }
        throw new IllegalArgumentException(operatorName + " operator requires a Comparable value");
    }

    private Collection<?> toCollection(Object value) {
        if (value instanceof Collection<?> collection) {
            return collection;
        }
        if (value != null && value.getClass().isArray()) {
            int length = Array.getLength(value);
            List<Object> list = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                list.add(Array.get(value, i));
            }
            return list;
        }
        throw new IllegalArgumentException("IN operators require a Collection or array value");
    }

    private enum ComparisonOperator {
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN,
        LESS_THAN_OR_EQUAL
    }

    /**
     * Build equals predicate with optional case insensitivity
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Predicate buildEqualsPredicate(Expression<?> expression, Object value, boolean ignoreCase, CriteriaBuilder criteriaBuilder) {
        if (ignoreCase && value instanceof String strValue) {
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(expression.as(String.class)),
                    strValue.toLowerCase()
            );
        }
        return criteriaBuilder.equal(expression, value);
    }

    /**
     * Enum for LIKE pattern types
     */
    private enum LikeType {
        CONTAINS, STARTS_WITH, ENDS_WITH
    }

    /**
     * Build LIKE predicate with optional case insensitivity
     */
    private Predicate buildLikePredicate(Expression<?> expression, Object value, boolean ignoreCase,
                                        CriteriaBuilder criteriaBuilder, LikeType likeType) {
        String strValue = (String) value;
        String pattern = switch (likeType) {
            case CONTAINS -> "%" + strValue + "%";
            case STARTS_WITH -> strValue + "%";
            case ENDS_WITH -> "%" + strValue;
        };

        if (ignoreCase) {
            return criteriaBuilder.like(
                    criteriaBuilder.lower(expression.as(String.class)),
                    pattern.toLowerCase()
            );
        }
        return criteriaBuilder.like(expression.as(String.class), pattern);
    }

    /**
     * Build BETWEEN predicate
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Predicate buildBetweenPredicate(Expression<?> expression, Object value, CriteriaBuilder criteriaBuilder) {
        if (value instanceof List<?> list && list.size() == 2) {
            return criteriaBuilder.between(
                    expression.as(Comparable.class),
                    (Comparable) list.get(0),
                    (Comparable) list.get(1)
            );
        }
        throw new IllegalArgumentException("BETWEEN operator requires a List with 2 elements");
    }

    /**
     * Get a path expression, supporting nested paths like "category.name"
     */
    private Expression<?> getPath(Root<T> root, String path) {
        if (path.isBlank()) {
            throw new IllegalArgumentException("Path must not be blank");
        }

        String[] parts = path.split("\\.");
        Path<?> currentPath = root;

        for (String part : parts) {
            try {
                currentPath = currentPath.get(part);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid path segment '" + part + "' in path '" + path + "'", ex);
            }
        }

        return currentPath;
    }

}
