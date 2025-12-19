package id.sajiin.sajiinservices.identity.presentation.response;

public record LoginShopResponse (
        Long id,
        String shopId,
        String shopName,
        String shopEmail,
        String shopLocation,
        String shopPhoneNumber,
        String shopOpenDay,
        String shopCloseDay,
        String shopOpenTime,
        String shopCloseTime,
        Boolean isNonFnb,
        Boolean isDigitalOrder,
        Boolean isDigitalMenu
) {
}
