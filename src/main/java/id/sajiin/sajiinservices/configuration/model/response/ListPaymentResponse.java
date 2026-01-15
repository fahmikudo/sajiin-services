package id.sajiin.sajiinservices.configuration.model.response;

import id.sajiin.sajiinservices.configuration.model.dto.PaymentDto;
import id.sajiin.sajiinservices.shared.core.BaseListServiceResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListPaymentResponse extends BaseListServiceResponse {
    private List<PaymentDto> payments;
}
