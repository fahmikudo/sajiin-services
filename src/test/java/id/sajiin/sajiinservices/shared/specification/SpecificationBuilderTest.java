package id.sajiin.sajiinservices.shared.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.FetchParent;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SpecificationBuilderTest {

    private final SpecificationBuilder<DummyEntity> builder = new SpecificationBuilder<>();

    @Test
    void buildSpecificationWithNullRequestReturnsEmptyPredicate() {
        var specification = builder.buildSpecification(null);

        Root<DummyEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);

        assertNull(specification.toPredicate(root, query, criteriaBuilder));
    }

    @Test
    void likeOperatorSkipsBlankValues() {
        LikeRequest request = new LikeRequest();
        request.keyword = "   ";

        var specification = builder.buildSpecification(request);
        assertNull(specification.toPredicate(mock(Root.class), mock(CriteriaQuery.class), mock(CriteriaBuilder.class)));
    }

    @Test
    void inOperatorAcceptsArrays() {
        InArrayRequest request = new InArrayRequest();
        request.statuses = new String[]{"ACTIVE", "INACTIVE"};

        var specification = builder.buildSpecification(request);

        @SuppressWarnings("unchecked")
        Root<DummyEntity> root = mock(Root.class);
        @SuppressWarnings("unchecked")
        Path<Object> path = mock(Path.class);
        when(root.get("status")).thenReturn(path);

        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        Predicate predicate = mock(Predicate.class);
        when(path.in(Mockito.<Collection<?>>any())).thenReturn(predicate);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertSame(predicate, result);
        verify(path).in(argThat((Collection<?> collection) ->
                collection.size() == 2 && collection.contains("ACTIVE")));
    }

    @Test
    void inOperatorSkipsEmptyCollections() {
        InCollectionRequest request = new InCollectionRequest();
        request.statuses = Collections.emptyList();

        var specification = builder.buildSpecification(request);
        assertNull(specification.toPredicate(mock(Root.class), mock(CriteriaQuery.class), mock(CriteriaBuilder.class)));
    }

    @Test
    void betweenOperatorRejectsInvalidShape() {
        BetweenRequest request = new BetweenRequest();
        request.range = List.of(1);

        assertThrows(IllegalArgumentException.class, () -> builder.buildSpecification(request));
    }

    @Test
    void comparableOperatorsRequireComparableValues() {
        ComparisonRequest request = new ComparisonRequest();
        request.threshold = new Object();

        assertThrows(IllegalArgumentException.class, () -> builder.buildSpecification(request));
    }

    @Test
    void equalsIgnoreCaseOnlyWhenPathIsString() {
        EqualsRequest request = new EqualsRequest();
        request.name = "Alice";

        var specification = builder.buildSpecification(request);

        @SuppressWarnings("unchecked")
        Root<DummyEntity> root = mock(Root.class);
        @SuppressWarnings("unchecked")
        Path<Object> path = mock(Path.class);
        when(root.get("name")).thenReturn(path);
        when(path.getJavaType()).thenAnswer(invocation -> Integer.class);

        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        Predicate predicate = mock(Predicate.class);
        when(criteriaBuilder.equal(path, "Alice")).thenReturn(predicate);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertSame(predicate, result);
        verify(criteriaBuilder, never()).lower(any());
    }

    @Test
    void orGroupCombinesPredicates() {
        OrGroupRequest request = new OrGroupRequest();
        request.name = "Alice";
        request.description = "data";

        var specification = builder.buildSpecification(request);

        @SuppressWarnings("unchecked")
        Root<DummyEntity> root = mock(Root.class);
        @SuppressWarnings("unchecked")
        Path<Object> namePath = mock(Path.class);
        @SuppressWarnings("unchecked")
        Path<Object> descriptionPath = mock(Path.class);
        when(root.get("name")).thenReturn(namePath);
        when(root.get("description")).thenReturn(descriptionPath);
        when(namePath.getJavaType()).thenAnswer(invocation -> String.class);
        Expression<String> descriptionExpression = mock(Expression.class);
        when(descriptionPath.as(String.class)).thenReturn(descriptionExpression);

        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        Predicate equalsPredicate = mock(Predicate.class);
        Predicate likePredicate = mock(Predicate.class);
        Predicate orPredicate = mock(Predicate.class);
        when(criteriaBuilder.equal(namePath, "Alice")).thenReturn(equalsPredicate);
        when(criteriaBuilder.like(descriptionExpression, "%data%"))
                .thenReturn(likePredicate);
        when(criteriaBuilder.or(equalsPredicate, likePredicate)).thenReturn(orPredicate);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertSame(orPredicate, result);
        verify(criteriaBuilder).or(equalsPredicate, likePredicate);
    }

    @Test
    void fetchJoinAppliedOnlyForNonCountQueries() {
        var specification = builder.buildSpecification(new JoinOnlyRequest());

        Root<DummyEntity> root = mock(Root.class);
        CriteriaQuery<DummyEntity> query = mock(CriteriaQuery.class);
        when(query.getResultType()).thenAnswer(invocation -> DummyEntity.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);

        @SuppressWarnings("unchecked")
        Fetch<Object, Object> fetch = mock(Fetch.class);
        when(root.fetch(eq("details"), eq(jakarta.persistence.criteria.JoinType.LEFT))).thenReturn(fetch);

        specification.toPredicate(root, query, criteriaBuilder);

        verify(root).fetch("details", jakarta.persistence.criteria.JoinType.LEFT);
        verify(query).distinct(true);
    }

    @Test
    void fetchJoinFallsBackToRegularJoinForCountQueries() {
        var specification = builder.buildSpecification(new JoinOnlyRequest());

        Root<DummyEntity> root = mock(Root.class);
        CriteriaQuery<Long> query = mock(CriteriaQuery.class);
        when(query.getResultType()).thenAnswer(invocation -> Long.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);

        @SuppressWarnings("unchecked")
        jakarta.persistence.criteria.Join<Object, Object> join = mock(jakarta.persistence.criteria.Join.class);
        when(root.join(eq("details"), eq(jakarta.persistence.criteria.JoinType.LEFT))).thenReturn(join);

        specification.toPredicate(root, query, criteriaBuilder);

        verify(root, never()).fetch(anyString(), any());
        verify(root).join("details", jakarta.persistence.criteria.JoinType.LEFT);
        verify(query, never()).distinct(true);
    }

    private static class DummyEntity {}

    private static class LikeRequest {
        @QueryField(path = "name", operator = QueryOperator.LIKE)
        private String keyword;
    }

    private static class InArrayRequest {
        @QueryField(path = "status", operator = QueryOperator.IN)
        private String[] statuses;
    }

    private static class InCollectionRequest {
        @QueryField(path = "status", operator = QueryOperator.IN)
        private Collection<String> statuses = new ArrayList<>();
    }

    private static class BetweenRequest {
        @QueryField(path = "value", operator = QueryOperator.BETWEEN)
        private List<Integer> range;
    }

    private static class ComparisonRequest {
        @QueryField(path = "value", operator = QueryOperator.GREATER_THAN)
        private Object threshold;
    }

    private static class EqualsRequest {
        @QueryField(path = "name", ignoreCase = true)
        private String name;
    }

    private static class OrGroupRequest {
        @QueryField(path = "name")
        @OrGroup("search")
        private String name;

        @QueryField(path = "description", operator = QueryOperator.LIKE)
        @OrGroup("search")
        private String description;
    }

    @QueryConfig(joins = {
            @Join(path = "details", fetch = true, type = JoinType.LEFT)
    })
    private static class JoinOnlyRequest {
    }
}
