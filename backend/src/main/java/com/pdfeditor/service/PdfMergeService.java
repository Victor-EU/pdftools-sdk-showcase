package com.pdfeditor.service;

import com.pdfeditor.dto.FileResponse;
import com.pdfeditor.exception.PdfProcessingException;
import com.pdftools.pdf.Document;
import com.pdftools.documentassembly.DocumentAssembler;
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
 * Service for merging multiple PDF files into a single PDF.
 * Uses PDF Tools SDK DocumentAssembler for merge operations.
 *
 * @author PDF Editor Team
 */
@Service
public class PdfMergeService {

    private static final Logger logger = LoggerFactory.getLogger(PdfMergeService.class);

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.output.dir}")
    private String outputDir;

    /**
     * Merges multiple PDF files into a single PDF document.
     *
     * @param files list of PDF files to merge
     * @param outputFileName desired output filename
     * @return FileResponse containing merged PDF information
     * @throws PdfProcessingException if merge operation fails
     */
    public FileResponse mergePdfs(List<MultipartFile> files, String outputFileName) {
        logger.info("Starting PDF merge operation for {} files", files.size());

        if (files == null || files.isEmpty()) {
            throw new PdfProcessingException("No files provided for merging");
        }

        if (files.size() < 2) {
            throw new PdfProcessingException("At least 2 files are required for merging");
        }

        List<File> tempFiles = new ArrayList<>();
        File outputFile = null;

        try {
            // Save uploaded files temporarily
            for (MultipartFile file : files) {
                File tempFile = saveUploadedFile(file);
                tempFiles.add(tempFile);
            }

            // Generate output filename
            if (outputFileName == null || outputFileName.isEmpty()) {
                outputFileName = "merged_" + UUID.randomUUID().toString() + ".pdf";
            }
            if (!outputFileName.endsWith(".pdf")) {
                outputFileName += ".pdf";
            }

            outputFile = new File(outputDir, outputFileName);

            // Create output stream for merged PDF
            try (FileStream outputStream = new FileStream(outputFile.getAbsolutePath(), FileStream.Mode.READ_WRITE_NEW);
                 DocumentAssembler docAssembler = new DocumentAssembler(outputStream)) {

                // Append all documents
                for (File tempFile : tempFiles) {
                    try (FileStream inputStream = new FileStream(tempFile.getAbsolutePath(), FileStream.Mode.READ_ONLY);
                         Document inputDocument = Document.open(inputStream, null)) {

                        // Append entire document
                        docAssembler.append(inputDocument);
                    }
                }

                // Create the final structure of the output PDF
                docAssembler.assemble();
            }

            logger.info("PDF merge completed successfully: {}", outputFileName);

            // Create response
            FileResponse response = new FileResponse();
            response.fileName = outputFileName;
            response.filePath = outputFile.getAbsolutePath();
            response.fileSize = outputFile.length();
            response.downloadUrl = "/download/" + outputFileName;

            return response;

        } catch (Exception e) {
            logger.error("Error merging PDFs", e);
            throw new PdfProcessingException("Failed to merge PDF files", e);
        } finally {
            // Clean up temporary files
            cleanupTempFiles(tempFiles);
        }
    }

    /**
     * Saves an uploaded file to the upload directory.
     *
     * @param file the uploaded file
     * @return the saved file
     * @throws IOException if file save fails
     */
    private File saveUploadedFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        return filePath.toFile();
    }

    /**
     * Cleans up temporary files after processing.
     *
     * @param tempFiles list of temporary files to delete
     */
    private void cleanupTempFiles(List<File> tempFiles) {
        for (File file : tempFiles) {
            try {
                if (file != null && file.exists()) {
                    Files.delete(file.toPath());
                }
            } catch (IOException e) {
                logger.warn("Failed to delete temp file: {}", file.getAbsolutePath(), e);
            }
        }
    }
}
