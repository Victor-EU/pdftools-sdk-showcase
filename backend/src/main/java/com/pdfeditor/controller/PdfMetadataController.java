package com.pdfeditor.controller;

import com.pdfeditor.dto.ApiResponse;
import com.pdfeditor.dto.MetadataResponse;
import com.pdfeditor.service.PdfMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for PDF metadata extraction operations.
 * Provides endpoint to extract metadata from PDF files.
 *
 * @author PDF Editor Team
 */
@RestController
@RequestMapping("/metadata")
public class PdfMetadataController {

    private static final Logger logger = LoggerFactory.getLogger(PdfMetadataController.class);

    @Autowired
    private PdfMetadataService pdfMetadataService;

    /**
     * Extracts metadata from a PDF file.
     *
     * POST /api/metadata
     *
     * @param file PDF file to extract metadata from
     * @return ApiResponse containing extracted metadata
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MetadataResponse>> extractMetadata(
            @RequestParam("file") MultipartFile file) {

        logger.info("Received metadata extraction request for: {}", file.getOriginalFilename());

        MetadataResponse response = pdfMetadataService.extractMetadata(file);

        String message = String.format("Metadata extracted successfully (%d pages)", response.pageCount);

        return ResponseEntity.ok(ApiResponse.success(message, response));
    }
}
