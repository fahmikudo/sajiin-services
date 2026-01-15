package id.sajiin.sajiinservices.identity.model.request;

import id.sajiin.sajiinservices.shared.core.BaseServiceRequest;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleRequestDto extends BaseServiceRequest {
    private String roleId;
    private String roleName;
    private String description;
    private String type;
    private String status;
}
