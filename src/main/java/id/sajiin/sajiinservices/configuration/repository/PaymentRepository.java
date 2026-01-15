package id.sajiin.sajiinservices.configuration.repository;

import id.sajiin.sajiinservices.configuration.domain.Payment;
import id.sajiin.sajiinservices.configuration.repository.query.PaymentEntityRequest;
import id.sajiin.sajiinservices.shared.repository.BaseRepository;

public interface PaymentRepository extends BaseRepository<Payment, PaymentEntityRequest> {
}
