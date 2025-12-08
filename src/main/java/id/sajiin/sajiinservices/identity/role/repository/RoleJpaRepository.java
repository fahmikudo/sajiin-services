package id.sajiin.sajiinservices.identity.role.repository;

import id.sajiin.sajiinservices.identity.role.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
}
