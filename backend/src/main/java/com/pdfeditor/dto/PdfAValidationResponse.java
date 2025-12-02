package com.pdfeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Response DTO containing PDF/A validation results.
 * Provides detailed conformance information and any validation errors.
 *
 * @author PDF Editor Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PdfAValidationResponse {

    /** Whether the document is PDF/A compliant */
    public boolean isCompliant;

    /** Detected PDF/A conformance level (e.g., "PDF/A-1a", "PDF/A-2b", "PDF/A-3u") */
    public String conformanceLevel;

    /** Part of PDF/A standard (1, 2, or 3) */
    public Integer pdfaPart;

    /** PDF/A conformance level letter (a, b, or u) */
    public String pdfaLevel;

    /** Number of validation errors */
    public int errorCount;

    /** Number of validation warnings */
    public int warningCount;

    /** List of validation errors */
    public List<ValidationIssue> errors;

    /** List of validation warnings */
    public List<ValidationIssue> warnings;

    /** Summary of the validation */
    public String summary;

    /**
     * Represents a validation issue (error or warning).
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationIssue {
        /** Error/warning code */
        public String code;

        /** Human-readable message */
        public String message;

        /** Severity level */
        public String severity;

        /** Page number where issue was found (if applicable) */
        public Integer pageNumber;

        /** Object type that caused the issue */
        public String objectType;

        /** Detailed context information */
        public String context;
    }
}
