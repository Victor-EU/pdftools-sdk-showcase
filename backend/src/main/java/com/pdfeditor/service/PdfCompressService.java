package com.pdfeditor.service;

import com.pdfeditor.dto.CompressRequest;
import com.pdfeditor.dto.FileResponse;
import com.pdfeditor.exception.PdfProcessingException;
import com.pdftools.pdf.Document;
import com.pdftools.optimization.Optimizer;
import com.pdftools.optimization.profiles.Profile;
import com.pdftools.optimization.profiles.Web;
import com.pdftools.optimization.profiles.Print;
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
import java.util.UUID;

/**
 * Service for compressing PDF files to reduce file size.
 * Uses PDF Tools SDK Optimizer with different compression profiles.
 *
 * @author PDF Editor Team
 */
@Service
public class PdfCompressService {

    private static final Logger logger = LoggerFactory.getLogger(PdfCompressService.class);

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.output.dir}")
    private String outputDir;

    /**
     * Compresses a PDF file based on the provided compression settings.
     *
     * @param file the PDF file to compress
     * @param compressRequest compression configuration
     * @return FileResponse containing compressed PDF information
     * @throws PdfProcessingException if compression fails
     */
    public FileResponse compressPdf(MultipartFile file, CompressRequest compressRequest) {
        logger.info("Starting PDF compression: profile={}", compressRequest.compressionProfile);

        if (file == null || file.isEmpty()) {
            throw new PdfProcessingException("No file provided for compression");
        }

        File tempFile = null;
        File outputFile = null;
        long originalSize = file.getSize();

        try {
            // Save uploaded file
            tempFile = saveUploadedFile(file);

            // Open source document
            FileStream inputStream = new FileStream(tempFile.getAbsolutePath(), FileStream.Mode.READ_ONLY);
            Document sourceDoc = Document.open(inputStream, null);

            // Generate output filename
            String outputFileName = compressRequest.outputFileName;
            if (outputFileName == null || outputFileName.isEmpty()) {
                outputFileName = "compressed_" + UUID.randomUUID().toString() + ".pdf";
            }
            if (!outputFileName.endsWith(".pdf")) {
                outputFileName += ".pdf";
            }

            outputFile = new File(outputDir, outputFileName);

            // Select compression profile
            Profile profile = selectCompressionProfile(compressRequest);

            // Create optimizer
            Optimizer optimizer = new Optimizer();

            // Create output stream and optimize
            FileStream outputStream = new FileStream(outputFile.getAbsolutePath(), FileStream.Mode.READ_WRITE_NEW);
            Document optimizedDoc = optimizer.optimizeDocument(sourceDoc, outputStream, profile);

            // Close resources
            optimizedDoc.close();
            outputStream.close();

            long compressedSize = outputFile.length();
            double compressionRatio = ((originalSize - compressedSize) / (double) originalSize) * 100;

            sourceDoc.close();
            inputStream.close();

            logger.info("PDF compression completed: {} -> {} bytes ({}% reduction)",
                       originalSize, compressedSize, String.format("%.2f", compressionRatio));

            // Create response
            FileResponse response = new FileResponse();
            response.fileName = outputFileName;
            response.filePath = outputFile.getAbsolutePath();
            response.fileSize = compressedSize;
            response.downloadUrl = "/download/" + outputFileName;
            response.originalSize = originalSize;
            response.compressionRatio = compressionRatio;

            return response;

        } catch (Exception e) {
            logger.error("Error compressing PDF", e);
            throw new PdfProcessingException("Failed to compress PDF file", e);
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
     * Selects the appropriate compression profile based on request.
     *
     * @param request compression request
     * @return compression profile
     */
    private Profile selectCompressionProfile(CompressRequest request) {
        String profileName = request.compressionProfile;

        if (profileName == null) {
            profileName = "web";
        }

        switch (profileName.toLowerCase()) {
            case "web":
                logger.info("Using Web compression profile (optimized for screen viewing)");
                return new Web();

            case "print":
                logger.info("Using Print compression profile (optimized for printing)");
                return new Print();

            case "custom":
                logger.info("Using custom compression profile with quality: {}",
                           request.imageQuality);
                // For custom profile, we can use Web as base and adjust if needed
                // PDF Tools SDK may have additional customization options
                return new Web();

            default:
                logger.warn("Unknown profile '{}', defaulting to Web", profileName);
                return new Web();
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
