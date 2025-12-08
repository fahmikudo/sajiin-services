package id.sajiin.sajiinservices.identity.rolepermission.repository;

import id.sajiin.sajiinservices.identity.rolepermission.domain.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionJpaRepository extends JpaRepository<RolePermission, Long>, JpaSpecificationExecutor<RolePermission> {
}
