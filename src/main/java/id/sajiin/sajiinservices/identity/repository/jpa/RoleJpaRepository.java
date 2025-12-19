package id.sajiin.sajiinservices.identity.repository.jpa;

import id.sajiin.sajiinservices.identity.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
}
