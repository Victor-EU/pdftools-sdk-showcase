package com.pdfeditor.exception;

import com.pdfeditor.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * Global exception handler for REST API endpoints.
 * Provides centralized exception handling and consistent error responses.
 *
 * @author PDF Editor Team
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles PDF processing exceptions.
     *
     * @param ex the exception
     * @return error response entity
     */
    @ExceptionHandler(PdfProcessingException.class)
    public ResponseEntity<ApiResponse<Void>> handlePdfProcessingException(PdfProcessingException ex) {
        logger.error("PDF processing error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("PDF processing failed: " + ex.getMessage()));
    }

    /**
     * Handles file size exceeded exceptions.
     *
     * @param ex the exception
     * @return error response entity
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        logger.error("File size exceeded: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(ApiResponse.error("File size exceeds maximum allowed size (100MB)"));
    }

    /**
     * Handles all other uncaught exceptions.
     *
     * @param ex the exception
     * @return error response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An unexpected error occurred: " + ex.getMessage()));
    }
}
