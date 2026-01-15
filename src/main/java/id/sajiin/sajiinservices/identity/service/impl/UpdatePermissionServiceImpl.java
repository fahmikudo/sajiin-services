package id.sajiin.sajiinservices.identity.service.impl;

import id.sajiin.sajiinservices.identity.domain.Permission;
import id.sajiin.sajiinservices.identity.model.dto.PermissionDto;
import id.sajiin.sajiinservices.identity.model.mapper.PermissionMapper;
import id.sajiin.sajiinservices.identity.model.request.UpdatePermissionRequestDto;
import id.sajiin.sajiinservices.identity.model.response.UpdatePermissionResponseDto;
import id.sajiin.sajiinservices.identity.repository.PermissionRepository;
import id.sajiin.sajiinservices.identity.repository.query.PermissionEntityRequest;
import id.sajiin.sajiinservices.identity.service.UpdatePermissionService;
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
public class UpdatePermissionServiceImpl implements UpdatePermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public UpdatePermissionResponseDto execute(UpdatePermissionRequestDto request) throws GeneralException {
        validateRequest(request);
        PermissionEntityRequest permissionEntityRequest = PermissionEntityRequest.builder()
                .id(request.getId())
                .build();

        Optional<List<Permission>> permissions = permissionRepository.find(permissionEntityRequest);
        if (permissions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission not found");
        }
        Permission permission = permissions.get().getFirst();
        permission.setPermissionId(request.getPermissionId());
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        permission.setStatus(request.getStatus());
        permission.setUpdatedBy(String.valueOf(request.getUserId()));

        Permission updatedPermission = permissionRepository.saveAndReturn(permission);
        PermissionDto permissionDto = permissionMapper.toDto(updatedPermission);

        return new UpdatePermissionResponseDto(permissionDto);
    }

    private void validateRequest(UpdatePermissionRequestDto request) {
        if (NumberUtil.isNullOrZero(request.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this service");
        }
        if (NumberUtil.isNullOrZero(request.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id can not be null");
        }
    }
}
