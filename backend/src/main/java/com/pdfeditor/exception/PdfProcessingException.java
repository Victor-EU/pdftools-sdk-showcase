package com.pdfeditor.exception;

/**
 * Custom exception for PDF processing errors.
 * Thrown when PDF operations fail during execution.
 *
 * @author PDF Editor Team
 */
public class PdfProcessingException extends RuntimeException {

    public PdfProcessingException(String message) {
        super(message);
    }

    public PdfProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
