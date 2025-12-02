package com.pdfeditor.controller;

import com.pdfeditor.dto.ApiResponse;
import com.pdfeditor.dto.FileResponse;
import com.pdfeditor.dto.PdfAConversionRequest;
import com.pdfeditor.service.PdfAConversionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for PDF/A conversion operations.
 * Provides endpoint to convert PDF files to PDF/A format.
 *
 * @author PDF Editor Team
 */
@RestController
@RequestMapping("/convert-pdfa")
public class PdfAConversionController {

    private static final Logger logger = LoggerFactory.getLogger(PdfAConversionController.class);

    @Autowired
    private PdfAConversionService pdfAConversionService;

    /**
     * Converts a PDF file to PDF/A format.
     *
     * POST /api/convert-pdfa
     *
     * @param file PDF file to convert
     * @param conformanceLevel target PDF/A conformance level (e.g., "1a", "2b", "3u")
     * @param outputFileName optional output filename
     * @param copyMetadata whether to copy metadata from source (default: true)
     * @param embedFonts whether to embed fonts (default: true)
     * @return ApiResponse containing converted PDF/A file information
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FileResponse>> convertToPdfA(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "conformanceLevel", defaultValue = "2b") String conformanceLevel,
            @RequestParam(value = "outputFileName", required = false) String outputFileName,
            @RequestParam(value = "copyMetadata", defaultValue = "true") boolean copyMetadata,
            @RequestParam(value = "embedFonts", defaultValue = "true") boolean embedFonts) {

        logger.info("Received PDF/A conversion request for: {}, target level: {}",
                   file.getOriginalFilename(), conformanceLevel);

        PdfAConversionRequest request = new PdfAConversionRequest();
        request.conformanceLevel = conformanceLevel;
        request.outputFileName = outputFileName;
        request.copyMetadata = copyMetadata;
        request.embedFonts = embedFonts;

        FileResponse response = pdfAConversionService.convertToPdfA(file, request);

        String message = String.format("PDF converted to PDF/A-%s successfully", conformanceLevel.toUpperCase());

        return ResponseEntity.ok(ApiResponse.success(message, response));
    }
}
