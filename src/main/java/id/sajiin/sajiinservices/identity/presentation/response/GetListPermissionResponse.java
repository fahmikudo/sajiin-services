package id.sajiin.sajiinservices.identity.presentation.response;

import id.sajiin.sajiinservices.shared.presentation.BaseListResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetListPermissionResponse extends BaseListResponse<List<PermissionResponse>> {
}
