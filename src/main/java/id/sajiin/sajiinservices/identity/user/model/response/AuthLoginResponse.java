package id.sajiin.sajiinservices.identity.user.model.response;

import id.sajiin.sajiinservices.identity.user.model.dto.AuthPermissionDto;
import id.sajiin.sajiinservices.shared.core.BaseServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AuthLoginResponse extends BaseServiceResponse {

    private String accessToken;
    private String refreshToken;

    private Long userId;
    private String username;
    private String email;
    private String profilePicture;

    private Long roleId;
    private String roleName;
    private String roleType;

    private List<AuthPermissionDto> permissions;

}
