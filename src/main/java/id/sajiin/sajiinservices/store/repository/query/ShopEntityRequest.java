package id.sajiin.sajiinservices.store.repository.query;

import id.sajiin.sajiinservices.shared.core.BaseEntityRequest;
import id.sajiin.sajiinservices.shared.specification.QueryField;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShopEntityRequest extends BaseEntityRequest {

    @QueryField
    private Long id;

    @QueryField
    private Long userId;

}
