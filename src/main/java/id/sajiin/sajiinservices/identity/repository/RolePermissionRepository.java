package id.sajiin.sajiinservices.identity.repository;

import id.sajiin.sajiinservices.identity.domain.RolePermission;
import id.sajiin.sajiinservices.identity.repository.query.RolePermissionEntityRequest;
import id.sajiin.sajiinservices.shared.repository.BaseRepository;

public interface RolePermissionRepository extends BaseRepository<RolePermission, RolePermissionEntityRequest> {
}
