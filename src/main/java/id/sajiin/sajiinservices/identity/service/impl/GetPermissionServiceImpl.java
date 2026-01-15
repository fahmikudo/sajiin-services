package id.sajiin.sajiinservices.identity.service.impl;

import id.sajiin.sajiinservices.identity.domain.Permission;
import id.sajiin.sajiinservices.identity.model.dto.PermissionDto;
import id.sajiin.sajiinservices.identity.model.mapper.PermissionMapper;
import id.sajiin.sajiinservices.identity.model.request.GetPermissionRequestDto;
import id.sajiin.sajiinservices.identity.model.response.GetPermissionResponseDto;
import id.sajiin.sajiinservices.identity.repository.PermissionRepository;
import id.sajiin.sajiinservices.identity.repository.query.PermissionEntityRequest;
import id.sajiin.sajiinservices.identity.service.GetPermissionService;
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
public class GetPermissionServiceImpl implements GetPermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public GetPermissionResponseDto execute(GetPermissionRequestDto request) throws GeneralException {
        validateRequest(request);
        Permission permissionById = getPermissionById(request);
        PermissionDto permissionDto = permissionMapper.toDto(permissionById);
        return new GetPermissionResponseDto(permissionDto);
    }

    private Permission getPermissionById(GetPermissionRequestDto request) {
        PermissionEntityRequest permissionEntityRequest = PermissionEntityRequest.builder()
                .id(request.getId())
                .build();

        Optional<List<Permission>> permissions = permissionRepository.find(permissionEntityRequest);
        if (permissions.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission not found with id: " + request.getId());
        }
        return permissions.get().getFirst();
    }

    private void validateRequest(GetPermissionRequestDto request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request can not be null");
        }
        if (NumberUtil.isNullOrZero(request.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id can not be null or zero");
        }
    }
}
