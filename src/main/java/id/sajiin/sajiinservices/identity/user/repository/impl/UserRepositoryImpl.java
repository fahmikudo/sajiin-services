package id.sajiin.sajiinservices.identity.user.repository.impl;

import id.sajiin.sajiinservices.identity.user.domain.User;
import id.sajiin.sajiinservices.identity.user.repository.UserJpaRepository;
import id.sajiin.sajiinservices.identity.user.repository.UserRepository;
import id.sajiin.sajiinservices.identity.user.repository.query.UserEntityRequest;
import id.sajiin.sajiinservices.shared.specification.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final SpecificationBuilder<User> specificationBuilder;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.specificationBuilder = new SpecificationBuilder<>();
    }

    @Override
    public void save(User entity) {
        userJpaRepository.save(entity);
    }

    @Override
    public User saveAndReturn(User entity) {
        return userJpaRepository.save(entity);
    }

    @Override
    public Optional<List<User>> find(UserEntityRequest request) {
        Specification<User> specification = buildSpecification(request);
        List<User> users = userJpaRepository.findAll(specification);
        if (users.isEmpty()) {
            return Optional.of(List.of());
        }
        return Optional.of(users);
    }

    @Override
    public Page<User> findWithPagination(UserEntityRequest request) {
        return null;
    }

    public Specification<User> buildSpecification(UserEntityRequest request) {
        return specificationBuilder.buildSpecification(request);
    }
}
