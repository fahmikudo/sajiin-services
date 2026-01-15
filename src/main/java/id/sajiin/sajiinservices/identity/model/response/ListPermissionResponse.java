package id.sajiin.sajiinservices.identity.model.response;

import id.sajiin.sajiinservices.identity.model.dto.PermissionDto;
import id.sajiin.sajiinservices.shared.core.BaseListServiceResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListPermissionResponse extends BaseListServiceResponse {
    private List<PermissionDto> permissions;
}
