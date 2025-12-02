package com.pdfeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API response wrapper for consistent response structure.
 *
 * @param <T> type of the response data
 * @author PDF Editor Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    public boolean success;
    public String message;
    public T data;

    /**
     * Creates a successful response with data.
     *
     * @param message success message
     * @param data response data
     * @param <T> type of the response data
     * @return ApiResponse instance
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.message = message;
        response.data = data;
        return response;
    }

    /**
     * Creates an error response.
     *
     * @param message error message
     * @param <T> type of the response data
     * @return ApiResponse instance
     */
    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        response.data = null;
        return response;
    }
}
