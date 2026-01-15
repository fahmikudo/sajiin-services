package id.sajiin.sajiinservices.store.presentation.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ShopResponse (
        Long id,
        String shopId,
        String image,
        String name,
        String about,
        String location,
        String email,
        String phone,
        String openDay,
        String closeDay,
        String openTime,
        String closeTime,
        Boolean isOpened,
        Boolean isNonFnb,
        Boolean isDigitalOrderActive,
        Boolean isDigitalMenuActive,
        Boolean isAvailable,
        String status,
        Long userId,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy
) {
}
