package id.sajiin.sajiinservices.configuration.controller;

import id.sajiin.sajiinservices.configuration.model.request.ListBizparRequest;
import id.sajiin.sajiinservices.configuration.presentation.ListBizparPresentation;
import id.sajiin.sajiinservices.configuration.presentation.response.GetListBizparResponse;
import id.sajiin.sajiinservices.configuration.service.ListBizparService;
import id.sajiin.sajiinservices.identity.presentation.response.AuthLoginResponse;
import id.sajiin.sajiinservices.security.UserContext;
import id.sajiin.sajiinservices.security.UserContextHolder;
import id.sajiin.sajiinservices.shared.core.PageEntityRequest;
import id.sajiin.sajiinservices.shared.presentation.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bizpars")
@RequiredArgsConstructor
public class BizparController {

    private final ListBizparService listBizparService;
    private final ListBizparPresentation listBizparPresentation;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get All Bizpar with Pagination Endpoint",
            summary = "Retrieve a paginated list of bizpar entries",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = GetListBizparResponse.class))),
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
    public ResponseEntity<GetListBizparResponse> getAllBizparsWithPagination(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search) {
        UserContext userContext = UserContextHolder.get();
        ListBizparRequest serviceRequest = new ListBizparRequest();
        serviceRequest.setUserId(userContext.getUserId());
        serviceRequest.setPaginated(true);
        serviceRequest.setPagination(new PageEntityRequest(page, size));
        serviceRequest.setSearch(search);

        var serviceResponse = listBizparService.execute(serviceRequest);
        var response = listBizparPresentation.present(serviceResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
