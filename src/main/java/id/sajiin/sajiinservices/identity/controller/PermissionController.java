package id.sajiin.sajiinservices.identity.controller;

import id.sajiin.sajiinservices.identity.model.mapper.PermissionMapper;
import id.sajiin.sajiinservices.identity.model.request.CreatePermissionRequestDto;
import id.sajiin.sajiinservices.identity.model.request.GetPermissionRequestDto;
import id.sajiin.sajiinservices.identity.model.request.ListPermissionRequest;
import id.sajiin.sajiinservices.identity.model.request.UpdatePermissionRequestDto;
import id.sajiin.sajiinservices.identity.presentation.request.CreatePermissionRequest;
import id.sajiin.sajiinservices.identity.presentation.response.CreatePermissionResponse;
import id.sajiin.sajiinservices.identity.presentation.response.GetListPermissionResponse;
import id.sajiin.sajiinservices.identity.presentation.response.GetPermissionResponse;
import id.sajiin.sajiinservices.identity.service.CreatePermissionService;
import id.sajiin.sajiinservices.identity.service.GetPermissionService;
import id.sajiin.sajiinservices.identity.service.ListPermissionService;
import id.sajiin.sajiinservices.identity.service.UpdatePermissionService;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Tag(name = "Permission", description = "Permission API Endpoints")
public class PermissionController {

    private final ListPermissionService listPermissionService;
    private final GetPermissionService getPermissionService;
    private final CreatePermissionService createPermissionService;
    private final UpdatePermissionService updatePermissionService;
    private final PermissionMapper permissionMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get All Permission with Pagination Endpoint",
            summary = "Retrieve a paginated list of permission entries",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = GetListPermissionResponse.class))),
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
    public ResponseEntity<GetListPermissionResponse> getAllPermissionsWithPagination(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search) {
        UserContext userContext = UserContextHolder.get();
        ListPermissionRequest serviceRequest = new ListPermissionRequest();
        serviceRequest.setUserId(userContext.getUserId());
        serviceRequest.setPaginated(true);
        serviceRequest.setPagination(new PageEntityRequest(page, size));
        serviceRequest.setSearch(search);

        var serviceResponse = listPermissionService.execute(serviceRequest);

        GetListPermissionResponse response = new GetListPermissionResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setPagination(new PaginationResponse(serviceResponse.getCurrentPage(), serviceResponse.getPageSize(), serviceResponse.getTotalRecord(), serviceResponse.getTotalPage()));
        response.setData(serviceResponse.getPermissions().stream().map(permissionMapper::toResponse).toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get Permission by ID Endpoint",
            summary = "Retrieve a single permission entry by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = GetPermissionResponse.class))),
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
    public ResponseEntity<GetPermissionResponse> getPermissionById(@PathVariable Long id) {
        UserContext userContext = UserContextHolder.get();
        GetPermissionRequestDto serviceRequest = new GetPermissionRequestDto();
        serviceRequest.setUserId(userContext.getUserId());
        serviceRequest.setId(id);

        var serviceResponse = getPermissionService.execute(serviceRequest);

        GetPermissionResponse response = new GetPermissionResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setData(permissionMapper.toResponse(serviceResponse.getPermission()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Create Permission Endpoint",
            summary = "Create a new permission entry",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Success",
                            content = @Content(schema = @Schema(implementation = CreatePermissionResponse.class))),
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
    public ResponseEntity<CreatePermissionResponse> createPermission(@RequestBody @Validated CreatePermissionRequest request) {
        UserContext userContext = UserContextHolder.get();

        CreatePermissionRequestDto requestDto = new CreatePermissionRequestDto();
        requestDto.setUserId(userContext.getUserId());
        requestDto.setPermissionId(request.getPermissionId());
        requestDto.setName(request.getName());
        requestDto.setDescription(request.getDescription());
        requestDto.setStatus(request.getStatus());

        var serviceResponse = createPermissionService.execute(requestDto);

        CreatePermissionResponse response = new CreatePermissionResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.CREATED.value()));
        response.setData(permissionMapper.toResponse(serviceResponse.getPermission()));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Update Permission Endpoint",
            summary = "Update a permission entry",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Success",
                            content = @Content(schema = @Schema(implementation = CreatePermissionResponse.class))),
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
    public ResponseEntity<CreatePermissionResponse> updatePermission(@PathVariable Long id, @RequestBody @Validated CreatePermissionRequest request) {
        UserContext userContext = UserContextHolder.get();

        UpdatePermissionRequestDto requestDto = new UpdatePermissionRequestDto();
        requestDto.setUserId(userContext.getUserId());
        requestDto.setId(id);
        requestDto.setPermissionId(request.getPermissionId());
        requestDto.setName(request.getName());
        requestDto.setDescription(request.getDescription());
        requestDto.setStatus(request.getStatus());

        var serviceResponse = updatePermissionService.execute(requestDto);

        CreatePermissionResponse response = new CreatePermissionResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setData(permissionMapper.toResponse(serviceResponse.getPermission()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
