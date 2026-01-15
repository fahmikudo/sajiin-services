package id.sajiin.sajiinservices.configuration.repository.impl;

import id.sajiin.sajiinservices.configuration.domain.Payment;
import id.sajiin.sajiinservices.configuration.repository.PaymentRepository;
import id.sajiin.sajiinservices.configuration.repository.jpa.PaymentJpaRepository;
import id.sajiin.sajiinservices.configuration.repository.query.PaymentEntityRequest;
import id.sajiin.sajiinservices.shared.specification.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final SpecificationBuilder<Payment> specificationBuilder;

    public PaymentRepositoryImpl(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
        this.specificationBuilder = new SpecificationBuilder<>();
    }

    @Override
    public void save(Payment entity) {
        paymentJpaRepository.save(entity);
    }

    @Override
    public Payment saveAndReturn(Payment entity) {
        return paymentJpaRepository.save(entity);
    }

    @Override
    public Optional<List<Payment>> find(PaymentEntityRequest request) {
        Specification<Payment> specification = buildSpecification(request);
        List<Payment> payments = paymentJpaRepository.findAll(specification);
        if (payments.isEmpty()) {
            return Optional.of(List.of());
        }
        return Optional.of(payments);
    }

    @Override
    public Page<Payment> findWithPagination(PaymentEntityRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPageNumber(), request.getPageSize());
        Specification<Payment> specification = buildSpecification(request);
        Page<Payment> paymentPage = paymentJpaRepository.findAll(specification, pageRequest);
        if (paymentPage.isEmpty()) {
            return Page.empty();
        }
        return new PageImpl<>(paymentPage.getContent(), pageRequest, paymentPage.getTotalElements());
    }

    public Specification<Payment> buildSpecification(PaymentEntityRequest request) {
        return specificationBuilder.buildSpecification(request);
    }
}
