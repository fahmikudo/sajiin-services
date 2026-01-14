package id.sajiin.sajiinservices.configuration.controller;

import id.sajiin.sajiinservices.configuration.model.mapper.BizparMapper;
import id.sajiin.sajiinservices.configuration.model.request.CreateBizparRequestDto;
import id.sajiin.sajiinservices.configuration.model.request.GetBizparRequestDto;
import id.sajiin.sajiinservices.configuration.model.request.ListBizparRequest;
import id.sajiin.sajiinservices.configuration.model.request.UpdateBizparRequestDto;
import id.sajiin.sajiinservices.configuration.presentation.request.CreateBizparRequest;
import id.sajiin.sajiinservices.configuration.presentation.response.CreateBizparResponse;
import id.sajiin.sajiinservices.configuration.presentation.response.GetBizparResponse;
import id.sajiin.sajiinservices.configuration.presentation.response.GetListBizparResponse;
import id.sajiin.sajiinservices.configuration.service.CreateBizparService;
import id.sajiin.sajiinservices.configuration.service.GetBizparService;
import id.sajiin.sajiinservices.configuration.service.ListBizparService;
import id.sajiin.sajiinservices.configuration.service.UpdateBizparService;
import id.sajiin.sajiinservices.security.UserContext;
import id.sajiin.sajiinservices.security.UserContextHolder;
import id.sajiin.sajiinservices.shared.constant.MessageConstant;
import id.sajiin.sajiinservices.shared.core.PageEntityRequest;
import id.sajiin.sajiinservices.shared.presentation.ErrorResponse;
import id.sajiin.sajiinservices.shared.presentation.PaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bizpars")
@RequiredArgsConstructor
public class BizparController {

    private final ListBizparService listBizparService;
    private final GetBizparService getBizparService;
    private final CreateBizparService createBizparService;
    private final UpdateBizparService updateBizparService;
    private final BizparMapper bizparMapper;

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
        
        GetListBizparResponse response = new GetListBizparResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setPagination(new PaginationResponse(serviceResponse.getCurrentPage(), serviceResponse.getPageSize(), serviceResponse.getTotalRecord(), serviceResponse.getTotalPage()));
        response.setData(serviceResponse.getBizpars().stream().map(bizparMapper::toResponse).toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get Bizpar by ID Endpoint",
            summary = "Retrieve a single bizpar entry by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = GetBizparResponse.class))),
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
    public ResponseEntity<GetBizparResponse> getBizparById(@PathVariable Long id) {
        UserContext userContext = UserContextHolder.get();
        GetBizparRequestDto serviceRequest = new GetBizparRequestDto();
        serviceRequest.setUserId(userContext.getUserId());
        serviceRequest.setId(id);

        var serviceResponse = getBizparService.execute(serviceRequest);
        
        GetBizparResponse response = new GetBizparResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setData(bizparMapper.toResponse(serviceResponse.getBizpar()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Create Bizpar Endpoint",
            summary = "Create a new bizpar entry",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Success",
                            content = @Content(schema = @Schema(implementation = CreateBizparResponse.class))),
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
    public ResponseEntity<CreateBizparResponse> createBizpar(@RequestBody @Validated CreateBizparRequest request) {
        UserContext userContext = UserContextHolder.get();

        CreateBizparRequestDto requestDto = new CreateBizparRequestDto();
        requestDto.setUserId(userContext.getUserId());
        requestDto.setKey(request.getKey());
        requestDto.setValue(request.getValue());
        requestDto.setDescription(request.getDescription());

        var serviceResponse = createBizparService.execute(requestDto);
        
        CreateBizparResponse response = new CreateBizparResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.CREATED.value()));
        response.setData(bizparMapper.toResponse(serviceResponse.getBizpar()));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Update Bizpar Endpoint",
            summary = "Update a bizpar entry",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Success",
                            content = @Content(schema = @Schema(implementation = CreateBizparResponse.class))),
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
    public ResponseEntity<CreateBizparResponse> updateBizpar(@PathVariable Long id, @RequestBody @Validated CreateBizparRequest request) {
        UserContext userContext = UserContextHolder.get();

        UpdateBizparRequestDto requestDto = new UpdateBizparRequestDto();
        requestDto.setUserId(userContext.getUserId());
        requestDto.setId(id);
        requestDto.setKey(request.getKey());
        requestDto.setValue(request.getValue());
        requestDto.setDescription(request.getDescription());

        var serviceResponse = updateBizparService.execute(requestDto);
        
        CreateBizparResponse response = new CreateBizparResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setData(bizparMapper.toResponse(serviceResponse.getBizpar()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
