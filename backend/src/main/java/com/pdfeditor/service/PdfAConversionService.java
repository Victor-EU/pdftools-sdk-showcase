package com.pdfeditor.service;

import com.pdfeditor.dto.FileResponse;
import com.pdfeditor.dto.PdfAConversionRequest;
import com.pdfeditor.exception.PdfProcessingException;
import com.pdftools.pdfa.conversion.Converter;
import com.pdftools.pdfa.conversion.ConversionOptions;
import com.pdftools.pdfa.validation.Validator;
import com.pdftools.pdfa.validation.AnalysisResult;
import com.pdftools.pdfa.validation.AnalysisOptions;
import com.pdftools.pdf.Conformance;
import com.pdftools.pdf.Document;
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
 * Service for converting PDF documents to PDF/A format.
 * Uses PDF Tools SDK Validator and Converter for PDF/A conversion.
 *
 * @author PDF Editor Team
 */
@Service
public class PdfAConversionService {

    private static final Logger logger = LoggerFactory.getLogger(PdfAConversionService.class);

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.output.dir}")
    private String outputDir;

    /**
     * Converts a PDF file to PDF/A format.
     *
     * @param file the PDF file to convert
     * @param request conversion options including target conformance level
     * @return FileResponse containing converted PDF/A information
     * @throws PdfProcessingException if conversion fails
     */
    public FileResponse convertToPdfA(MultipartFile file, PdfAConversionRequest request) {
        logger.info("Starting PDF/A conversion for file: {}, target level: {}",
                   file.getOriginalFilename(), request.conformanceLevel);

        if (file == null || file.isEmpty()) {
            throw new PdfProcessingException("No file provided for PDF/A conversion");
        }

        File tempFile = null;
        File outputFile = null;

        try {
            // Save uploaded file temporarily
            tempFile = saveUploadedFile(file);

            // Open source document
            FileStream inputStream = new FileStream(tempFile.getAbsolutePath(), FileStream.Mode.READ_ONLY);
            Document sourceDoc = Document.open(inputStream, null);

            // Generate output filename
            String outputFileName = request.outputFileName;
            if (outputFileName == null || outputFileName.isEmpty()) {
                String baseName = file.getOriginalFilename();
                if (baseName != null && baseName.contains(".")) {
                    baseName = baseName.substring(0, baseName.lastIndexOf("."));
                }
                outputFileName = baseName + "_pdfa_" + UUID.randomUUID().toString().substring(0, 8) + ".pdf";
            }
            if (!outputFileName.endsWith(".pdf")) {
                outputFileName += ".pdf";
            }

            outputFile = new File(outputDir, outputFileName);
            Files.createDirectories(outputFile.getParentFile().toPath());

            // Create output stream
            FileStream outputStream = new FileStream(outputFile.getAbsolutePath(), FileStream.Mode.READ_WRITE_NEW);

            // Step 1: Create a Validator and analyze the document
            Validator validator = new Validator();
            AnalysisOptions analysisOptions = new AnalysisOptions();

            // Analyze the document to get an AnalysisResult
            AnalysisResult analysisResult = validator.analyze(sourceDoc, analysisOptions);

            // Step 2: Create converter and conversion options
            Converter converter = new Converter();
            ConversionOptions conversionOptions = new ConversionOptions();

            // Step 3: Perform conversion using the analysis result
            // The convert method takes: AnalysisResult, Document, Stream
            Document convertedDoc = converter.convert(analysisResult, sourceDoc, outputStream, conversionOptions);

            // Close resources
            if (convertedDoc != null) {
                convertedDoc.close();
            }
            outputStream.close();
            sourceDoc.close();
            inputStream.close();

            // Build response
            FileResponse response = new FileResponse();
            response.fileName = outputFileName;
            response.filePath = outputFile.getAbsolutePath();
            response.fileSize = outputFile.length();
            response.downloadUrl = "/download/" + outputFileName;
            response.originalSize = file.getSize();

            logger.info("PDF/A conversion completed: {} -> {} bytes",
                       file.getSize(), response.fileSize);

            return response;

        } catch (Exception e) {
            logger.error("Error converting to PDF/A", e);
            // Clean up output file on error
            if (outputFile != null && outputFile.exists()) {
                try {
                    Files.delete(outputFile.toPath());
                } catch (IOException ex) {
                    logger.warn("Failed to delete output file after error", ex);
                }
            }
            throw new PdfProcessingException("Failed to convert PDF to PDF/A format: " + e.getMessage(), e);
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
     */
    private File saveUploadedFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        return filePath.toFile();
    }
}
