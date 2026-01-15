package id.sajiin.sajiinservices.identity.service.impl;

import id.sajiin.sajiinservices.identity.domain.Role;
import id.sajiin.sajiinservices.identity.model.dto.RoleDto;
import id.sajiin.sajiinservices.identity.model.mapper.RoleMapper;
import id.sajiin.sajiinservices.identity.model.request.GetRoleRequestDto;
import id.sajiin.sajiinservices.identity.model.response.GetRoleResponseDto;
import id.sajiin.sajiinservices.identity.repository.RoleRepository;
import id.sajiin.sajiinservices.identity.repository.query.RoleEntityRequest;
import id.sajiin.sajiinservices.identity.service.GetRoleService;
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
public class GetRoleServiceImpl implements GetRoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public GetRoleResponseDto execute(GetRoleRequestDto request) throws GeneralException {
        validateRequest(request);
        Role roleById = getRoleById(request);
        RoleDto roleDto = roleMapper.toDto(roleById);
        return new GetRoleResponseDto(roleDto);
    }

    private Role getRoleById(GetRoleRequestDto request) {
        RoleEntityRequest roleEntityRequest = RoleEntityRequest.builder()
                .id(request.getId())
                .build();

        Optional<List<Role>> roles = roleRepository.find(roleEntityRequest);
        if (roles.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id: " + request.getId());
        }
        return roles.get().getFirst();
    }

    private void validateRequest(GetRoleRequestDto request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request can not be null");
        }
        if (NumberUtil.isNullOrZero(request.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id can not be null or zero");
        }
    }
}
