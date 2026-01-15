package id.sajiin.sajiinservices.configuration.presentation.response;

import id.sajiin.sajiinservices.shared.presentation.BaseListResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetListPaymentResponse extends BaseListResponse<List<PaymentResponse>> {
}
