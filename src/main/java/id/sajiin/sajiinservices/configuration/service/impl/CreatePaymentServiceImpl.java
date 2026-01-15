package id.sajiin.sajiinservices.configuration.service.impl;

import id.sajiin.sajiinservices.configuration.domain.Payment;
import id.sajiin.sajiinservices.configuration.model.dto.PaymentDto;
import id.sajiin.sajiinservices.configuration.model.mapper.PaymentMapper;
import id.sajiin.sajiinservices.configuration.model.request.CreatePaymentRequestDto;
import id.sajiin.sajiinservices.configuration.model.response.CreatePaymentResponseDto;
import id.sajiin.sajiinservices.configuration.repository.PaymentRepository;
import id.sajiin.sajiinservices.configuration.service.CreatePaymentService;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreatePaymentServiceImpl implements CreatePaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    @Override
    public CreatePaymentResponseDto execute(CreatePaymentRequestDto request) throws GeneralException {
        validateRequest(request);
        Payment newPayment = new Payment();
        newPayment.setPaymentId(request.getPaymentId());
        newPayment.setImage(request.getImage());
        newPayment.setName(request.getName());
        newPayment.setDescription(request.getDescription());
        newPayment.setStatus(request.getStatus());
        newPayment.setCreatedBy(String.valueOf(request.getUserId()));

        Payment savedPayment = paymentRepository.saveAndReturn(newPayment);
        PaymentDto paymentDto = paymentMapper.toDto(savedPayment);

        return new CreatePaymentResponseDto(paymentDto);
    }

    private void validateRequest(CreatePaymentRequestDto request) {
        if (request.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this service");
        }
        if (request.getPaymentId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment ID can not be null");
        }
        if (request.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name can not be null");
        }
    }
}
