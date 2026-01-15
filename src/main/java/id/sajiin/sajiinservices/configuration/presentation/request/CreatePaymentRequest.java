package id.sajiin.sajiinservices.configuration.presentation.request;

import id.sajiin.sajiinservices.shared.model.ActiveStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentRequest {
    @NotBlank
    private String paymentId;
    private String image;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private ActiveStatus status;
}
