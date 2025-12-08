package id.sajiin.sajiinservices.identity.role.model;

import id.sajiin.sajiinservices.identity.role.dto.RoleDto;
import id.sajiin.sajiinservices.shared.core.BaseServiceResponse;

public class GetRoleServiceResponse extends BaseServiceResponse {

    private RoleDto role;

    public GetRoleServiceResponse(RoleDto role) {
        this.role = role;
    }

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }
}
