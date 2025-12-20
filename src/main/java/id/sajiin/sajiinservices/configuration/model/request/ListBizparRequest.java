package id.sajiin.sajiinservices.configuration.model.request;

import id.sajiin.sajiinservices.shared.core.BaseListServiceRequest;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListBizparRequest extends BaseListServiceRequest {

    private String search;

}
