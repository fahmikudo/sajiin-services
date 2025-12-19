package id.sajiin.sajiinservices.identity.service.impl;

import id.sajiin.sajiinservices.identity.domain.*;
import id.sajiin.sajiinservices.identity.model.dto.AuthShopDto;
import id.sajiin.sajiinservices.identity.repository.EmployeeRepository;
import id.sajiin.sajiinservices.identity.repository.RoleRepository;
import id.sajiin.sajiinservices.identity.repository.query.EmployeeEntityRequest;
import id.sajiin.sajiinservices.identity.repository.query.RoleEntityRequest;
import id.sajiin.sajiinservices.identity.repository.RolePermissionRepository;
import id.sajiin.sajiinservices.identity.repository.query.RolePermissionEntityRequest;
import id.sajiin.sajiinservices.identity.model.dto.AuthPermissionDto;
import id.sajiin.sajiinservices.identity.model.request.AuthLoginRequest;
import id.sajiin.sajiinservices.identity.model.response.AuthLoginResponse;
import id.sajiin.sajiinservices.identity.repository.UserRepository;
import id.sajiin.sajiinservices.identity.repository.query.UserEntityRequest;
import id.sajiin.sajiinservices.identity.service.AuthLoginService;
import id.sajiin.sajiinservices.security.JwtService;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import id.sajiin.sajiinservices.store.model.dto.ShopDto;
import id.sajiin.sajiinservices.store.model.request.ListShopRequest;
import id.sajiin.sajiinservices.store.model.response.ListShopResponse;
import id.sajiin.sajiinservices.store.service.ListShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthLoginServiceImpl implements AuthLoginService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final EmployeeRepository employeeRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final ListShopService listShopService;

    @Override
    public AuthLoginResponse execute(AuthLoginRequest request) throws GeneralException {
        validateRequest(request);
        User userData = getUserData(request);

        boolean isMatch = passwordEncoder.matches(request.getPassword(), userData.getPassword());
        if (!isMatch) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password");
        }

        Role roleData = getRoleData(userData.getRole().getId());

        List<AuthShopDto> authShopDtos = getDataShops(userData);
        List<AuthPermissionDto> authPermissionDtos = getRolePermissionByRoleId(roleData.getId());

        String accessToken = jwtService.generateToken(userData.getUsername());
        String refreshToken = jwtService.generateRefreshToken(userData);

        return AuthLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(userData.getId())
                .username(userData.getUsername())
                .email(userData.getEmail())
                .profilePicture(userData.getImage())
                .roleId(roleData.getId())
                .roleName(roleData.getRoleName())
                .roleType(roleData.getType())
                .permissions(authPermissionDtos)
                .shops(authShopDtos)
                .build();
    }

    private List<AuthShopDto> getDataShops(User userData) {
        List<AuthShopDto> shopDtos;
        if (userData.getOwnerId() != null) {
            Employee employee = getEmployeeById(userData.getOwnerId());
            ListShopRequest listShopRequest = ListShopRequest.builder()
                    .id(employee.getShopId())
                    .build();
            ListShopResponse shopResponse = listShopService.execute(listShopRequest);
            shopDtos = constructAuthShopDtos(shopResponse.getShops());
        } else {
            ListShopRequest listShopRequest = ListShopRequest.builder()
                    .userId(userData.getId())
                    .build();
            ListShopResponse shopResponse = listShopService.execute(listShopRequest);
            shopDtos = constructAuthShopDtos(shopResponse.getShops());
        }
        return shopDtos;
    }

    private List<AuthShopDto> constructAuthShopDtos(List<ShopDto> shopDtos) {
        List<AuthShopDto> authShopDtos = new ArrayList<>();
        for (ShopDto shopDto : shopDtos) {
            AuthShopDto authShopDto = new AuthShopDto(
                    shopDto.id(),
                    shopDto.shopId(),
                    shopDto.name(),
                    shopDto.email(),
                    shopDto.location(),
                    shopDto.phone(),
                    shopDto.openDay(),
                    shopDto.closeDay(),
                    shopDto.openTime(),
                    shopDto.closeTime(),
                    shopDto.isNonFnb(),
                    shopDto.isDigitalOrderActive(),
                    shopDto.isDigitalMenuActive()
            );
            authShopDtos.add(authShopDto);
        }
        return authShopDtos;
    }

    private Employee getEmployeeById(Long ownerId) {
        EmployeeEntityRequest employeeEntityRequest = EmployeeEntityRequest.builder()
                .id(ownerId)
                .build();
        var employee = employeeRepository.find(employeeEntityRequest);
        if (employee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password");
        }
        return employee.get().getFirst();
    }

    private List<AuthPermissionDto> getRolePermissionByRoleId(Long id) {
        RolePermissionEntityRequest rolePermissionEntityRequest = RolePermissionEntityRequest.builder()
                .roleId(id)
                .build();
        var rolePermissions = rolePermissionRepository.find(rolePermissionEntityRequest);
        if (rolePermissions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password");
        }

        var permissions = rolePermissions.get().stream().map(RolePermission::getPermission).toList();
        if (permissions.isEmpty()) {
            return Collections.emptyList();
        }

        return constructAuthRolePermissions(permissions);
    }

    private List<AuthPermissionDto> constructAuthRolePermissions(List<Permission> permissions) {
        List<AuthPermissionDto> permissionDtos = new ArrayList<>();
        for (Permission permission : permissions) {
            AuthPermissionDto dto = new AuthPermissionDto(
                    permission.getId(),
                    permission.getName()
            );
            permissionDtos.add(dto);
        }
        return permissionDtos;
    }

    private Role getRoleData(Long id) {
        RoleEntityRequest roleEntityRequest = RoleEntityRequest.builder()
                .id(id)
                .build();
        var role = roleRepository.find(roleEntityRequest);
        if (role.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password");
        }
        return role.get().getFirst();
    }

    private User getUserData(AuthLoginRequest request) {
        UserEntityRequest entityRequest = UserEntityRequest.builder()
                .username(request.getUsername())
                .build();
        Optional<List<User>> users = userRepository.find(entityRequest);
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password");
        }
        return users.get().getFirst();
    }

    private void validateRequest(AuthLoginRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be null");
        }
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be null or empty");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be null or empty");
        }
    }
}
