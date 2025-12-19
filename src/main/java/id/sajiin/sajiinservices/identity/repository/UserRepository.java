package id.sajiin.sajiinservices.identity.repository;

import id.sajiin.sajiinservices.identity.domain.User;
import id.sajiin.sajiinservices.identity.repository.query.UserEntityRequest;
import id.sajiin.sajiinservices.shared.repository.BaseRepository;

public interface UserRepository extends BaseRepository<User, UserEntityRequest> {
}
