package id.sajiin.sajiinservices.identity.service.impl;

import id.sajiin.sajiinservices.identity.domain.Role;
import id.sajiin.sajiinservices.identity.model.dto.RoleDto;
import id.sajiin.sajiinservices.identity.model.mapper.RoleMapper;
import id.sajiin.sajiinservices.identity.model.request.UpdateRoleRequestDto;
import id.sajiin.sajiinservices.identity.model.response.UpdateRoleResponseDto;
import id.sajiin.sajiinservices.identity.repository.RoleRepository;
import id.sajiin.sajiinservices.identity.repository.query.RoleEntityRequest;
import id.sajiin.sajiinservices.identity.service.UpdateRoleService;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import id.sajiin.sajiinservices.shared.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateRoleServiceImpl implements UpdateRoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public UpdateRoleResponseDto execute(UpdateRoleRequestDto request) throws GeneralException {
        validateRequest(request);
        RoleEntityRequest roleEntityRequest = RoleEntityRequest.builder()
                .id(request.getId())
                .build();

        Optional<List<Role>> roles = roleRepository.find(roleEntityRequest);
        if (roles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }
        Role role = roles.get().getFirst();
        role.setRoleId(request.getRoleId());
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        role.setType(request.getType());
        role.setStatus(request.getStatus());

        Role updatedRole = roleRepository.saveAndReturn(role);
        RoleDto roleDto = roleMapper.toDto(updatedRole);

        return new UpdateRoleResponseDto(roleDto);
    }

    private void validateRequest(UpdateRoleRequestDto request) {
        if (NumberUtil.isNullOrZero(request.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this service");
        }
        if (NumberUtil.isNullOrZero(request.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id can not be null");
        }
    }
}
