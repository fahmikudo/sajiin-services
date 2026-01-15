package id.sajiin.sajiinservices.configuration.repository.query;

import id.sajiin.sajiinservices.shared.core.BaseEntityRequest;
import id.sajiin.sajiinservices.shared.specification.QueryField;
import id.sajiin.sajiinservices.shared.specification.QueryOperator;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntityRequest extends BaseEntityRequest {

    @QueryField
    private Long id;

    @QueryField(operator = QueryOperator.LIKE, ignoreCase = true)
    private String name;
    
    @QueryField
    private String paymentId;

}
