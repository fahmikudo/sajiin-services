package id.sajiin.sajiinservices.identity.repository;

import id.sajiin.sajiinservices.identity.domain.Permission;
import id.sajiin.sajiinservices.identity.repository.query.PermissionEntityRequest;
import id.sajiin.sajiinservices.shared.repository.BaseRepository;

public interface PermissionRepository extends BaseRepository<Permission, PermissionEntityRequest> {
}
