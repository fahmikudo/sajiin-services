package id.sajiin.sajiinservices.identity.repository.impl;

import id.sajiin.sajiinservices.identity.domain.Employee;
import id.sajiin.sajiinservices.identity.repository.EmployeeRepository;
import id.sajiin.sajiinservices.identity.repository.jpa.EmployeeJpaRepository;
import id.sajiin.sajiinservices.identity.repository.query.EmployeeEntityRequest;
import id.sajiin.sajiinservices.shared.specification.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final EmployeeJpaRepository employeeJpaRepository;
    private final SpecificationBuilder<Employee> specificationBuilder;

    public EmployeeRepositoryImpl(EmployeeJpaRepository employeeJpaRepository) {
        this.employeeJpaRepository = employeeJpaRepository;
        this.specificationBuilder = new SpecificationBuilder<>();
    }

    @Override
    public void save(Employee entity) {
        employeeJpaRepository.save(entity);
    }

    @Override
    public Employee saveAndReturn(Employee entity) {
        return employeeJpaRepository.save(entity);
    }

    @Override
    public Optional<List<Employee>> find(EmployeeEntityRequest request) {
        Specification<Employee> specification = buildSpecification(request);
        List<Employee> employees = employeeJpaRepository.findAll(specification);
        if (employees.isEmpty()) {
            return Optional.of(Collections.emptyList());
        }
        return Optional.of(employees);
    }

    @Override
    public Page<Employee> findWithPagination(EmployeeEntityRequest request) {
        return null;
    }

    public Specification<Employee> buildSpecification(EmployeeEntityRequest request) {
        return specificationBuilder.buildSpecification(request);
    }
}
