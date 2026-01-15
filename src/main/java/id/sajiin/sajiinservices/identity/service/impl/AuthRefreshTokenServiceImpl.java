package id.sajiin.sajiinservices.identity.service.impl;

import id.sajiin.sajiinservices.identity.domain.User;
import id.sajiin.sajiinservices.identity.model.request.GetListShopByUserRequestDto;
import id.sajiin.sajiinservices.identity.model.request.RefreshTokenRequestDto;
import id.sajiin.sajiinservices.identity.model.response.GetListShopByUserResponseDto;
import id.sajiin.sajiinservices.identity.model.response.RefreshTokenResponseDto;
import id.sajiin.sajiinservices.identity.repository.UserRepository;
import id.sajiin.sajiinservices.identity.repository.query.UserEntityRequest;
import id.sajiin.sajiinservices.identity.service.AuthRefreshTokenService;
import id.sajiin.sajiinservices.identity.service.GetListShopByUserService;
import id.sajiin.sajiinservices.security.JwtService;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import id.sajiin.sajiinservices.shared.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthRefreshTokenServiceImpl implements AuthRefreshTokenService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final GetListShopByUserService getListShopByUserService;

    @Override
    public RefreshTokenResponseDto execute(RefreshTokenRequestDto request) throws GeneralException {
        if (StringUtil.isNullOrEmpty(request.getToken())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is required");
        }
        String token = jwtService.extractToken(request.getToken());
        String username = jwtService.extractUsername(token);
        User user = getUserData(username);

        if (!jwtService.isTokenValid(token, user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token");
        }

        GetListShopByUserResponseDto getListShopByUserResponseDto = getListShopByUserService.execute(new GetListShopByUserRequestDto(username));
        Set<Long> shopIds = getListShopByUserResponseDto.getShopIds();

        String accessToken = jwtService.generateToken(username, shopIds);
        String refreshToken = jwtService.generateRefreshToken(user);
        Long expiredAt = jwtService.getExpiryIn();

        return new RefreshTokenResponseDto(accessToken, refreshToken, expiredAt);
    }

    private User getUserData(String username) {
        UserEntityRequest entityRequest = UserEntityRequest.builder()
                .username(username)
                .build();
        Optional<List<User>> users = userRepository.find(entityRequest);
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
        return users.get().getFirst();
    }

}
