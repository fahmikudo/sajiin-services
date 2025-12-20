package id.sajiin.sajiinservices.configuration.presentation.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BizparResponse (
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
