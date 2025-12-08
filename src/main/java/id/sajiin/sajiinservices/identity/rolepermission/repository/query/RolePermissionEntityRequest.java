package id.sajiin.sajiinservices.identity.rolepermission.repository.query;

import id.sajiin.sajiinservices.shared.core.BaseEntityRequest;
import id.sajiin.sajiinservices.shared.specification.Join;
import id.sajiin.sajiinservices.shared.specification.JoinType;
import id.sajiin.sajiinservices.shared.specification.QueryConfig;
import id.sajiin.sajiinservices.shared.specification.QueryField;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@QueryConfig(
        distinct = true,
        joins = {
                @Join(path = "role", type = JoinType.LEFT, fetch = true),
                @Join(path = "permission", type = JoinType.LEFT, fetch = true)
        }
)
public class RolePermissionEntityRequest extends BaseEntityRequest {

    @QueryField(path = "role.id")
    private Long roleId;


}
