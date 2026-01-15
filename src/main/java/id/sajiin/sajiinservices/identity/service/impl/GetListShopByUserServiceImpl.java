package id.sajiin.sajiinservices.identity.service.impl;

import id.sajiin.sajiinservices.identity.domain.Employee;
import id.sajiin.sajiinservices.identity.domain.User;
import id.sajiin.sajiinservices.identity.model.dto.AuthShopDto;
import id.sajiin.sajiinservices.identity.model.request.GetListShopByUserRequestDto;
import id.sajiin.sajiinservices.identity.model.request.LoginRequestDto;
import id.sajiin.sajiinservices.identity.model.response.GetListShopByUserResponseDto;
import id.sajiin.sajiinservices.identity.repository.EmployeeRepository;
import id.sajiin.sajiinservices.identity.repository.UserRepository;
import id.sajiin.sajiinservices.identity.repository.query.EmployeeEntityRequest;
import id.sajiin.sajiinservices.identity.repository.query.UserEntityRequest;
import id.sajiin.sajiinservices.identity.service.GetListShopByUserService;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import id.sajiin.sajiinservices.shared.util.StringUtil;
import id.sajiin.sajiinservices.store.model.dto.ShopDto;
import id.sajiin.sajiinservices.store.model.request.ListShopRequest;
import id.sajiin.sajiinservices.store.model.response.ListShopResponse;
import id.sajiin.sajiinservices.store.service.ListShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetListShopByUserServiceImpl implements GetListShopByUserService {

    private final ListShopService listShopService;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Override
    public GetListShopByUserResponseDto execute(GetListShopByUserRequestDto request) throws GeneralException {
        if (StringUtil.isNullOrEmpty(request.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be null or empty");
        }
        User userData = getUserData(request.getUsername());
        Set<Long> shopIds = getDataShops(userData);
        return new GetListShopByUserResponseDto(shopIds);
    }

    private User getUserData(String username) {
        UserEntityRequest entityRequest = UserEntityRequest.builder()
                .username(username)
                .build();
        Optional<List<User>> users = userRepository.find(entityRequest);
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password");
        }
        return users.get().getFirst();
    }

    private Set<Long> getDataShops(User userData) {
        Set<Long> shopIds;
        if (userData.getOwnerId() != null) {
            Employee employee = getEmployeeById(userData.getOwnerId());
            ListShopRequest listShopRequest = ListShopRequest.builder()
                    .id(employee.getShopId())
                    .build();
            ListShopResponse shopResponse = listShopService.execute(listShopRequest);
            shopIds = shopResponse.getShops().stream()
                    .map(ShopDto::id)
                    .collect(Collectors.toSet());
        } else {
            ListShopRequest listShopRequest = ListShopRequest.builder()
                    .userId(userData.getId())
                    .build();
            ListShopResponse shopResponse = listShopService.execute(listShopRequest);
            shopIds = shopResponse.getShops().stream()
                    .map(ShopDto::id)
                    .collect(Collectors.toSet());
        }
        return shopIds;
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
}
