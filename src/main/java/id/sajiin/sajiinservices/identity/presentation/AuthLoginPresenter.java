package id.sajiin.sajiinservices.identity.presentation;

import id.sajiin.sajiinservices.identity.model.response.LoginResponseDto;
import id.sajiin.sajiinservices.identity.presentation.response.AuthLoginResponse;
import id.sajiin.sajiinservices.identity.presentation.response.LoginResponse;
import id.sajiin.sajiinservices.shared.presentation.BasePresentation;

public interface AuthLoginPresenter extends BasePresentation<AuthLoginResponse, LoginResponseDto> {
}
