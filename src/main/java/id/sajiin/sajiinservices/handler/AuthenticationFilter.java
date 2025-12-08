package id.sajiin.sajiinservices.handler;

import id.sajiin.sajiinservices.identity.user.domain.User;
import id.sajiin.sajiinservices.security.JwtService;
import id.sajiin.sajiinservices.security.UserContext;
import id.sajiin.sajiinservices.security.UserContextHolder;
import id.sajiin.sajiinservices.shared.constant.MessageConstant;
import id.sajiin.sajiinservices.shared.presentation.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            final String token = request.getHeader(AUTHORIZATION_HEADER);
            final String jwt;
            final String userEmail;
            if (token == null || !token.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = token.substring(7);
            userEmail = jwtService.extractUsername(jwt);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    User user = (User) userDetails;

                    UserContext userContext = new UserContext();
                    userContext.setUserId(user.getId());

                    UserContextHolder.set(userContext);
                }
            }
            if (!request.getMethod().equalsIgnoreCase("OPTIONS")) {
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            String errors = e.getLocalizedMessage();

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage(MessageConstant.messageCode(HttpStatus.UNAUTHORIZED.value()));
            errorResponse.setErrors(Collections.singletonList(errors));

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } finally {
            UserContextHolder.clear();
        }
    }
}
