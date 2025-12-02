package com.pdfeditor.controller;

import com.pdfeditor.dto.ApiResponse;
import com.pdfeditor.dto.FileResponse;
import com.pdfeditor.dto.SplitRequest;
import com.pdfeditor.service.PdfSplitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller for PDF split operations.
 * Provides endpoint to split a PDF file into multiple PDFs.
 *
 * @author PDF Editor Team
 */
@RestController
@RequestMapping("/split")
public class PdfSplitController {

    private static final Logger logger = LoggerFactory.getLogger(PdfSplitController.class);

    @Autowired
    private PdfSplitService pdfSplitService;

    /**
     * Splits a PDF file based on the provided configuration.
     *
     * POST /api/split
     *
     * @param file PDF file to split
     * @param splitMode split mode ("pages" or "ranges")
     * @param splitPoints page numbers or ranges for splitting
     * @param outputFileNameBase base name for output files
     * @return ApiResponse containing list of split PDF information
     */
    @PostMapping
    public ResponseEntity<ApiResponse<List<FileResponse>>> splitPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam("splitMode") String splitMode,
            @RequestParam("splitPoints") List<String> splitPoints,
            @RequestParam(value = "outputFileNameBase", required = false) String outputFileNameBase) {

        logger.info("Received split request: mode={}, points={}", splitMode, splitPoints.size());

        SplitRequest splitRequest = new SplitRequest();
        splitRequest.splitMode = splitMode;
        splitRequest.splitPoints = splitPoints;
        splitRequest.outputFileNameBase = outputFileNameBase;
        List<FileResponse> responses = pdfSplitService.splitPdf(file, splitRequest);

        return ResponseEntity.ok(ApiResponse.success(
            String.format("PDF split into %d files successfully", responses.size()),
            responses
        ));
    }
}
