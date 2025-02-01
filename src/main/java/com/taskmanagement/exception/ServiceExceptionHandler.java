package com.taskmanagement.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.taskmanagement.dto.ApiResponse;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ServiceExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ApiResponse<?> handleResourceNotFoundException(ResourceNotFoundException e) {
    return ApiResponse.notFound(null, e.getMessage());
  }

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiResponse<?> handleValidationException(ValidationException e) {
    return ApiResponse.badRequest(null, e.getMessage());
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public ApiResponse<?> handleAuthenticationException(AuthenticationException e) {
    return ApiResponse.unauthorized(null, e.getMessage());
  }

  @ExceptionHandler(IllegalAccessException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiResponse<?> handleIllegalAccessException(IllegalAccessException e) {
    log.error("IllegalAccessException: {}", e.getMessage());
    return ApiResponse.badRequest(null, "Request failed");
  }

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  public ApiResponse<?> handleException(MethodArgumentNotValidException exception) {

    BindingResult result = exception.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();
    String errorMessage = "";

    if (exception.getCause() instanceof InvalidFormatException) {
      errorMessage = "error, Invalid date format. Use dd-MM-yyyy";
    }
    else {
      errorMessage =
              fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
    }
    return ApiResponse.badRequest(errorMessage, "Please provide the required fields");
  }

  @ExceptionHandler(value = IncorrectResultSizeDataAccessException.class)
  public ApiResponse<?> handleException(IncorrectResultSizeDataAccessException exception) {
    log.info("Request failed due to duplicate records: {}", exception.getMessage());
    return ApiResponse.badRequest("Request failed due to duplicate records", "Request failed");
  }

  @ExceptionHandler(ForbiddenException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public ApiResponse<?> handleForbiddenException(ForbiddenException e) {
    return ApiResponse.forbidden(null, e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ApiResponse<?> handleException(Exception e) {
    log.error("Error occurred", e);
    return ApiResponse.error(null, "Something went wrong, please try again");
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public ApiResponse<?> handleAuthorizationDeniedException(Exception e) {
    log.error("Authorization exception", e);
    return ApiResponse.forbidden(null, "Unauthorized to perform this operation");
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ResponseBody
  public ApiResponse<?> handleHttpRequestMethodNotSupportedException(
          HttpRequestMethodNotSupportedException e) {
    return ApiResponse.badRequest(null, e.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiResponse<?> handleIllegalArgumentException(IllegalArgumentException e) {
    log.error("IllegalArgumentException: {}", e.getMessage());
    return ApiResponse.badRequest(null, "Bad request");
  }

  @ExceptionHandler(UnrecognizedPropertyException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiResponse<?> handleUnrecognizedPropertyException(UnrecognizedPropertyException e) {
    log.error("UnrecognizedPropertyException while parsing JSON object: {}", e.getMessage());
    return ApiResponse.badRequest(null, e.getMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiResponse<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    log.error("HttpMessageNotReadableException: {}", e.getMessage());
    return ApiResponse.badRequest(null, "Bad request");
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiResponse<?> handleBadRequestException(BadRequestException ex) {
    log.error("Bad request: {}", ex.getMessage());
    return ApiResponse.badRequest(null, ex.getMessage());
  }
}
