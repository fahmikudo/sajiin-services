package id.sajiin.sajiinservices.identity.model.request;

import id.sajiin.sajiinservices.shared.core.BaseServiceRequest;
import id.sajiin.sajiinservices.shared.model.ActiveStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePermissionRequestDto extends BaseServiceRequest {
    private String permissionId;
    private String name;
    private String description;
    private ActiveStatus status;
}
