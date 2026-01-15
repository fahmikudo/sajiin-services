package id.sajiin.sajiinservices.identity.service.impl;

import id.sajiin.sajiinservices.identity.domain.Permission;
import id.sajiin.sajiinservices.identity.model.dto.PermissionDto;
import id.sajiin.sajiinservices.identity.model.mapper.PermissionMapper;
import id.sajiin.sajiinservices.identity.model.request.CreatePermissionRequestDto;
import id.sajiin.sajiinservices.identity.model.response.CreatePermissionResponseDto;
import id.sajiin.sajiinservices.identity.repository.PermissionRepository;
import id.sajiin.sajiinservices.identity.service.CreatePermissionService;
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
public class CreatePermissionServiceImpl implements CreatePermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Transactional
    @Override
    public CreatePermissionResponseDto execute(CreatePermissionRequestDto request) throws GeneralException {
        validateRequest(request);
        Permission newPermission = new Permission();
        newPermission.setPermissionId(request.getPermissionId());
        newPermission.setName(request.getName());
        newPermission.setDescription(request.getDescription());
        newPermission.setStatus(request.getStatus());
        newPermission.setCreatedBy(String.valueOf(request.getUserId()));

        Permission savedPermission = permissionRepository.saveAndReturn(newPermission);
        PermissionDto permissionDto = permissionMapper.toDto(savedPermission);

        return new CreatePermissionResponseDto(permissionDto);
    }

    private void validateRequest(CreatePermissionRequestDto request) {
        if (request.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this service");
        }
        if (request.getPermissionId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Permission ID can not be null");
        }
        if (request.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name can not be null");
        }
    }
}
