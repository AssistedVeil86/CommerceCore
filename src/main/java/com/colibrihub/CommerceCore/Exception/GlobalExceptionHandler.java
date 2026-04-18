package com.colibrihub.CommerceCore.Exception;

import com.colibrihub.CommerceCore.dto.Response.ApiResponse;
import com.colibrihub.CommerceCore.dto.Response.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest req) {
        List<ValidationError> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();

            var errorDto = new ValidationError(fieldName, message);
            errors.add(errorDto);
        });

        var response = ApiResponse.error("Error de Validación: Revisa los campos indicados");
        response.setData(errors);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest req) {
        var response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidOperationException(
            InvalidOperationException ex, WebRequest req) {
        var response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(SyncException.class)
    public ResponseEntity<ApiResponse<Object>> handleSyncException(SyncException ex, WebRequest req) {
        var response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnAuthorizedException(UnAuthorizedException ex){
        var response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleWebClientRequestException(WebClientRequestException ex){
        var response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex){
        var response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
