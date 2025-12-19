package id.sajiin.sajiinservices.identity.repository.jpa;

import id.sajiin.sajiinservices.identity.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeJpaRepository extends JpaRepository<Employee,Long>, JpaSpecificationExecutor<Employee> {
}
