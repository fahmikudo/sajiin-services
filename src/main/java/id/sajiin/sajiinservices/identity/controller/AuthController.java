package id.sajiin.sajiinservices.identity.controller;

import id.sajiin.sajiinservices.identity.model.request.AuthLoginRequest;
import id.sajiin.sajiinservices.identity.presentation.AuthLoginPresenter;
import id.sajiin.sajiinservices.identity.presentation.request.LoginRequest;
import id.sajiin.sajiinservices.identity.presentation.response.LoginResponse;
import id.sajiin.sajiinservices.identity.service.AuthLoginService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthLoginService authLoginService;
    private final AuthLoginPresenter authLoginPresenter;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull LoginResponse> authLogin(@RequestBody LoginRequest authLoginRequest) {

        AuthLoginRequest authLoginEntityRequest = new AuthLoginRequest();
        authLoginEntityRequest.setUsername(authLoginRequest.username());
        authLoginEntityRequest.setPassword(authLoginRequest.password());

        var entityResponse = authLoginService.execute(authLoginEntityRequest);

        var response = authLoginPresenter.present(entityResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
