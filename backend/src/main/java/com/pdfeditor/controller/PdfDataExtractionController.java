package com.pdfeditor.controller;

import com.pdfeditor.dto.ApiResponse;
import com.pdfeditor.dto.DataExtractionResponse;
import com.pdfeditor.service.PdfDataExtractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for PDF data extraction operations.
 * Provides endpoint to extract text and content from PDF files.
 *
 * @author PDF Editor Team
 */
@RestController
@RequestMapping("/extract")
public class PdfDataExtractionController {

    private static final Logger logger = LoggerFactory.getLogger(PdfDataExtractionController.class);

    @Autowired
    private PdfDataExtractionService pdfDataExtractionService;

    /**
     * Extracts text and data from a PDF file.
     *
     * POST /api/extract
     *
     * @param file PDF file to extract data from
     * @param extractImages whether to extract images (default: false)
     * @param pages specific pages to extract (e.g., "1-5,8,10-12")
     * @return ApiResponse containing extracted data
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DataExtractionResponse>> extractData(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "extractImages", defaultValue = "false") boolean extractImages,
            @RequestParam(value = "pages", required = false) String pages) {

        logger.info("Received data extraction request for: {}, extractImages: {}, pages: {}",
                   file.getOriginalFilename(), extractImages, pages);

        DataExtractionResponse response = pdfDataExtractionService.extractData(file, extractImages, pages);

        String message = String.format("Data extracted successfully (%d words from %d pages)",
                                       response.wordCount, response.pages.size());

        return ResponseEntity.ok(ApiResponse.success(message, response));
    }
}
