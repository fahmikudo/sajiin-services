package id.sajiin.sajiinservices.identity.user.presentation.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class LoginDto {
    private String accessToken;
    private String refreshToken;

    private Long userId;
    private String username;
    private String email;
    private String profilePicture;

    private Long roleId;
    private String roleName;
    private String roleType;

    private List<LoginPermissionResponse> permissions;
}
