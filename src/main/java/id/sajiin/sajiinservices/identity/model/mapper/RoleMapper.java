package id.sajiin.sajiinservices.identity.model.mapper;

import id.sajiin.sajiinservices.identity.domain.Role;
import id.sajiin.sajiinservices.identity.model.dto.RoleDto;
import id.sajiin.sajiinservices.identity.presentation.response.RoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDto);

    RoleResponse toResponse(RoleDto roleDto);

}
