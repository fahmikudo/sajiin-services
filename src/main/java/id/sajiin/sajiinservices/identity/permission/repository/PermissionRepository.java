package id.sajiin.sajiinservices.identity.permission.repository;

import id.sajiin.sajiinservices.identity.permission.domain.Permission;
import id.sajiin.sajiinservices.identity.permission.repository.query.PermissionEntityRequest;
import id.sajiin.sajiinservices.shared.repository.BaseRepository;

public interface PermissionRepository extends BaseRepository<Permission, PermissionEntityRequest> {
}
