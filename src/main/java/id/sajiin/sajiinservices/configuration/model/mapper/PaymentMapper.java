package id.sajiin.sajiinservices.configuration.model.mapper;

import id.sajiin.sajiinservices.configuration.domain.Payment;
import id.sajiin.sajiinservices.configuration.model.dto.PaymentDto;
import id.sajiin.sajiinservices.configuration.presentation.response.PaymentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDto toDto(Payment payment);

    Payment toEntity(PaymentDto paymentDto);

    PaymentResponse toResponse(PaymentDto paymentDto);

}
