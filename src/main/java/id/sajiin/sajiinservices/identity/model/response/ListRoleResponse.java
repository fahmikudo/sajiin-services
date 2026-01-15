package id.sajiin.sajiinservices.identity.model.response;

import id.sajiin.sajiinservices.identity.model.dto.RoleDto;
import id.sajiin.sajiinservices.shared.core.BaseListServiceResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListRoleResponse extends BaseListServiceResponse {
    private List<RoleDto> roles;
}
