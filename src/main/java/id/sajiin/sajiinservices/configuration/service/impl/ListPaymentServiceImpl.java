package id.sajiin.sajiinservices.configuration.service.impl;

import id.sajiin.sajiinservices.configuration.domain.Payment;
import id.sajiin.sajiinservices.configuration.model.dto.PaymentDto;
import id.sajiin.sajiinservices.configuration.model.mapper.PaymentMapper;
import id.sajiin.sajiinservices.configuration.model.request.ListPaymentRequest;
import id.sajiin.sajiinservices.configuration.model.response.ListPaymentResponse;
import id.sajiin.sajiinservices.configuration.repository.PaymentRepository;
import id.sajiin.sajiinservices.configuration.repository.query.PaymentEntityRequest;
import id.sajiin.sajiinservices.configuration.service.ListPaymentService;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import id.sajiin.sajiinservices.shared.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListPaymentServiceImpl implements ListPaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public ListPaymentResponse execute(ListPaymentRequest request) throws GeneralException {
        validateRequest(request);
        ListPaymentResponse response = null;
        if (request.getPaginated().equals(Boolean.TRUE)) {
            response = getListPaymentWithPagination(request);
        }
        return response;
    }

    private ListPaymentResponse getListPaymentWithPagination(ListPaymentRequest request) {
        PaymentEntityRequest entityRequest = new PaymentEntityRequest();
        entityRequest.setPageNumber(request.getPagination().getPage() - 1);
        entityRequest.setPageSize(request.getPagination().getSize());
        entityRequest.setName(request.getSearch());

        Page<Payment> payments = paymentRepository.findWithPagination(entityRequest);

        ListPaymentResponse listPaymentResponse = new ListPaymentResponse();

        listPaymentResponse.setCurrentPage(payments.getNumber() + 1);
        listPaymentResponse.setTotalRecord(payments.getTotalElements());
        listPaymentResponse.setTotalPage(payments.getTotalPages());
        listPaymentResponse.setPageSize(payments.getSize());
        listPaymentResponse.setPayments(constructToPaymentDtoList(payments.getContent()));

        return listPaymentResponse;
    }

    private List<PaymentDto> constructToPaymentDtoList(List<Payment> paymentList) {
        return paymentList.stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    private void validateRequest(ListPaymentRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request list payment can not be null");
        }
        if (NumberUtil.isNullOrZero(request.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this service");
        }
    }
}
