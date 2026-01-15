package id.sajiin.sajiinservices.identity.model.mapper;

import id.sajiin.sajiinservices.identity.domain.Permission;
import id.sajiin.sajiinservices.identity.model.dto.PermissionDto;
import id.sajiin.sajiinservices.identity.presentation.response.PermissionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionDto toDto(Permission permission);

    Permission toEntity(PermissionDto permissionDto);

    PermissionResponse toResponse(PermissionDto permissionDto);

}
