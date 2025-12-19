package id.sajiin.sajiinservices.store.model.dto;
import java.time.LocalDateTime;

public record ShopDto (
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
