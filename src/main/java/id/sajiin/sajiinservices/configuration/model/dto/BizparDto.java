package id.sajiin.sajiinservices.configuration.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BizparDto (
        Long id,
        String key,
        String value,
        String type,
        String description,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy
) {
}
