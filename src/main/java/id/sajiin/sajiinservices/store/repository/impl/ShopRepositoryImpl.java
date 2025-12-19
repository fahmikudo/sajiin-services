package id.sajiin.sajiinservices.store.repository.impl;

import id.sajiin.sajiinservices.shared.specification.SpecificationBuilder;
import id.sajiin.sajiinservices.store.domain.Shop;
import id.sajiin.sajiinservices.store.repository.jpa.ShopJpaRepository;
import id.sajiin.sajiinservices.store.repository.ShopRepository;
import id.sajiin.sajiinservices.store.repository.query.ShopEntityRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ShopRepositoryImpl implements ShopRepository {

    private final ShopJpaRepository shopJpaRepository;
    private final SpecificationBuilder<Shop> specificationBuilder;

    public ShopRepositoryImpl(ShopJpaRepository shopJpaRepository) {
        this.shopJpaRepository = shopJpaRepository;
        this.specificationBuilder = new SpecificationBuilder<>();
    }

    @Override
    public void save(Shop entity) {
        shopJpaRepository.save(entity);
    }

    @Override
    public Shop saveAndReturn(Shop entity) {
        return shopJpaRepository.save(entity);
    }

    @Override
    public Optional<List<Shop>> find(ShopEntityRequest request) {
        Specification<Shop> specification = buildSpecification(request);
        List<Shop> shops = shopJpaRepository.findAll(specification);
        if (!shops.isEmpty()) {
            return Optional.of(Collections.emptyList());
        }
        return Optional.of(shops);
    }

    @Override
    public Page<Shop> findWithPagination(ShopEntityRequest request) {
        return null;
    }

    public Specification<Shop> buildSpecification(ShopEntityRequest request) {
        return specificationBuilder.buildSpecification(request);
    }

}
