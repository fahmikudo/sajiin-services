package id.sajiin.sajiinservices.identity.role.model;

import id.sajiin.sajiinservices.shared.core.BaseServiceRequest;

public class GetRoleServiceRequest extends BaseServiceRequest {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
