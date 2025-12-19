package id.sajiin.sajiinservices.store.mapper;

import id.sajiin.sajiinservices.store.domain.Shop;
import id.sajiin.sajiinservices.store.model.dto.ShopDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShopMapper {

    ShopDto shopToShopDto(Shop shop);

}
