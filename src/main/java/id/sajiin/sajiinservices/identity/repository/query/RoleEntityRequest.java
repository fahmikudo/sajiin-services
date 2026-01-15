package id.sajiin.sajiinservices.identity.repository.query;

import id.sajiin.sajiinservices.shared.core.BaseEntityRequest;
import id.sajiin.sajiinservices.shared.specification.QueryField;
import id.sajiin.sajiinservices.shared.specification.QueryOperator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntityRequest extends BaseEntityRequest {

    @QueryField
    private Long id;

    @QueryField
    private String roleId;

    @QueryField
    private String roleName;

    @QueryField(operator = QueryOperator.LIKE, ignoreCase = true, orGroup = "search")
    private String likeRoleName;

    @QueryField(operator = QueryOperator.LIKE, ignoreCase = true, orGroup = "search")
    private String likeDescription;

    @QueryField
    private String type;

    @QueryField
    private String status;

}
