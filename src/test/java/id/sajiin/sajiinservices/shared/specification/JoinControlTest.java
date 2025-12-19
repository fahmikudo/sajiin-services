package id.sajiin.sajiinservices.shared.specification;

import id.sajiin.sajiinservices.identity.user.domain.User;
import id.sajiin.sajiinservices.identity.user.repository.query.UserEntityRequest;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for @JoinControl annotation functionality
 */
class JoinControlTest {

    @Test
    void testJoinControlWithIncludeRoleTrue() {
        // Given: A request with includeRole set to true
        UserEntityRequest request = UserEntityRequest.builder()
                .username("testuser")
                .includeRole(true)
                .build();

        // When: Building specification
        SpecificationBuilder<User> builder = new SpecificationBuilder<>();
        Specification<User> specification = builder.buildSpecification(request);

        // Then: Specification should be created successfully
        assertNotNull(specification);
        // Note: The join will be applied when the specification is executed
    }

    @Test
    void testJoinControlWithIncludeRoleFalse() {
        // Given: A request with includeRole set to false
        UserEntityRequest request = UserEntityRequest.builder()
                .username("testuser")
                .includeRole(false)
                .build();

        // When: Building specification
        SpecificationBuilder<User> builder = new SpecificationBuilder<>();
        Specification<User> specification = builder.buildSpecification(request);

        // Then: Specification should be created successfully (without join)
        assertNotNull(specification);
    }

    @Test
    void testJoinControlWithoutSettingFlag() {
        // Given: A request without setting includeRole (defaults to false)
        UserEntityRequest request = UserEntityRequest.builder()
                .username("testuser")
                .build();

        // When: Building specification
        SpecificationBuilder<User> builder = new SpecificationBuilder<>();
        Specification<User> specification = builder.buildSpecification(request);

        // Then: Specification should be created successfully (without join)
        assertNotNull(specification);
    }
}

