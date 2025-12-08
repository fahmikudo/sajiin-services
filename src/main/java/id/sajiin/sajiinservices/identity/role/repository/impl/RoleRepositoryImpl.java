package id.sajiin.sajiinservices.identity.role.repository.impl;

import id.sajiin.sajiinservices.identity.role.repository.query.RoleEntityRequest;
import id.sajiin.sajiinservices.identity.role.domain.Role;
import id.sajiin.sajiinservices.identity.role.repository.RoleJpaRepository;
import id.sajiin.sajiinservices.identity.role.repository.RoleRepository;
import id.sajiin.sajiinservices.shared.specification.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final SpecificationBuilder<Role> specificationBuilder;

    public RoleRepositoryImpl(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
        this.specificationBuilder = new SpecificationBuilder<>();
    }

    @Override
    public void save(Role role) {
        roleJpaRepository.save(role);
    }

    @Override
    public Role saveAndReturn(Role entity) {
        return roleJpaRepository.save(entity);
    }

    @Override
    public Optional<List<Role>> find(RoleEntityRequest request) {
        Specification<Role> specification = builderSpecification(request);
        List<Role> products = roleJpaRepository.findAll(specification);
        if (products.isEmpty()) {
            return Optional.of(Collections.emptyList());
        }
        return Optional.of(products);
    }

    @Override
    public Page<Role> findWithPagination(RoleEntityRequest request) {
        return null;
    }

    public Specification<Role> builderSpecification(RoleEntityRequest request) {
        return specificationBuilder.buildSpecification(request);
    }
}
