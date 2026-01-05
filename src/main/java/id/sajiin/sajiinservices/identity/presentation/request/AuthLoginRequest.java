package id.sajiin.sajiinservices.identity.presentation.request;

public record AuthLoginRequest(
        String username,
        String password
) {
}
