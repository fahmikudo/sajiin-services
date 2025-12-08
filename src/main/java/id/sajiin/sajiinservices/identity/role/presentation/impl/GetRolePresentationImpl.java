package id.sajiin.sajiinservices.identity.role.presentation.impl;

import id.sajiin.sajiinservices.identity.role.dto.RoleDto;
import id.sajiin.sajiinservices.identity.role.model.GetRoleServiceResponse;
import id.sajiin.sajiinservices.identity.role.presentation.GetRolePresentation;
import id.sajiin.sajiinservices.identity.role.presentation.response.GetRoleResponse;
import id.sajiin.sajiinservices.identity.role.presentation.response.RoleResponse;
import id.sajiin.sajiinservices.shared.constant.MessageConstant;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class GetRolePresentationImpl implements GetRolePresentation {
    @Override
    public GetRoleResponse present(GetRoleServiceResponse serviceResponse) throws GeneralException {
        var response = new GetRoleResponse();
        RoleResponse roleResponse = constructRoleResponse(serviceResponse.getRole());
        response.setSuccess(Boolean.TRUE);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setData(roleResponse);
        return response;
    }

    private RoleResponse constructRoleResponse(RoleDto role) {
        return new RoleResponse(
            role.id(),
            role.roleId(),
            role.roleName(),
            role.description(),
            role.type(),
            role.status(),
            role.createdAt(),
            role.updatedAt()
        );
    }
}
