package id.sajiin.sajiinservices.store.presentation.response;

import id.sajiin.sajiinservices.shared.presentation.BaseListResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetListShopResponse extends BaseListResponse<List<ShopResponse>> {
}
