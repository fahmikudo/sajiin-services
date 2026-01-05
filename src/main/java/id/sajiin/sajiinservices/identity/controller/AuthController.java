package id.sajiin.sajiinservices.identity.controller;

import id.sajiin.sajiinservices.identity.model.request.LoginRequestDto;
import id.sajiin.sajiinservices.identity.presentation.AuthLoginPresenter;
import id.sajiin.sajiinservices.identity.presentation.request.AuthLoginRequest;
import id.sajiin.sajiinservices.identity.presentation.response.AuthLoginResponse;
import id.sajiin.sajiinservices.identity.presentation.response.LoginResponse;
import id.sajiin.sajiinservices.identity.service.AuthLoginService;
import id.sajiin.sajiinservices.shared.presentation.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth", description = "Authentication API Endpoints")
public class AuthController {

    private final AuthLoginService authLoginService;
    private final AuthLoginPresenter authLoginPresenter;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "User Authentication Login Endpoint",
            summary = "Authenticate user and generate access token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = AuthLoginResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid Credentials",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Failed\",\"errors\":[\"Invalid username or password\"]}"))),
                    @ApiResponse(responseCode = "400", description = "Invalid Request",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Failed\",\"errors\":[\"Invalid request parameter\"]}"))),
                    @ApiResponse(responseCode = "404", description = "Data not found",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Failed\",\"errors\":[\"Data not found\"]}"))),
                    @ApiResponse(responseCode = "500", description = "An unexpected error occurred on the server. Please try again later or contact support if the issue persists",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Failed\",\"errors\":[\"An unexpected error occurred on the server\"]}"))),
            }
    )
    public ResponseEntity<@NonNull AuthLoginResponse> authLogin(@RequestBody AuthLoginRequest authLoginRequest) {

        LoginRequestDto authLoginEntityRequest = new LoginRequestDto();
        authLoginEntityRequest.setUsername(authLoginRequest.username());
        authLoginEntityRequest.setPassword(authLoginRequest.password());

        var entityResponse = authLoginService.execute(authLoginEntityRequest);

        var response = authLoginPresenter.present(entityResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
