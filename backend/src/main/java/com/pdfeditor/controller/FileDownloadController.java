package com.pdfeditor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * REST controller for downloading processed PDF files.
 *
 * @author PDF Editor Team
 */
@RestController
@RequestMapping("/download")
public class FileDownloadController {

    private static final Logger logger = LoggerFactory.getLogger(FileDownloadController.class);

    @Value("${app.output.dir}")
    private String outputDir;

    /**
     * Downloads a processed file by filename.
     *
     * GET /api/download/{filename}
     *
     * @param filename name of the file to download
     * @return file as downloadable resource
     */
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        logger.info("Download request for file: {}", filename);

        try {
            // Security: Prevent path traversal attacks
            Path outputPath = Paths.get(outputDir).toAbsolutePath().normalize();
            Path filePath = outputPath.resolve(filename).normalize();

            // Verify the resolved path is within the output directory
            if (!filePath.startsWith(outputPath)) {
                logger.warn("Path traversal attempt detected: {}", filename);
                return ResponseEntity.badRequest().build();
            }

            File file = filePath.toFile();

            if (!file.exists() || !file.isFile()) {
                logger.warn("File not found: {}", filename);
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);

            // Determine content type
            String contentType = determineContentType(filename);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                           "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);

        } catch (Exception e) {
            logger.error("Error downloading file: {}", filename, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Determines content type based on file extension.
     *
     * @param filename file name
     * @return MIME type string
     */
    private String determineContentType(String filename) {
        String lowerFilename = filename.toLowerCase();

        if (lowerFilename.endsWith(".pdf")) {
            return "application/pdf";
        } else if (lowerFilename.endsWith(".png")) {
            return "image/png";
        } else if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerFilename.endsWith(".tiff") || lowerFilename.endsWith(".tif")) {
            return "image/tiff";
        } else {
            return "application/octet-stream";
        }
    }
}
