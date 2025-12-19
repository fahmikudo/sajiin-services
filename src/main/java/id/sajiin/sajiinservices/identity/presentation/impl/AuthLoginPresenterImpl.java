package id.sajiin.sajiinservices.identity.presentation.impl;

import id.sajiin.sajiinservices.identity.model.dto.AuthPermissionDto;
import id.sajiin.sajiinservices.identity.model.dto.AuthShopDto;
import id.sajiin.sajiinservices.identity.model.response.AuthLoginResponse;
import id.sajiin.sajiinservices.identity.presentation.AuthLoginPresenter;
import id.sajiin.sajiinservices.identity.presentation.response.LoginDto;
import id.sajiin.sajiinservices.identity.presentation.response.LoginPermissionResponse;
import id.sajiin.sajiinservices.identity.presentation.response.LoginResponse;
import id.sajiin.sajiinservices.identity.presentation.response.LoginShopResponse;
import id.sajiin.sajiinservices.shared.constant.MessageConstant;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthLoginPresenterImpl implements AuthLoginPresenter {

    @Override
    public LoginResponse present(AuthLoginResponse serviceResponse) throws GeneralException {
        var permissions = serviceResponse.getPermissions();
        var authPermissions = constructPermissionResponses(permissions);
        var authShops = constructShopResponses(serviceResponse.getShops());
        var loginDto = LoginDto.builder()
                .accessToken(serviceResponse.getAccessToken())
                .refreshToken(serviceResponse.getRefreshToken())
                .userId(serviceResponse.getUserId())
                .username(serviceResponse.getUsername())
                .email(serviceResponse.getEmail())
                .profilePicture(serviceResponse.getProfilePicture())
                .roleId(serviceResponse.getRoleId())
                .roleName(serviceResponse.getRoleName())
                .roleType(serviceResponse.getRoleType())
                .permissions(authPermissions)
                .shops(authShops)
                .build();

        var response = new LoginResponse();
        response.setSuccess(Boolean.TRUE);
        response.setData(loginDto);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        return response;
    }

    private List<LoginShopResponse> constructShopResponses(List<AuthShopDto> shops) {
        if (shops == null || shops.isEmpty()) {
            return List.of();
        }
        return shops.stream()
                .map(shop -> new LoginShopResponse(
                        shop.id(),
                        shop.shopId(),
                        shop.shopName(),
                        shop.shopEmail(),
                        shop.shopLocation(),
                        shop.shopPhoneNumber(),
                        shop.shopOpenDay(),
                        shop.shopCloseDay(),
                        shop.shopOpenTime(),
                        shop.shopCloseTime(),
                        shop.isNonFnb(),
                        shop.isDigitalOrder(),
                        shop.isDigitalMenu()
                ))
                .toList();
    }

    private List<LoginPermissionResponse> constructPermissionResponses(List<AuthPermissionDto> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return List.of();
        }
        return permissions.stream()
                .map(permission -> new LoginPermissionResponse(
                        permission.permissionId(),
                        permission.permissionName()
                ))
                .toList();
    }
}
