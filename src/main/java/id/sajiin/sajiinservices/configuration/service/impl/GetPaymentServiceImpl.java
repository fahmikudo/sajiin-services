package id.sajiin.sajiinservices.configuration.service.impl;

import id.sajiin.sajiinservices.configuration.domain.Payment;
import id.sajiin.sajiinservices.configuration.model.dto.PaymentDto;
import id.sajiin.sajiinservices.configuration.model.mapper.PaymentMapper;
import id.sajiin.sajiinservices.configuration.model.request.GetPaymentRequestDto;
import id.sajiin.sajiinservices.configuration.model.response.GetPaymentResponseDto;
import id.sajiin.sajiinservices.configuration.repository.PaymentRepository;
import id.sajiin.sajiinservices.configuration.repository.query.PaymentEntityRequest;
import id.sajiin.sajiinservices.configuration.service.GetPaymentService;
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
public class GetPaymentServiceImpl implements GetPaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public GetPaymentResponseDto execute(GetPaymentRequestDto request) throws GeneralException {
        validateRequest(request);
        Payment paymentById = getPaymentById(request);
        PaymentDto paymentDto = paymentMapper.toDto(paymentById);
        return new GetPaymentResponseDto(paymentDto);
    }

    private Payment getPaymentById(GetPaymentRequestDto request) {
        PaymentEntityRequest paymentEntityRequest = new PaymentEntityRequest();
        paymentEntityRequest.setId(request.getId());

        Optional<List<Payment>> payments = paymentRepository.find(paymentEntityRequest);
        if (payments.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found with id: " + request.getId());
        }
        return payments.get().getFirst();
    }

    private void validateRequest(GetPaymentRequestDto request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request can not be null");
        }
        if (NumberUtil.isNullOrZero(request.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id can not be null or zero");
        }
    }
}
