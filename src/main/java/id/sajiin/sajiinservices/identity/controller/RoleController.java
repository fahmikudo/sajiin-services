package id.sajiin.sajiinservices.identity.controller;

import id.sajiin.sajiinservices.identity.model.mapper.RoleMapper;
import id.sajiin.sajiinservices.identity.model.request.CreateRoleRequestDto;
import id.sajiin.sajiinservices.identity.model.request.GetRoleRequestDto;
import id.sajiin.sajiinservices.identity.model.request.ListRoleRequest;
import id.sajiin.sajiinservices.identity.model.request.UpdateRoleRequestDto;
import id.sajiin.sajiinservices.identity.presentation.request.CreateRoleRequest;
import id.sajiin.sajiinservices.identity.presentation.response.CreateRoleResponse;
import id.sajiin.sajiinservices.identity.presentation.response.GetListRoleResponse;
import id.sajiin.sajiinservices.identity.presentation.response.GetRoleResponse;
import id.sajiin.sajiinservices.identity.service.CreateRoleService;
import id.sajiin.sajiinservices.identity.service.GetRoleService;
import id.sajiin.sajiinservices.identity.service.ListRoleService;
import id.sajiin.sajiinservices.identity.service.UpdateRoleService;
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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Role", description = "Role API Endpoints")
public class RoleController {

    private final ListRoleService listRoleService;
    private final GetRoleService getRoleService;
    private final CreateRoleService createRoleService;
    private final UpdateRoleService updateRoleService;
    private final RoleMapper roleMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get All Role with Pagination Endpoint",
            summary = "Retrieve a paginated list of role entries",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = GetListRoleResponse.class))),
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
    public ResponseEntity<GetListRoleResponse> getAllRolesWithPagination(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search) {
        UserContext userContext = UserContextHolder.get();
        ListRoleRequest serviceRequest = new ListRoleRequest();
        serviceRequest.setUserId(userContext.getUserId());
        serviceRequest.setPaginated(true);
        serviceRequest.setPagination(new PageEntityRequest(page, size));
        serviceRequest.setSearch(search);

        var serviceResponse = listRoleService.execute(serviceRequest);

        GetListRoleResponse response = new GetListRoleResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setPagination(new PaginationResponse(serviceResponse.getCurrentPage(), serviceResponse.getPageSize(), serviceResponse.getTotalRecord(), serviceResponse.getTotalPage()));
        response.setData(serviceResponse.getRoles().stream().map(roleMapper::toResponse).toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get Role by ID Endpoint",
            summary = "Retrieve a single role entry by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = GetRoleResponse.class))),
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
    public ResponseEntity<GetRoleResponse> getRoleById(@PathVariable Long id) {
        UserContext userContext = UserContextHolder.get();
        GetRoleRequestDto serviceRequest = new GetRoleRequestDto();
        serviceRequest.setUserId(userContext.getUserId());
        serviceRequest.setId(id);

        var serviceResponse = getRoleService.execute(serviceRequest);

        GetRoleResponse response = new GetRoleResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setData(roleMapper.toResponse(serviceResponse.getRole()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Create Role Endpoint",
            summary = "Create a new role entry",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Success",
                            content = @Content(schema = @Schema(implementation = CreateRoleResponse.class))),
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
    public ResponseEntity<CreateRoleResponse> createRole(@RequestBody @Validated CreateRoleRequest request) {
        UserContext userContext = UserContextHolder.get();

        CreateRoleRequestDto requestDto = new CreateRoleRequestDto();
        requestDto.setUserId(userContext.getUserId());
        requestDto.setRoleId(request.getRoleId());
        requestDto.setRoleName(request.getRoleName());
        requestDto.setDescription(request.getDescription());
        requestDto.setType(request.getType());
        requestDto.setStatus(request.getStatus());

        var serviceResponse = createRoleService.execute(requestDto);

        CreateRoleResponse response = new CreateRoleResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.CREATED.value()));
        response.setData(roleMapper.toResponse(serviceResponse.getRole()));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Update Role Endpoint",
            summary = "Update a role entry",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Success",
                            content = @Content(schema = @Schema(implementation = CreateRoleResponse.class))),
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
    public ResponseEntity<CreateRoleResponse> updateRole(@PathVariable Long id, @RequestBody @Validated CreateRoleRequest request) {
        UserContext userContext = UserContextHolder.get();

        UpdateRoleRequestDto requestDto = new UpdateRoleRequestDto();
        requestDto.setUserId(userContext.getUserId());
        requestDto.setId(id);
        requestDto.setRoleId(request.getRoleId());
        requestDto.setRoleName(request.getRoleName());
        requestDto.setDescription(request.getDescription());
        requestDto.setType(request.getType());
        requestDto.setStatus(request.getStatus());

        var serviceResponse = updateRoleService.execute(requestDto);

        CreateRoleResponse response = new CreateRoleResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setData(roleMapper.toResponse(serviceResponse.getRole()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
