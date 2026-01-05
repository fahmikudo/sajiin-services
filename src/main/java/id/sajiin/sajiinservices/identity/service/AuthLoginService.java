package id.sajiin.sajiinservices.identity.service;

import id.sajiin.sajiinservices.identity.model.request.LoginRequestDto;
import id.sajiin.sajiinservices.identity.model.response.LoginResponseDto;
import id.sajiin.sajiinservices.shared.core.BaseService;

public interface AuthLoginService extends BaseService<LoginRequestDto, LoginResponseDto> {
}
