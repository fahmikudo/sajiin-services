package id.sajiin.sajiinservices.identity.presentation.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RoleResponse (
        Long id,
        String roleId,
        String roleName,
        String description,
        String type,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
