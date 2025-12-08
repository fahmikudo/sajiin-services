package id.sajiin.sajiinservices.identity.user.presentation.request;

public record LoginRequest (
        String username,
        String password
) {
}
