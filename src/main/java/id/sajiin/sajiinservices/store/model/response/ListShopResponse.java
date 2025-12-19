package id.sajiin.sajiinservices.store.model.response;

import id.sajiin.sajiinservices.shared.core.BaseListServiceResponse;
import id.sajiin.sajiinservices.store.model.dto.ShopDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListShopResponse extends BaseListServiceResponse {

    private List<ShopDto> shops;

}
