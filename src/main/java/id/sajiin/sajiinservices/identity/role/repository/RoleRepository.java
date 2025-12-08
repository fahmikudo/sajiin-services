package id.sajiin.sajiinservices.identity.role.repository;

import id.sajiin.sajiinservices.identity.role.domain.Role;
import id.sajiin.sajiinservices.identity.role.repository.query.RoleEntityRequest;
import id.sajiin.sajiinservices.shared.repository.BaseRepository;

public interface RoleRepository extends BaseRepository<Role, RoleEntityRequest> {

}
