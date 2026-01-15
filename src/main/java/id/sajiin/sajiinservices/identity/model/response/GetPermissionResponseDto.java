package id.sajiin.sajiinservices.identity.model.response;

import id.sajiin.sajiinservices.identity.model.dto.PermissionDto;
import id.sajiin.sajiinservices.shared.core.BaseServiceResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPermissionResponseDto extends BaseServiceResponse {
    private PermissionDto permission;
}
