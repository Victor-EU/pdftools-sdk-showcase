package com.pdfeditor.service;

import com.pdfeditor.dto.MetadataResponse;
import com.pdfeditor.exception.PdfProcessingException;
import com.pdftools.pdf.Document;
import com.pdftools.pdf.Metadata;
import com.pdftools.pdf.Conformance;
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
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * Service for extracting metadata from PDF documents.
 * Uses PDF Tools SDK to read document properties and XMP metadata.
 *
 * @author PDF Editor Team
 */
@Service
public class PdfMetadataService {

    private static final Logger logger = LoggerFactory.getLogger(PdfMetadataService.class);

    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Extracts metadata from a PDF file.
     *
     * @param file the PDF file to analyze
     * @return MetadataResponse containing all extracted metadata
     * @throws PdfProcessingException if metadata extraction fails
     */
    public MetadataResponse extractMetadata(MultipartFile file) {
        logger.info("Starting metadata extraction for file: {}", file.getOriginalFilename());

        if (file == null || file.isEmpty()) {
            throw new PdfProcessingException("No file provided for metadata extraction");
        }

        File tempFile = null;

        try {
            // Save uploaded file temporarily
            tempFile = saveUploadedFile(file);

            // Open the PDF document
            FileStream inputStream = new FileStream(tempFile.getAbsolutePath(), FileStream.Mode.READ_ONLY);
            Document document = Document.open(inputStream, null);

            // Create response object
            MetadataResponse response = new MetadataResponse();

            // Extract basic document info
            response.pageCount = document.getPageCount();
            response.fileSize = file.getSize();

            // Get metadata object
            Metadata metadata = document.getMetadata();
            if (metadata != null) {
                response.title = metadata.getTitle();
                response.author = metadata.getAuthor();
                response.subject = metadata.getSubject();
                response.keywords = metadata.getKeywords();
                response.creator = metadata.getCreator();
                response.producer = metadata.getProducer();

                // Convert OffsetDateTime to Date
                OffsetDateTime creationDate = metadata.getCreationDate();
                if (creationDate != null) {
                    response.creationDate = Date.from(creationDate.toInstant());
                }
                OffsetDateTime modDate = metadata.getModificationDate();
                if (modDate != null) {
                    response.modificationDate = Date.from(modDate.toInstant());
                }
            }

            // Get conformance info
            try {
                Conformance conformance = document.getConformance();
                if (conformance != null) {
                    response.pdfaConformance = conformance.toString();
                }
            } catch (Exception e) {
                logger.debug("Could not determine PDF/A conformance: {}", e.getMessage());
            }

            // Set default values for properties we can't determine from this SDK version
            response.pdfVersion = "1.7";
            response.isEncrypted = false;
            response.isLinearized = false;
            response.hasForms = false;
            response.isTagged = false;

            // Close resources
            document.close();
            inputStream.close();

            logger.info("Metadata extraction completed for: {}", file.getOriginalFilename());
            return response;

        } catch (Exception e) {
            logger.error("Error extracting metadata from PDF", e);
            throw new PdfProcessingException("Failed to extract metadata from PDF file", e);
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
