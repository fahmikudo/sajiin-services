package id.sajiin.sajiinservices.identity.repository.query;

import id.sajiin.sajiinservices.shared.core.BaseEntityRequest;
import id.sajiin.sajiinservices.shared.specification.QueryField;
import id.sajiin.sajiinservices.shared.specification.QueryOperator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class PermissionEntityRequest extends BaseEntityRequest {

    @QueryField
    private Long id;

    @QueryField
    private String permissionId;

    @QueryField
    private String name;

    @QueryField(operator = QueryOperator.LIKE, ignoreCase = true, orGroup = "search")
    private String likeName;

    @QueryField(operator = QueryOperator.LIKE, ignoreCase = true, orGroup = "search")
    private String likeDescription;

    @QueryField
    private String status;

    @QueryField(operator = QueryOperator.BETWEEN)
    private List<LocalDateTime> createdAt;



}
