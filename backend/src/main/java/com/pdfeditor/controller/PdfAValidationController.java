package com.pdfeditor.controller;

import com.pdfeditor.dto.ApiResponse;
import com.pdfeditor.dto.PdfAValidationResponse;
import com.pdfeditor.service.PdfAValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for PDF/A validation operations.
 * Provides endpoint to validate PDF/A conformance.
 *
 * @author PDF Editor Team
 */
@RestController
@RequestMapping("/validate-pdfa")
public class PdfAValidationController {

    private static final Logger logger = LoggerFactory.getLogger(PdfAValidationController.class);

    @Autowired
    private PdfAValidationService pdfAValidationService;

    /**
     * Validates a PDF file for PDF/A conformance.
     *
     * POST /api/validate-pdfa
     *
     * @param file PDF file to validate
     * @param conformanceLevel optional target conformance level to validate against
     * @return ApiResponse containing validation results
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PdfAValidationResponse>> validatePdfA(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "conformanceLevel", required = false) String conformanceLevel) {

        logger.info("Received PDF/A validation request for: {}, target level: {}",
                   file.getOriginalFilename(), conformanceLevel);

        PdfAValidationResponse response = pdfAValidationService.validatePdfA(file, conformanceLevel);

        String message = response.isCompliant
            ? "Document is PDF/A compliant"
            : String.format("Document is NOT PDF/A compliant (%d errors, %d warnings)",
                           response.errorCount, response.warningCount);

        return ResponseEntity.ok(ApiResponse.success(message, response));
    }
}
