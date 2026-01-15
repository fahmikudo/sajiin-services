package id.sajiin.sajiinservices.identity.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoleRequest {
    @NotBlank
    private String roleId;
    @NotBlank
    private String roleName;
    private String description;
    @NotBlank
    private String type;
    @NotBlank
    private String status;
}
