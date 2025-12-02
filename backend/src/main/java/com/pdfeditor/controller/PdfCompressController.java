package com.pdfeditor.controller;

import com.pdfeditor.dto.ApiResponse;
import com.pdfeditor.dto.CompressRequest;
import com.pdfeditor.dto.FileResponse;
import com.pdfeditor.service.PdfCompressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for PDF compression operations.
 * Provides endpoint to compress PDF files to reduce size.
 *
 * @author PDF Editor Team
 */
@RestController
@RequestMapping("/compress")
public class PdfCompressController {

    private static final Logger logger = LoggerFactory.getLogger(PdfCompressController.class);

    @Autowired
    private PdfCompressService pdfCompressService;

    /**
     * Compresses a PDF file to reduce its size.
     *
     * POST /api/compress
     *
     * @param file PDF file to compress
     * @param compressionProfile compression profile ("web", "print", "custom")
     * @param imageQuality image quality for custom compression (1-100)
     * @param outputFileName optional output filename
     * @return ApiResponse containing compressed PDF information
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FileResponse>> compressPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "compressionProfile", defaultValue = "web") String compressionProfile,
            @RequestParam(value = "imageQuality", required = false) Integer imageQuality,
            @RequestParam(value = "outputFileName", required = false) String outputFileName) {

        logger.info("Received compress request: profile={}", compressionProfile);

        CompressRequest compressRequest = new CompressRequest();
        compressRequest.compressionProfile = compressionProfile;
        compressRequest.imageQuality = imageQuality;
        compressRequest.outputFileName = outputFileName;

        FileResponse response = pdfCompressService.compressPdf(file, compressRequest);

        Double ratio = response.compressionRatio;
        String message = ratio != null
            ? String.format("PDF compressed successfully (%.2f%% size reduction)", ratio)
            : "PDF compressed successfully";

        return ResponseEntity.ok(ApiResponse.success(message, response));
    }
}
