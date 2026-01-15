package id.sajiin.sajiinservices.identity.model.request;

import id.sajiin.sajiinservices.shared.core.BaseListServiceRequest;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListRoleRequest extends BaseListServiceRequest {
    private String search;
}
