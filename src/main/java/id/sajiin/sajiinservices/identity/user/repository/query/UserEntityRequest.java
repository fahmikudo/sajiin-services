package id.sajiin.sajiinservices.identity.user.repository.query;

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
        joins = {
                @Join(path = "role", type = JoinType.LEFT, fetch = false),
        }
)
public class UserEntityRequest extends BaseEntityRequest {

    @QueryField
    private Long id;

    @QueryField
    private String username;


}
