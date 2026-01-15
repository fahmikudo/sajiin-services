package id.sajiin.sajiinservices.identity.service.impl;

import id.sajiin.sajiinservices.identity.domain.Role;
import id.sajiin.sajiinservices.identity.model.dto.RoleDto;
import id.sajiin.sajiinservices.identity.model.mapper.RoleMapper;
import id.sajiin.sajiinservices.identity.model.request.CreateRoleRequestDto;
import id.sajiin.sajiinservices.identity.model.response.CreateRoleResponseDto;
import id.sajiin.sajiinservices.identity.repository.RoleRepository;
import id.sajiin.sajiinservices.identity.service.CreateRoleService;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateRoleServiceImpl implements CreateRoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Transactional
    @Override
    public CreateRoleResponseDto execute(CreateRoleRequestDto request) throws GeneralException {
        validateRequest(request);
        Role newRole = new Role();
        newRole.setRoleId(request.getRoleId());
        newRole.setRoleName(request.getRoleName());
        newRole.setDescription(request.getDescription());
        newRole.setType(request.getType());
        newRole.setStatus(request.getStatus());

        Role savedRole = roleRepository.saveAndReturn(newRole);
        RoleDto roleDto = roleMapper.toDto(savedRole);

        return new CreateRoleResponseDto(roleDto);
    }

    private void validateRequest(CreateRoleRequestDto request) {
        if (request.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this service");
        }
        if (request.getRoleId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role ID can not be null");
        }
        if (request.getRoleName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role Name can not be null");
        }
    }
}
