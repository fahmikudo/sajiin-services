# @JoinControl Annotation - Usage Guide

## Overview
The `@JoinControl` annotation provides dynamic, programmatic control over JPA joins in your entity request classes. Unlike `@QueryConfig` which applies joins statically, `@JoinControl` allows you to enable/disable joins at runtime based on boolean field values.

## When to Use

### Use `@JoinControl` when:
- Joins are optional and not needed in all queries
- You want to optimize performance by avoiding unnecessary joins
- Different use cases require different join configurations
- You want runtime control over query complexity

### Use `@QueryConfig` when:
- Joins are ALWAYS required for the entity
- You need consistent join behavior across all queries
- The relationship is core to the entity's functionality

## Basic Usage

### 1. Define the Control Field with @JoinControl

```java
@Getter
@Setter
@Builder
public class UserEntityRequest extends BaseEntityRequest {

    @QueryField
    private String username;

    @JoinControl(path = "role", type = JoinType.LEFT, fetch = false)
    private boolean includeRole;
}
```

### 2. Use in Your Service/Controller

```java
// WITHOUT role join (better performance when role is not needed)
UserEntityRequest request = UserEntityRequest.builder()
    .username("john.doe")
    .includeRole(false)  // or omit it (defaults to false)
    .build();

// WITH role join (when you need role data)
UserEntityRequest request = UserEntityRequest.builder()
    .username("john.doe")
    .includeRole(true)  // Join will be applied
    .build();
```

## Annotation Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `path` | String | (required) | The entity relationship path to join (e.g., "role", "role.permissions") |
| `type` | JoinType | LEFT | The type of join: LEFT, RIGHT, or INNER |
| `fetch` | boolean | false | Whether to use fetch join (eager loading) |

## Multiple Joins Example

```java
@Getter
@Setter
@Builder
public class UserEntityRequest extends BaseEntityRequest {

    @QueryField
    private String username;

    @JoinControl(path = "role", type = JoinType.LEFT, fetch = false)
    private boolean includeRole;

    @JoinControl(path = "permissions", type = JoinType.LEFT, fetch = true)
    private boolean includePermissions;

    @JoinControl(path = "department", type = JoinType.INNER, fetch = false)
    private boolean includeDepartment;
}
```

Usage:
```java
// Only join role
var request = UserEntityRequest.builder()
    .username("john")
    .includeRole(true)
    .includePermissions(false)
    .includeDepartment(false)
    .build();

// Join role AND permissions
var request = UserEntityRequest.builder()
    .username("john")
    .includeRole(true)
    .includePermissions(true)
    .includeDepartment(false)
    .build();
```

## Nested Path Joins

```java
@JoinControl(path = "role.permissions", type = JoinType.LEFT, fetch = true)
private boolean includeRolePermissions;
```

This will join through multiple levels: `user -> role -> permissions`

## Hybrid Approach (Recommended)

Combine both `@QueryConfig` (for mandatory joins) and `@JoinControl` (for optional joins):

```java
@Getter
@Setter
@Builder
@QueryConfig(
    joins = {
        // ALWAYS join tenant (required for all queries)
        @Join(path = "tenant", type = JoinType.LEFT, fetch = true)
    }
)
public class UserEntityRequest extends BaseEntityRequest {

    @QueryField
    private String username;

    // Optional joins controlled dynamically
    @JoinControl(path = "role", type = JoinType.LEFT, fetch = false)
    private boolean includeRole;

    @JoinControl(path = "permissions", type = JoinType.LEFT, fetch = true)
    private boolean includePermissions;
}
```

## Performance Considerations

### Without Join (includeRole = false)
```sql
SELECT u.* FROM users u WHERE u.username = ?
```
✅ Fast, minimal overhead

### With Join (includeRole = true)
```sql
SELECT u.* FROM users u LEFT JOIN roles r ON u.role_id = r.id WHERE u.username = ?
```
⚠️ Slower, but necessary when you need role data

### Best Practice
Only set the join flag to `true` when you actually need the related data in your result or for filtering.

## Common Use Cases

### Case 1: List View (No Join Needed)
```java
// Just show username, don't need role
var request = UserEntityRequest.builder()
    .includeRole(false)
    .build();
```

### Case 2: Detail View (Join Needed)
```java
// Show user with role information
var request = UserEntityRequest.builder()
    .id(userId)
    .includeRole(true)
    .build();
```

### Case 3: Filter by Role (Join Needed for Query)
```java
// Find users with specific role
var request = UserEntityRequest.builder()
    .username("john")
    .includeRole(true)  // Need join to filter by role
    .build();
```

## How It Works

1. The `SpecificationBuilder` scans for fields annotated with `@JoinControl`
2. It checks if the field is a boolean type
3. If the boolean value is `true`, it applies the join specified in the annotation
4. If `false` or not set, the join is skipped

## Migration from @QueryConfig

### Before (Static Join)
```java
@QueryConfig(
    joins = {
        @Join(path = "role", type = JoinType.LEFT, fetch = false)
    }
)
public class UserEntityRequest extends BaseEntityRequest {
    // Always joins, even when not needed
}
```

### After (Dynamic Join)
```java
public class UserEntityRequest extends BaseEntityRequest {
    @JoinControl(path = "role", type = JoinType.LEFT, fetch = false)
    private boolean includeRole;
    // Joins only when includeRole = true
}
```

## Testing

```java
@Test
void testWithJoin() {
    var request = UserEntityRequest.builder()
        .username("test")
        .includeRole(true)
        .build();
    
    var spec = new SpecificationBuilder<User>().buildSpecification(request);
    var users = userRepository.findAll(spec);
    
    // Role will be joined in the query
}

@Test
void testWithoutJoin() {
    var request = UserEntityRequest.builder()
        .username("test")
        .includeRole(false)
        .build();
    
    var spec = new SpecificationBuilder<User>().buildSpecification(request);
    var users = userRepository.findAll(spec);
    
    // No role join, better performance
}
```

## Summary

- ✅ **Production Ready**: Both `@QueryConfig` and `@JoinControl` are valid
- ✅ **Performance**: `@JoinControl` provides better performance optimization
- ✅ **Flexibility**: Enables runtime control over joins
- ✅ **Recommended**: Use hybrid approach - `@QueryConfig` for mandatory joins, `@JoinControl` for optional ones

