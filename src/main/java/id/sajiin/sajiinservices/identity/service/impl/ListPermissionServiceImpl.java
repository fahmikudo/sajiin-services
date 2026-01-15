package id.sajiin.sajiinservices.identity.service.impl;

import id.sajiin.sajiinservices.identity.domain.Permission;
import id.sajiin.sajiinservices.identity.model.dto.PermissionDto;
import id.sajiin.sajiinservices.identity.model.mapper.PermissionMapper;
import id.sajiin.sajiinservices.identity.model.request.ListPermissionRequest;
import id.sajiin.sajiinservices.identity.model.response.ListPermissionResponse;
import id.sajiin.sajiinservices.identity.repository.PermissionRepository;
import id.sajiin.sajiinservices.identity.repository.query.PermissionEntityRequest;
import id.sajiin.sajiinservices.identity.service.ListPermissionService;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import id.sajiin.sajiinservices.shared.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListPermissionServiceImpl implements ListPermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public ListPermissionResponse execute(ListPermissionRequest request) throws GeneralException {
        validateRequest(request);
        ListPermissionResponse response = null;
        if (request.getPaginated().equals(Boolean.TRUE)) {
            response = getListPermissionWithPagination(request);
        }
        return response;
    }

    private ListPermissionResponse getListPermissionWithPagination(ListPermissionRequest request) {
        PermissionEntityRequest entityRequest = new PermissionEntityRequest();
        entityRequest.setPageNumber(request.getPagination().getPage() - 1);
        entityRequest.setPageSize(request.getPagination().getSize());
        entityRequest.setLikeName(request.getSearch());

        Page<Permission> permissions = permissionRepository.findWithPagination(entityRequest);

        ListPermissionResponse listPermissionResponse = new ListPermissionResponse();

        listPermissionResponse.setCurrentPage(permissions.getNumber() + 1);
        listPermissionResponse.setTotalRecord(permissions.getTotalElements());
        listPermissionResponse.setTotalPage(permissions.getTotalPages());
        listPermissionResponse.setPageSize(permissions.getSize());
        listPermissionResponse.setPermissions(constructToPermissionDtoList(permissions.getContent()));

        return listPermissionResponse;
    }

    private List<PermissionDto> constructToPermissionDtoList(List<Permission> permissionList) {
        return permissionList.stream()
                .map(permissionMapper::toDto)
                .toList();
    }

    private void validateRequest(ListPermissionRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request list permission can not be null");
        }
        if (NumberUtil.isNullOrZero(request.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this service");
        }
    }
}
