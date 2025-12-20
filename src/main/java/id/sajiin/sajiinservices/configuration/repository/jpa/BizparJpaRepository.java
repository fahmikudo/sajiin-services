package id.sajiin.sajiinservices.configuration.repository.jpa;

import id.sajiin.sajiinservices.configuration.domain.Bizpar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BizparJpaRepository extends JpaRepository<Bizpar, Long>, JpaSpecificationExecutor<Bizpar> {
}
