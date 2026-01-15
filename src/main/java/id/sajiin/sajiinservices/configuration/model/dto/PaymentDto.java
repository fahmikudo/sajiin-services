package id.sajiin.sajiinservices.configuration.model.dto;

import id.sajiin.sajiinservices.shared.model.ActiveStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PaymentDto(
        Long id,
        String paymentId,
        String image,
        String name,
        String description,
        ActiveStatus status,
        Long userId,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy
) {
}
