package id.sajiin.sajiinservices.identity.user.repository;

import id.sajiin.sajiinservices.identity.user.domain.User;
import id.sajiin.sajiinservices.identity.user.repository.query.UserEntityRequest;
import id.sajiin.sajiinservices.shared.repository.BaseRepository;

public interface UserRepository extends BaseRepository<User, UserEntityRequest> {
}
