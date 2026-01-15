package id.sajiin.sajiinservices.identity.model.dto;

import id.sajiin.sajiinservices.shared.model.ActiveStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PermissionDto(
        Long id,
        String permissionId,
        String name,
        String description,
        ActiveStatus status,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy
) {
}
