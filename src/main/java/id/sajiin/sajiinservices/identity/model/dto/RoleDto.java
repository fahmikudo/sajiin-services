package id.sajiin.sajiinservices.identity.model.dto;

import java.time.LocalDateTime;

public record RoleDto(
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
