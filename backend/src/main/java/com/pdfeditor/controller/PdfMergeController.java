package com.pdfeditor.controller;

import com.pdfeditor.dto.ApiResponse;
import com.pdfeditor.dto.FileResponse;
import com.pdfeditor.service.PdfMergeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller for PDF merge operations.
 * Provides endpoint to merge multiple PDF files into one.
 *
 * @author PDF Editor Team
 */
@RestController
@RequestMapping("/merge")
public class PdfMergeController {

    private static final Logger logger = LoggerFactory.getLogger(PdfMergeController.class);

    @Autowired
    private PdfMergeService pdfMergeService;

    /**
     * Merges multiple PDF files into a single PDF.
     *
     * POST /api/merge
     *
     * @param files list of PDF files to merge
     * @param outputFileName optional output filename
     * @return ApiResponse containing merged PDF information
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FileResponse>> mergePdfs(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "outputFileName", required = false) String outputFileName) {

        logger.info("Received merge request for {} files", files.size());

        FileResponse response = pdfMergeService.mergePdfs(files, outputFileName);

        return ResponseEntity.ok(ApiResponse.success("PDF files merged successfully", response));
    }
}
