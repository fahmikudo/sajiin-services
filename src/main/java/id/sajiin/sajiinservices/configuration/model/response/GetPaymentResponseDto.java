package id.sajiin.sajiinservices.configuration.model.response;

import id.sajiin.sajiinservices.configuration.model.dto.PaymentDto;
import id.sajiin.sajiinservices.shared.core.BaseServiceResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPaymentResponseDto extends BaseServiceResponse {
    private PaymentDto payment;
}
