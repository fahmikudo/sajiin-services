package id.sajiin.sajiinservices.identity.repository.impl;

import id.sajiin.sajiinservices.identity.domain.Permission;
import id.sajiin.sajiinservices.identity.repository.jpa.PermissionJpaRepository;
import id.sajiin.sajiinservices.identity.repository.PermissionRepository;
import id.sajiin.sajiinservices.identity.repository.query.PermissionEntityRequest;
import id.sajiin.sajiinservices.shared.specification.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class PermissionRepositoryImpl implements PermissionRepository {

    private final PermissionJpaRepository permissionJpaRepository;
    private final SpecificationBuilder<Permission> specificationBuilder;

    public PermissionRepositoryImpl(PermissionJpaRepository permissionJpaRepository) {
        this.permissionJpaRepository = permissionJpaRepository;
        this.specificationBuilder = new SpecificationBuilder<>();
    }

    @Override
    public void save(Permission entity) {
        permissionJpaRepository.save(entity);
    }

    @Override
    public Permission saveAndReturn(Permission entity) {
        return permissionJpaRepository.save(entity);
    }

    @Override
    public Optional<List<Permission>> find(PermissionEntityRequest request) {
        Specification<Permission> specification = buildSpecification(request);
        List<Permission> permissions = permissionJpaRepository.findAll(specification);
        if (permissions.isEmpty()) {
            return Optional.of(Collections.emptyList());
        }
        return Optional.of(permissions);
    }

    @Override
    public Page<Permission> findWithPagination(PermissionEntityRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPageNumber(), request.getPageSize());
        Specification<Permission> specification = buildSpecification(request);
        Page<Permission> permissionPage = permissionJpaRepository.findAll(specification, pageRequest);
        if (permissionPage.isEmpty()) {
            return Page.empty();
        }
        return new PageImpl<>(permissionPage.getContent(), pageRequest, permissionPage.getTotalElements());
    }

    public Specification<Permission> buildSpecification(PermissionEntityRequest request) {
        return specificationBuilder.buildSpecification(request);
    }
}
