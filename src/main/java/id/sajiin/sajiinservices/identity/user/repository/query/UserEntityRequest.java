package id.sajiin.sajiinservices.identity.user.repository.query;

import id.sajiin.sajiinservices.shared.core.BaseEntityRequest;
import id.sajiin.sajiinservices.shared.specification.JoinControl;
import id.sajiin.sajiinservices.shared.specification.JoinType;
import id.sajiin.sajiinservices.shared.specification.QueryField;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserEntityRequest extends BaseEntityRequest {

    @QueryField
    private Long id;

    @QueryField
    private String username;

    @JoinControl(path = "role", type = JoinType.LEFT, fetch = false)
    private boolean includeRole;

}
