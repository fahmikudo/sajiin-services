package id.sajiin.sajiinservices.identity.service;

import id.sajiin.sajiinservices.identity.model.request.RefreshTokenRequestDto;
import id.sajiin.sajiinservices.identity.model.response.RefreshTokenResponseDto;
import id.sajiin.sajiinservices.shared.core.BaseService;

public interface AuthRefreshTokenService extends BaseService<RefreshTokenRequestDto, RefreshTokenResponseDto> {
}
