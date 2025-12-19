package id.sajiin.sajiinservices.store.repository.query;

import id.sajiin.sajiinservices.shared.core.BaseEntityRequest;
import id.sajiin.sajiinservices.shared.specification.QueryField;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopEntityRequest extends BaseEntityRequest {

    @QueryField
    private Long id;

    @QueryField
    private Long userId;

}
