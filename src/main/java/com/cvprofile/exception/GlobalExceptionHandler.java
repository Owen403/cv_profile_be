package com.cvprofile.exception;

import com.cvprofile.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  @ResponseBody
  public ResponseEntity<ApiResponse<Object>> handleApiException(
      ApiException ex, HttpServletRequest request) {
    ErrorCode errorCode = ex.getErrorCode();
    ApiResponse<Object> response =
        ApiResponse.error(
            ex.getDetailedMessage(),
            errorCode.getStatusCode().value(),
            errorCode.getErrorCode(),
            request.getRequestURI());
    return new ResponseEntity<>(response, errorCode.getStatusCode());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    
    ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
        .success(false)
        .message("Validation failed")
        .data(errors)
        .statusCode(ErrorCode.VALIDATION_ERROR.getStatusCode().value())
        .errorCode(ErrorCode.VALIDATION_ERROR.getErrorCode())
        .path(request.getRequestURI())
        .build();
    
    return new ResponseEntity<>(response, ErrorCode.VALIDATION_ERROR.getStatusCode());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseBody
  public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolationException(
      ConstraintViolationException ex, HttpServletRequest request) {
    Map<String, String> errors = ex.getConstraintViolations().stream()
        .collect(Collectors.toMap(
            violation -> violation.getPropertyPath().toString(),
            ConstraintViolation::getMessage
        ));
    
    ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
        .success(false)
        .message("Constraint validation failed")
        .data(errors)
        .statusCode(ErrorCode.VALIDATION_ERROR.getStatusCode().value())
        .errorCode(ErrorCode.VALIDATION_ERROR.getErrorCode())
        .path(request.getRequestURI())
        .build();
    
    return new ResponseEntity<>(response, ErrorCode.VALIDATION_ERROR.getStatusCode());
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseBody
  public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(
      BadCredentialsException ex, HttpServletRequest request) {
    ApiResponse<Object> response =
        ApiResponse.error(
            "Invalid username/email or password",
            ErrorCode.INVALID_CREDENTIALS.getStatusCode().value(),
            ErrorCode.INVALID_CREDENTIALS.getErrorCode(),
            request.getRequestURI());
    return new ResponseEntity<>(response, ErrorCode.INVALID_CREDENTIALS.getStatusCode());
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseBody
  public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(
      AuthenticationException ex, HttpServletRequest request) {
    ApiResponse<Object> response =
        ApiResponse.error(
            "Authentication failed: " + ex.getMessage(),
            ErrorCode.UNAUTHORIZED.getStatusCode().value(),
            ErrorCode.UNAUTHORIZED.getErrorCode(),
            request.getRequestURI());
    return new ResponseEntity<>(response, ErrorCode.UNAUTHORIZED.getStatusCode());
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseBody
  public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
      AccessDeniedException ex, HttpServletRequest request) {
    ApiResponse<Object> response =
        ApiResponse.error(
            "Access denied: " + ex.getMessage(),
            ErrorCode.FORBIDDEN.getStatusCode().value(),
            ErrorCode.FORBIDDEN.getErrorCode(),
            request.getRequestURI());
    return new ResponseEntity<>(response, ErrorCode.FORBIDDEN.getStatusCode());
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResponseEntity<ApiResponse<Object>> handleGeneralException(
      Exception ex, HttpServletRequest request) {
    ApiResponse<Object> response =
        ApiResponse.error(
            "Unexpected error: " + ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ErrorCode.INTERNAL_ERROR.getErrorCode(),
            request.getRequestURI());
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
