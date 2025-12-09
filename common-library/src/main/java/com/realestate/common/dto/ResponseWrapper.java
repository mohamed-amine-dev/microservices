package com.realestate.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Generic response wrapper for consistent API responses across all microservices
 * 
 * @param <T> Type of data being returned
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper<T> {
    
    /**
     * HTTP status code
     */
    private int status;
    
    /**
     * Response message
     */
    private String message;
    
    /**
     * Actual response data
     */
    private T data;
    
    /**
     * Timestamp of the response
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * Success response with data
     */
    public static <T> ResponseWrapper<T> success(T data) {
        return ResponseWrapper.<T>builder()
                .status(200)
                .message("Success")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Success response with custom message and data
     */
    public static <T> ResponseWrapper<T> success(String message, T data) {
        return ResponseWrapper.<T>builder()
                .status(200)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Success response with only message
     */
    public static <T> ResponseWrapper<T> success(String message) {
        return ResponseWrapper.<T>builder()
                .status(200)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Error response
     */
    public static <T> ResponseWrapper<T> error(int status, String message) {
        return ResponseWrapper.<T>builder()
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
