package id.sajiin.sajiinservices.identity.rolepermission.repository.impl;

import id.sajiin.sajiinservices.identity.rolepermission.domain.RolePermission;
import id.sajiin.sajiinservices.identity.rolepermission.repository.RolePermissionJpaRepository;
import id.sajiin.sajiinservices.identity.rolepermission.repository.RolePermissionRepository;
import id.sajiin.sajiinservices.identity.rolepermission.repository.query.RolePermissionEntityRequest;
import id.sajiin.sajiinservices.shared.specification.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RolePermissionRepositoryImpl implements RolePermissionRepository {

    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final SpecificationBuilder<RolePermission> specificationBuilder;

    public RolePermissionRepositoryImpl(RolePermissionJpaRepository rolePermissionJpaRepository) {
        this.rolePermissionJpaRepository = rolePermissionJpaRepository;
        this.specificationBuilder = new SpecificationBuilder<>();
    }

    @Override
    public void save(RolePermission entity) {
        rolePermissionJpaRepository.save(entity);
    }

    @Override
    public RolePermission saveAndReturn(RolePermission entity) {
        return rolePermissionJpaRepository.save(entity);
    }

    @Override
    public Optional<List<RolePermission>> find(RolePermissionEntityRequest request) {
        Specification<RolePermission> specification = buildSpecification(request);
        List<RolePermission> rolePermissions = rolePermissionJpaRepository.findAll(specification);
        if (rolePermissions.isEmpty()) {
            return Optional.of(List.of());
        }
        return Optional.of(rolePermissions);
    }

    @Override
    public Page<RolePermission> findWithPagination(RolePermissionEntityRequest request) {
        return null;
    }

    public Specification<RolePermission> buildSpecification(RolePermissionEntityRequest request) {
        return specificationBuilder.buildSpecification(request);
    }

}
