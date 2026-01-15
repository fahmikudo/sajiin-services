package id.sajiin.sajiinservices.store.model.request;

import id.sajiin.sajiinservices.shared.core.BaseListServiceRequest;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListShopRequest extends BaseListServiceRequest {

    private Long id;

    private Long userId;
    
    private String search;

}
