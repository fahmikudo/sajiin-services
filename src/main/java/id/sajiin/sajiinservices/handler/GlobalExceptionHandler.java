package id.sajiin.sajiinservices.handler;

import id.sajiin.sajiinservices.shared.constant.MessageConstant;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import id.sajiin.sajiinservices.shared.presentation.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class, RuntimeException.class, GeneralException.class})
    public ResponseEntity<@NonNull ErrorResponse> defaultErrorHandler(HttpServletRequest req, Exception e) {
        String errors = e.getLocalizedMessage();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setSuccess(false);
        errorResponse.setMessage(MessageConstant.messageCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        errorResponse.setErrors(Collections.singletonList(errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<@NonNull ErrorResponse> restException(HttpServletRequest req, ResponseStatusException e) {
        String errors = e.getReason();
        HttpStatus httpStatus = HttpStatus.valueOf(e.getStatusCode().value());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setSuccess(false);
        errorResponse.setMessage(MessageConstant.messageCode(httpStatus.value()));
        errorResponse.setErrors(Collections.singletonList(errors));
        return new ResponseEntity<>(errorResponse, e.getStatusCode());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ErrorResponse> methodArgumentException(MethodArgumentNotValidException ex) {
        List<String> errorList = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setSuccess(false);
        errorResponse.setMessage(MessageConstant.messageCode(HttpStatus.BAD_REQUEST.value()));
        errorResponse.setErrors(errorList);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
