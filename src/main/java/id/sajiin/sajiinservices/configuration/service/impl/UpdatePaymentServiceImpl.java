package id.sajiin.sajiinservices.configuration.service.impl;

import id.sajiin.sajiinservices.configuration.domain.Payment;
import id.sajiin.sajiinservices.configuration.model.dto.PaymentDto;
import id.sajiin.sajiinservices.configuration.model.mapper.PaymentMapper;
import id.sajiin.sajiinservices.configuration.model.request.UpdatePaymentRequestDto;
import id.sajiin.sajiinservices.configuration.model.response.UpdatePaymentResponseDto;
import id.sajiin.sajiinservices.configuration.repository.PaymentRepository;
import id.sajiin.sajiinservices.configuration.repository.query.PaymentEntityRequest;
import id.sajiin.sajiinservices.configuration.service.UpdatePaymentService;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import id.sajiin.sajiinservices.shared.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdatePaymentServiceImpl implements UpdatePaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public UpdatePaymentResponseDto execute(UpdatePaymentRequestDto request) throws GeneralException {
        validateRequest(request);
        PaymentEntityRequest paymentEntityRequest = new PaymentEntityRequest();
        paymentEntityRequest.setId(request.getId());

        Optional<List<Payment>> payments = paymentRepository.find(paymentEntityRequest);
        if (payments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }
        Payment payment = payments.get().getFirst();
        payment.setPaymentId(request.getPaymentId());
        payment.setImage(request.getImage());
        payment.setName(request.getName());
        payment.setDescription(request.getDescription());
        payment.setStatus(request.getStatus());
        payment.setUpdatedBy(String.valueOf(request.getUserId()));

        Payment updatedPayment = paymentRepository.saveAndReturn(payment);
        PaymentDto paymentDto = paymentMapper.toDto(updatedPayment);

        return new UpdatePaymentResponseDto(paymentDto);
    }

    private void validateRequest(UpdatePaymentRequestDto request) {
        if (NumberUtil.isNullOrZero(request.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this service");
        }
        if (NumberUtil.isNullOrZero(request.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id can not be null");
        }
    }
}
