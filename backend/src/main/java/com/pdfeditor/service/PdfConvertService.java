package com.pdfeditor.service;

import com.pdfeditor.dto.ConvertRequest;
import com.pdfeditor.dto.FileResponse;
import com.pdfeditor.exception.PdfProcessingException;
import com.pdftools.pdf.Document;
import com.pdftools.pdf2image.Converter;
import com.pdftools.pdf2image.profiles.Profile;
import com.pdftools.pdf2image.profiles.Viewing;
import com.pdftools.pdf2image.profiles.Archive;
import com.pdftools.sys.FileStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service for converting PDF files to image formats (PNG, JPEG, TIFF).
 * Uses PDF Tools SDK Converter for rendering pages to images.
 *
 * @author PDF Editor Team
 */
@Service
public class PdfConvertService {

    private static final Logger logger = LoggerFactory.getLogger(PdfConvertService.class);

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.output.dir}")
    private String outputDir;

    /**
     * Converts PDF pages to image format.
     *
     * @param file the PDF file to convert
     * @param convertRequest conversion configuration
     * @return list of FileResponse for each converted image
     * @throws PdfProcessingException if conversion fails
     */
    public List<FileResponse> convertPdfToImage(MultipartFile file, ConvertRequest convertRequest) {
        logger.info("Starting PDF to image conversion: format={}, dpi={}",
                    convertRequest.imageFormat, convertRequest.dpi);

        if (file == null || file.isEmpty()) {
            throw new PdfProcessingException("No file provided for conversion");
        }

        File tempFile = null;
        List<FileResponse> responses = new ArrayList<>();

        try {
            // Save uploaded file
            tempFile = saveUploadedFile(file);

            // Open source document
            FileStream inputStream = new FileStream(tempFile.getAbsolutePath(), FileStream.Mode.READ_ONLY);
            Document sourceDoc = Document.open(inputStream, null);
            int totalPages = sourceDoc.getPageCount();

            logger.info("Converting PDF with {} pages", totalPages);

            // Determine pages to convert
            List<Integer> pagesToConvert = parsePageNumbers(convertRequest.pages, totalPages);

            // Create converter
            Converter converter = new Converter();

            // Convert each page
            for (int pageNum : pagesToConvert) {
                String outputFileName = generateImageFileName(
                    convertRequest.outputFileNameBase,
                    pageNum,
                    totalPages,
                    convertRequest.imageFormat
                );

                File outputFile = new File(outputDir, outputFileName);

                // Render page to image
                renderPageToImage(converter, sourceDoc, pageNum, outputFile, convertRequest);

                responses.add(createFileResponse(outputFile));
                logger.info("Converted page {} to {}", pageNum, outputFileName);
            }

            sourceDoc.close();
            inputStream.close();

            logger.info("PDF to image conversion completed: {} pages converted", responses.size());

            return responses;

        } catch (Exception e) {
            logger.error("Error converting PDF to image", e);
            throw new PdfProcessingException("Failed to convert PDF to image", e);
        } finally {
            // Clean up temp file
            if (tempFile != null && tempFile.exists()) {
                try {
                    Files.delete(tempFile.toPath());
                } catch (IOException e) {
                    logger.warn("Failed to delete temp file", e);
                }
            }
        }
    }

    /**
     * Renders a specific page to an image file.
     *
     * @param converter PDF converter
     * @param sourceDoc source PDF document
     * @param pageNum page number to render
     * @param outputFile output image file
     * @param request conversion request
     */
    private void renderPageToImage(Converter converter, Document sourceDoc, int pageNum, File outputFile,
                                   ConvertRequest request) throws Exception {
        // Create profile based on format and settings
        Profile profile = createConversionProfile(request);

        // Create output stream for the image file
        FileStream outputStream = new FileStream(outputFile.getAbsolutePath(), FileStream.Mode.READ_WRITE_NEW);

        try {
            // Convert single page to image using PDF Tools SDK pdf2image Converter
            // convertPage takes: Document, output stream, profile, page number (1-based)
            converter.convertPage(sourceDoc, outputStream, profile, pageNum);

            logger.debug("Successfully converted page {} to {}", pageNum, outputFile.getName());
        } finally {
            outputStream.close();
        }
    }

    /**
     * Creates conversion profile based on request settings.
     *
     * @param request conversion request
     * @return Profile instance
     */
    private Profile createConversionProfile(ConvertRequest request) {
        // Use Viewing profile for web-suitable images (PNG, JPEG)
        // Archive profile is better for high-quality TIFF
        if ("tiff".equalsIgnoreCase(request.imageFormat) || "tif".equalsIgnoreCase(request.imageFormat)) {
            return new Archive();
        } else {
            Viewing profile = new Viewing();
            // DPI setting can be configured if the profile supports it
            // For now, use default profile settings
            return profile;
        }
    }

    /**
     * Parses page numbers from string format.
     * Supports: "1,3,5" or "1-5" or null (all pages)
     *
     * @param pagesStr page numbers string
     * @param totalPages total pages in document
     * @return list of page numbers
     */
    private List<Integer> parsePageNumbers(String pagesStr, int totalPages) {
        List<Integer> pages = new ArrayList<>();

        if (pagesStr == null || pagesStr.trim().isEmpty()) {
            // Convert all pages
            for (int i = 1; i <= totalPages; i++) {
                pages.add(i);
            }
            return pages;
        }

        // Parse page specification
        String[] parts = pagesStr.split(",");
        for (String part : parts) {
            part = part.trim();

            if (part.contains("-")) {
                // Range format: "1-5"
                String[] range = part.split("-");
                int start = Integer.parseInt(range[0].trim());
                int end = Integer.parseInt(range[1].trim());
                for (int i = start; i <= end && i <= totalPages; i++) {
                    pages.add(i);
                }
            } else {
                // Single page: "3"
                int page = Integer.parseInt(part);
                if (page >= 1 && page <= totalPages) {
                    pages.add(page);
                }
            }
        }

        return pages;
    }

    /**
     * Generates output filename for image.
     *
     * @param baseFileName base name
     * @param pageNum page number
     * @param totalPages total pages
     * @param format image format
     * @return generated filename
     */
    private String generateImageFileName(String baseFileName, int pageNum, int totalPages, String format) {
        if (baseFileName == null || baseFileName.isEmpty()) {
            baseFileName = "converted_" + UUID.randomUUID().toString();
        }

        if (format == null) {
            format = "png";
        }

        // Add leading zeros for page numbers
        int digits = String.valueOf(totalPages).length();
        String pageNumStr = String.format("%0" + digits + "d", pageNum);

        return String.format("%s_page_%s.%s", baseFileName, pageNumStr, format.toLowerCase());
    }

    /**
     * Creates FileResponse from output file.
     *
     * @param file output file
     * @return FileResponse
     */
    private FileResponse createFileResponse(File file) {
        FileResponse response = new FileResponse();
        response.fileName = file.getName();
        response.filePath = file.getAbsolutePath();
        response.fileSize = file.length();
        response.downloadUrl = "/download/" + file.getName();
        return response;
    }

    /**
     * Saves uploaded file to upload directory.
     *
     * @param file uploaded file
     * @return saved file
     */
    private File saveUploadedFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        return filePath.toFile();
    }
}
