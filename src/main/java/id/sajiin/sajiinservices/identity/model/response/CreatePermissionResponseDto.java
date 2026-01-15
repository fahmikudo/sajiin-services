package id.sajiin.sajiinservices.identity.model.response;

import id.sajiin.sajiinservices.identity.model.dto.PermissionDto;
import id.sajiin.sajiinservices.shared.core.BaseServiceResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreatePermissionResponseDto extends BaseServiceResponse {
    private PermissionDto permission;
}
