package id.sajiin.sajiinservices.identity.repository;

import id.sajiin.sajiinservices.identity.domain.Employee;
import id.sajiin.sajiinservices.identity.repository.query.EmployeeEntityRequest;
import id.sajiin.sajiinservices.shared.repository.BaseRepository;

public interface EmployeeRepository extends BaseRepository<Employee, EmployeeEntityRequest> {
}
