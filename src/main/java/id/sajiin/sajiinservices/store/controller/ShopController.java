package id.sajiin.sajiinservices.store.controller;

import id.sajiin.sajiinservices.security.UserContext;
import id.sajiin.sajiinservices.security.UserContextHolder;
import id.sajiin.sajiinservices.shared.constant.MessageConstant;
import id.sajiin.sajiinservices.shared.core.PageEntityRequest;
import id.sajiin.sajiinservices.shared.presentation.ErrorResponse;
import id.sajiin.sajiinservices.shared.presentation.PaginationResponse;
import id.sajiin.sajiinservices.store.mapper.ShopMapper;
import id.sajiin.sajiinservices.store.model.request.ListShopRequest;
import id.sajiin.sajiinservices.store.presentation.response.GetListShopResponse;
import id.sajiin.sajiinservices.store.service.ListShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
@Tag(name = "Shop", description = "Shop API Endpoints")
public class ShopController {

    private final ListShopService listShopService;
    private final ShopMapper shopMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get All Shop with Pagination Endpoint",
            summary = "Retrieve a paginated list of shop entries",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = GetListShopResponse.class))),
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
    public ResponseEntity<GetListShopResponse> getAllShopsWithPagination(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search) {
        UserContext userContext = UserContextHolder.get();
        ListShopRequest serviceRequest = ListShopRequest.builder()
                .userId(userContext.getUserId())
                .search(search)
                .build();
        serviceRequest.setPaginated(true);
        serviceRequest.setPagination(new PageEntityRequest(page, size));

        var serviceResponse = listShopService.execute(serviceRequest);

        GetListShopResponse response = new GetListShopResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setPagination(new PaginationResponse(serviceResponse.getCurrentPage(), serviceResponse.getPageSize(), serviceResponse.getTotalRecord(), serviceResponse.getTotalPage()));
        response.setData(serviceResponse.getShops().stream().map(shopMapper::toResponse).toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
