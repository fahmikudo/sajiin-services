package id.sajiin.sajiinservices.store.service.impl;

import id.sajiin.sajiinservices.shared.exception.GeneralException;
import id.sajiin.sajiinservices.store.domain.Shop;
import id.sajiin.sajiinservices.store.mapper.ShopMapper;
import id.sajiin.sajiinservices.store.model.dto.ShopDto;
import id.sajiin.sajiinservices.store.model.request.ListShopRequest;
import id.sajiin.sajiinservices.store.model.response.ListShopResponse;
import id.sajiin.sajiinservices.store.repository.ShopRepository;
import id.sajiin.sajiinservices.store.repository.query.ShopEntityRequest;
import id.sajiin.sajiinservices.store.service.ListShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ListShopServiceImpl implements ListShopService {

    private final ShopRepository shopRepository;
    private final ShopMapper shopMapper;

    @Override
    public ListShopResponse execute(ListShopRequest request) throws GeneralException {
        validateRequest(request);
        List<ShopDto> shops = getShops(request);
        return new ListShopResponse(shops);
    }

    private List<ShopDto> getShops(ListShopRequest request) {
        ShopEntityRequest shopEntityRequest = ShopEntityRequest.builder()
                .id(request.getId())
                .userId(request.getUserId())
                .build();
        Optional<List<Shop>> shops = shopRepository.find(shopEntityRequest);
        if (shops.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shops not found");
        }
        return mapToShopDtoList(shops.get());
    }

    public List<ShopDto> mapToShopDtoList(List<Shop> shops) {
        return shops.stream()
                .map(shopMapper::shopToShopDto)
                .toList();
    }

    private void validateRequest(ListShopRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be null");
        }
    }


}
