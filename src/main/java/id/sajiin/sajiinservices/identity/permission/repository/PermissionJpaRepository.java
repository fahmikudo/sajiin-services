package id.sajiin.sajiinservices.identity.permission.repository;

import id.sajiin.sajiinservices.identity.permission.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionJpaRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
}
