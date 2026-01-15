package id.sajiin.sajiinservices.identity.presentation.request;

import id.sajiin.sajiinservices.shared.model.ActiveStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePermissionRequest {
    @NotBlank
    private String permissionId;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private ActiveStatus status;
}
