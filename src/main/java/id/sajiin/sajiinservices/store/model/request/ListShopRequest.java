package id.sajiin.sajiinservices.store.model.request;

import id.sajiin.sajiinservices.shared.core.BaseListServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ListShopRequest extends BaseListServiceRequest {

    private Long id;

    private Long userId;

}
