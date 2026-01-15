package id.sajiin.sajiinservices.configuration.controller;

import id.sajiin.sajiinservices.configuration.model.dto.PaymentDto;
import id.sajiin.sajiinservices.configuration.model.mapper.PaymentMapper;
import id.sajiin.sajiinservices.configuration.model.request.CreatePaymentRequestDto;
import id.sajiin.sajiinservices.configuration.model.request.GetPaymentRequestDto;
import id.sajiin.sajiinservices.configuration.model.request.ListPaymentRequest;
import id.sajiin.sajiinservices.configuration.model.request.UpdatePaymentRequestDto;
import id.sajiin.sajiinservices.configuration.presentation.request.CreatePaymentRequest;
import id.sajiin.sajiinservices.configuration.presentation.response.CreatePaymentResponse;
import id.sajiin.sajiinservices.configuration.presentation.response.GetListPaymentResponse;
import id.sajiin.sajiinservices.configuration.presentation.response.GetPaymentResponse;
import id.sajiin.sajiinservices.configuration.service.CreatePaymentService;
import id.sajiin.sajiinservices.configuration.service.GetPaymentService;
import id.sajiin.sajiinservices.configuration.service.ListPaymentService;
import id.sajiin.sajiinservices.configuration.service.UpdatePaymentService;
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
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final ListPaymentService listPaymentService;
    private final GetPaymentService getPaymentService;
    private final CreatePaymentService createPaymentService;
    private final UpdatePaymentService updatePaymentService;
    private final PaymentMapper paymentMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get All Payment with Pagination Endpoint",
            summary = "Retrieve a paginated list of payment entries",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = GetListPaymentResponse.class))),
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
    public ResponseEntity<GetListPaymentResponse> getAllPaymentsWithPagination(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search) {
        UserContext userContext = UserContextHolder.get();
        ListPaymentRequest serviceRequest = new ListPaymentRequest();
        serviceRequest.setUserId(userContext.getUserId());
        serviceRequest.setPaginated(true);
        serviceRequest.setPagination(new PageEntityRequest(page, size));
        serviceRequest.setSearch(search);

        var serviceResponse = listPaymentService.execute(serviceRequest);

        GetListPaymentResponse response = new GetListPaymentResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setPagination(new PaginationResponse(serviceResponse.getCurrentPage(), serviceResponse.getPageSize(), serviceResponse.getTotalRecord(), serviceResponse.getTotalPage()));
        response.setData(serviceResponse.getPayments().stream().map(paymentMapper::toResponse).toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get Payment by ID Endpoint",
            summary = "Retrieve a single payment entry by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = GetPaymentResponse.class))),
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
    public ResponseEntity<GetPaymentResponse> getPaymentById(@PathVariable Long id) {
        UserContext userContext = UserContextHolder.get();
        GetPaymentRequestDto serviceRequest = new GetPaymentRequestDto();
        serviceRequest.setUserId(userContext.getUserId());
        serviceRequest.setId(id);

        var serviceResponse = getPaymentService.execute(serviceRequest);

        GetPaymentResponse response = new GetPaymentResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setData(paymentMapper.toResponse(serviceResponse.getPayment()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Create Payment Endpoint",
            summary = "Create a new payment entry",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Success",
                            content = @Content(schema = @Schema(implementation = CreatePaymentResponse.class))),
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
    public ResponseEntity<CreatePaymentResponse> createPayment(@RequestBody @Validated CreatePaymentRequest request) {
        UserContext userContext = UserContextHolder.get();

        CreatePaymentRequestDto requestDto = new CreatePaymentRequestDto();
        requestDto.setUserId(userContext.getUserId());
        requestDto.setPaymentId(request.getPaymentId());
        requestDto.setImage(request.getImage());
        requestDto.setName(request.getName());
        requestDto.setDescription(request.getDescription());
        requestDto.setStatus(request.getStatus());

        var serviceResponse = createPaymentService.execute(requestDto);

        CreatePaymentResponse response = new CreatePaymentResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.CREATED.value()));
        response.setData(paymentMapper.toResponse(serviceResponse.getPayment()));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Update Payment Endpoint",
            summary = "Update a payment entry",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Success",
                            content = @Content(schema = @Schema(implementation = CreatePaymentResponse.class))),
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
    public ResponseEntity<CreatePaymentResponse> updatePayment(@PathVariable Long id, @RequestBody @Validated CreatePaymentRequest request) {
        UserContext userContext = UserContextHolder.get();

        UpdatePaymentRequestDto requestDto = new UpdatePaymentRequestDto();
        requestDto.setUserId(userContext.getUserId());
        requestDto.setId(id);
        requestDto.setPaymentId(request.getPaymentId());
        requestDto.setImage(request.getImage());
        requestDto.setName(request.getName());
        requestDto.setDescription(request.getDescription());
        requestDto.setStatus(request.getStatus());

        var serviceResponse = updatePaymentService.execute(requestDto);

        CreatePaymentResponse response = new CreatePaymentResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setData(paymentMapper.toResponse(serviceResponse.getPayment()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
