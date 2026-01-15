package id.sajiin.sajiinservices.identity.model.request;

import id.sajiin.sajiinservices.shared.core.BaseServiceRequest;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleRequestDto extends BaseServiceRequest {
    private Long id;
    private String roleId;
    private String roleName;
    private String description;
    private String type;
    private String status;
}
