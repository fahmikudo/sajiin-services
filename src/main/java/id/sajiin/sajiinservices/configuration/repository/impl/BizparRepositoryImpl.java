package id.sajiin.sajiinservices.configuration.repository.impl;

import id.sajiin.sajiinservices.configuration.domain.Bizpar;
import id.sajiin.sajiinservices.configuration.repository.BizparRepository;
import id.sajiin.sajiinservices.configuration.repository.jpa.BizparJpaRepository;
import id.sajiin.sajiinservices.configuration.repository.query.BizparEntityRequest;
import id.sajiin.sajiinservices.shared.specification.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BizparRepositoryImpl implements BizparRepository {

    private final BizparJpaRepository bizparJpaRepository;
    private final SpecificationBuilder<Bizpar> specificationBuilder;

    public BizparRepositoryImpl(BizparJpaRepository bizparJpaRepository) {
        this.bizparJpaRepository = bizparJpaRepository;
        this.specificationBuilder = new SpecificationBuilder<>();
    }


    @Override
    public void save(Bizpar entity) {
        bizparJpaRepository.save(entity);
    }

    @Override
    public Bizpar saveAndReturn(Bizpar entity) {
        return bizparJpaRepository.save(entity);
    }

    @Override
    public Optional<List<Bizpar>> find(BizparEntityRequest request) {
        Specification<Bizpar> specification = buildSpecification(request);
        List<Bizpar> bizpars = bizparJpaRepository.findAll(specification);
        if (bizpars.isEmpty()) {
            return Optional.of(List.of());
        }
        return Optional.of(bizpars);
    }

    @Override
    public Page<Bizpar> findWithPagination(BizparEntityRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPageNumber(), request.getPageSize());
        Specification<Bizpar> specification = buildSpecification(request);
        Page<Bizpar> bizparPage = bizparJpaRepository.findAll(specification, pageRequest);
        if (bizparPage.isEmpty()) {
            return Page.empty();
        }
        return new PageImpl<>(bizparPage.getContent(), pageRequest, bizparPage.getTotalElements());
    }

    public Specification<Bizpar> buildSpecification(BizparEntityRequest request) {
        return specificationBuilder.buildSpecification(request);
    }
}
