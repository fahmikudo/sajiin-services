package id.sajiin.sajiinservices.identity.service.impl;

import id.sajiin.sajiinservices.identity.domain.Role;
import id.sajiin.sajiinservices.identity.model.dto.RoleDto;
import id.sajiin.sajiinservices.identity.model.mapper.RoleMapper;
import id.sajiin.sajiinservices.identity.model.request.ListRoleRequest;
import id.sajiin.sajiinservices.identity.model.response.ListRoleResponse;
import id.sajiin.sajiinservices.identity.repository.RoleRepository;
import id.sajiin.sajiinservices.identity.repository.query.RoleEntityRequest;
import id.sajiin.sajiinservices.identity.service.ListRoleService;
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
public class ListRoleServiceImpl implements ListRoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public ListRoleResponse execute(ListRoleRequest request) throws GeneralException {
        validateRequest(request);
        ListRoleResponse response = null;
        if (request.getPaginated().equals(Boolean.TRUE)) {
            response = getListRoleWithPagination(request);
        }
        return response;
    }

    private ListRoleResponse getListRoleWithPagination(ListRoleRequest request) {
        RoleEntityRequest entityRequest = new RoleEntityRequest();
        entityRequest.setPageNumber(request.getPagination().getPage());
        entityRequest.setPageSize(request.getPagination().getSize());
        entityRequest.setLikeRoleName(request.getSearch());

        Page<Role> roles = roleRepository.findWithPagination(entityRequest);

        ListRoleResponse listRoleResponse = new ListRoleResponse();

        listRoleResponse.setCurrentPage(roles.getNumber() + 1);
        listRoleResponse.setTotalRecord(roles.getTotalElements());
        listRoleResponse.setTotalPage(roles.getTotalPages());
        listRoleResponse.setPageSize(roles.getSize());
        listRoleResponse.setRoles(constructToRoleDtoList(roles.getContent()));

        return listRoleResponse;
    }

    private List<RoleDto> constructToRoleDtoList(List<Role> roleList) {
        return roleList.stream()
                .map(roleMapper::toDto)
                .toList();
    }

    private void validateRequest(ListRoleRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request list role can not be null");
        }
        if (NumberUtil.isNullOrZero(request.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this service");
        }
    }
}
