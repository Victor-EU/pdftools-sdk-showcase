package com.pdfeditor.controller;

import com.pdfeditor.dto.ApiResponse;
import com.pdfeditor.dto.ConvertRequest;
import com.pdfeditor.dto.FileResponse;
import com.pdfeditor.service.PdfConvertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller for PDF to image conversion operations.
 * Provides endpoint to convert PDF pages to image formats.
 *
 * @author PDF Editor Team
 */
@RestController
@RequestMapping("/convert")
public class PdfConvertController {

    private static final Logger logger = LoggerFactory.getLogger(PdfConvertController.class);

    @Autowired
    private PdfConvertService pdfConvertService;

    /**
     * Converts PDF pages to image format.
     *
     * POST /api/convert
     *
     * @param file PDF file to convert
     * @param imageFormat image format ("png", "jpeg", "tiff")
     * @param dpi resolution in DPI (default 150)
     * @param pages page numbers to convert (e.g., "1,3,5" or "1-5", null for all)
     * @param outputFileNameBase base name for output files
     * @return ApiResponse containing list of converted image information
     */
    @PostMapping
    public ResponseEntity<ApiResponse<List<FileResponse>>> convertPdfToImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "imageFormat", defaultValue = "png") String imageFormat,
            @RequestParam(value = "dpi", defaultValue = "150") Integer dpi,
            @RequestParam(value = "pages", required = false) String pages,
            @RequestParam(value = "outputFileNameBase", required = false) String outputFileNameBase) {

        logger.info("Received convert request: format={}, dpi={}, pages={}", imageFormat, dpi, pages);

        ConvertRequest convertRequest = new ConvertRequest();
        convertRequest.imageFormat = imageFormat;
        convertRequest.dpi = dpi;
        convertRequest.pages = pages;
        convertRequest.outputFileNameBase = outputFileNameBase;
        List<FileResponse> responses = pdfConvertService.convertPdfToImage(file, convertRequest);

        return ResponseEntity.ok(ApiResponse.success(
            String.format("PDF converted to %d image(s) successfully", responses.size()),
            responses
        ));
    }
}
