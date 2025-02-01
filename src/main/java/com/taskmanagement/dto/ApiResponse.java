package com.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ApiResponse<T> {
    @JsonProperty
    int code;
    @JsonProperty
    boolean success;
    @JsonProperty
    String message;
    @JsonProperty
    T body;


    public static <T> ApiResponse<T> ok(T body) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.code = 200;
        response.success = true;
        response.message = "Success";
        response.body = body;
        return response;
    }

    public static <T> ApiResponse<T> badRequest(T body, String message) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.code = 400;
        response.success = false;
        response.message = message;
        response.body = body;
        return response;
    }

    public static <T> ApiResponse<T> error(T body, String message) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.code = 500;
        response.success = false;
        response.message = message;
        response.body = body;
        return response;
    }

    public static <T> ApiResponse<T> notFound(T body, String message) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.code = 404;
        response.success = false;
        response.message = message;
        response.body = body;
        return response;
    }

    public static <T> ApiResponse<T> unauthorized(T body, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 401;
        response.success = false;
        response.message = message;
        response.body = body;
        return response;
    }

    public static <T> ApiResponse<T> forbidden(T body, String message) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.code = 403;
        response.success = false;
        response.message = message;
        response.body = body;
        return response;
    }

    public static <T> ApiResponse<T> conflict(T body, String message) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.code = 409;
        response.success = false;
        response.message = message;
        response.body = body;
        return response;
    }
}
