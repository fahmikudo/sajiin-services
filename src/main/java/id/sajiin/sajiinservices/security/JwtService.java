package id.sajiin.sajiinservices.security;

import id.sajiin.sajiinservices.identity.domain.User;
import id.sajiin.sajiinservices.identity.repository.UserRepository;
import id.sajiin.sajiinservices.identity.repository.query.UserEntityRequest;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import id.sajiin.sajiinservices.shared.util.CollectionUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${sajiin.jwt.secret}")
    private String secret;

    @Value("${sajiin.jwt.expiration-time}")
    private Long expirationTime;

    @Value("${sajiin.jwt.refresh-expiration-time}")
    private Long refreshExpirationTime;

    private final UserRepository userRepository;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Set<Long> extractShopIds(String token) {
        Claims claims = extractAllClaims(token);
        List<?> shopIdsList = claims.get("shopIds", List.class);
        if (CollectionUtil.isNotNullAndNotEmpty(shopIdsList)) {
            Set<Long> result = new LinkedHashSet<>();
            for (Object o : shopIdsList) {
                if (o == null) continue;
                if (o instanceof Number number) {
                    result.add(number.longValue());
                } else {
                    try {
                        result.add(Long.parseLong(o.toString()));
                    } catch (NumberFormatException ignored) {
                        return Collections.emptySet();
                    }
                }
            }
            return result;
        }
        return Collections.emptySet();
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String username, Set<Long> shopIds) {
        User userByEmail = getUserByUsername(username);
        Map<String, Object> extraClaims = new HashMap<>();
        if (CollectionUtil.isNotNullAndNotEmpty(shopIds)) {
            extraClaims.put("shopIds", shopIds);
        }
        return generateToken(extraClaims, userByEmail);
    }

    private User getUserByUsername(String username) {
        UserEntityRequest entityRequest = UserEntityRequest.builder()
                .username(username)
                .includeRole(false)
                .build();
        var user = userRepository.find(entityRequest)
                .orElseThrow(() -> new GeneralException("User not found"));
        return user.getFirst();
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, expirationTime);
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpirationTime);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(calculateExpirationDate(expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Date calculateExpirationDate(long expiration) {
        return new Date(System.currentTimeMillis() + expiration);
    }

}
