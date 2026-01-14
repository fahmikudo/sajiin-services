package id.sajiin.sajiinservices.identity.model.mapper;

import id.sajiin.sajiinservices.identity.model.dto.AuthPermissionDto;
import id.sajiin.sajiinservices.identity.model.dto.AuthShopDto;
import id.sajiin.sajiinservices.identity.model.response.LoginResponseDto;
import id.sajiin.sajiinservices.identity.presentation.response.LoginPermissionResponse;
import id.sajiin.sajiinservices.identity.presentation.response.LoginResponse;
import id.sajiin.sajiinservices.identity.presentation.response.LoginShopResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    LoginResponse toLoginResponse(LoginResponseDto dto);

    LoginShopResponse toShopResponse(AuthShopDto dto);

    LoginPermissionResponse toPermissionResponse(AuthPermissionDto dto);

}
