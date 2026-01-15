package id.sajiin.sajiinservices.configuration.model.request;

import id.sajiin.sajiinservices.shared.core.BaseServiceRequest;
import id.sajiin.sajiinservices.shared.model.ActiveStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequestDto extends BaseServiceRequest {
    private String paymentId;
    private String image;
    private String name;
    private String description;
    private ActiveStatus status;
}
