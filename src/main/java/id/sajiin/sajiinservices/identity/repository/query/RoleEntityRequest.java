package id.sajiin.sajiinservices.identity.repository.query;

import id.sajiin.sajiinservices.shared.core.BaseEntityRequest;
import id.sajiin.sajiinservices.shared.specification.QueryField;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoleEntityRequest extends BaseEntityRequest {

    @QueryField
    private Long id;

    @QueryField
    private String roleName;

}
