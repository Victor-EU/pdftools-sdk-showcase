package com.pdfeditor.service;

import com.pdfeditor.dto.FileResponse;
import com.pdfeditor.dto.SplitRequest;
import com.pdfeditor.exception.PdfProcessingException;
import com.pdftools.pdf.Document;
import com.pdftools.documentassembly.DocumentAssembler;
import com.pdftools.documentassembly.PageCopyOptions;
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
 * Service for splitting a PDF file into multiple PDFs.
 * Uses PDF Tools SDK DocumentAssembler for split operations.
 *
 * @author PDF Editor Team
 */
@Service
public class PdfSplitService {

    private static final Logger logger = LoggerFactory.getLogger(PdfSplitService.class);

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.output.dir}")
    private String outputDir;

    /**
     * Splits a PDF file based on the provided split request.
     *
     * @param file the PDF file to split
     * @param splitRequest split configuration
     * @return list of FileResponse for each split PDF
     * @throws PdfProcessingException if split operation fails
     */
    public List<FileResponse> splitPdf(MultipartFile file, SplitRequest splitRequest) {
        logger.info("Starting PDF split operation: mode={}, points={}",
                    splitRequest.splitMode, splitRequest.splitPoints.size());

        if (file == null || file.isEmpty()) {
            throw new PdfProcessingException("No file provided for splitting");
        }

        File tempFile = null;
        List<FileResponse> responses = new ArrayList<>();

        try {
            // Save uploaded file
            tempFile = saveUploadedFile(file);

            // Open source document
            FileStream sourceStream = new FileStream(tempFile.getAbsolutePath(), FileStream.Mode.READ_ONLY);
            Document sourceDoc = Document.open(sourceStream, null);
            int totalPages = sourceDoc.getPageCount();

            logger.info("Source PDF has {} pages", totalPages);

            // Split based on mode
            if ("ranges".equalsIgnoreCase(splitRequest.splitMode)) {
                responses = splitByRanges(sourceDoc, splitRequest.splitPoints,
                                         splitRequest.outputFileNameBase);
            } else if ("pages".equalsIgnoreCase(splitRequest.splitMode)) {
                responses = splitByPages(sourceDoc, splitRequest.splitPoints,
                                        splitRequest.outputFileNameBase);
            } else {
                throw new PdfProcessingException("Invalid split mode: " + splitRequest.splitMode);
            }

            sourceDoc.close();
            sourceStream.close();

            logger.info("PDF split completed successfully: {} files created", responses.size());

            return responses;

        } catch (Exception e) {
            logger.error("Error splitting PDF", e);
            throw new PdfProcessingException("Failed to split PDF file", e);
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
     * Splits PDF by page ranges (e.g., "1-3", "4-6", "7-10").
     *
     * @param sourceDoc source document
     * @param ranges list of page ranges
     * @param baseFileName base name for output files
     * @return list of FileResponse
     */
    private List<FileResponse> splitByRanges(Document sourceDoc, List<String> ranges, String baseFileName)
            throws Exception {
        List<FileResponse> responses = new ArrayList<>();
        int totalPages = sourceDoc.getPageCount();

        for (int i = 0; i < ranges.size(); i++) {
            String range = ranges.get(i);
            String[] parts = range.split("-");
            int startPage = Integer.parseInt(parts[0].trim());
            int endPage = Integer.parseInt(parts[1].trim());

            logger.info("Splitting range {}: pages {}-{} (total pages: {})", i + 1, startPage, endPage, totalPages);

            // Validate page range
            if (startPage < 1 || startPage > totalPages) {
                String errorMsg = "Start page " + startPage + " is out of range. PDF has " + totalPages + " pages (valid range: 1-" + totalPages + ")";
                logger.error(errorMsg);
                throw new PdfProcessingException(errorMsg);
            }
            if (endPage < startPage || endPage > totalPages) {
                String errorMsg = "End page " + endPage + " is out of range. PDF has " + totalPages + " pages (valid range: " + startPage + "-" + totalPages + ")";
                logger.error(errorMsg);
                throw new PdfProcessingException(errorMsg);
            }

            String outputFileName = generateOutputFileName(baseFileName, i + 1, startPage, endPage);
            File outputFile = new File(outputDir, outputFileName);

            // Create new document with specified page range
            try (FileStream outputStream = new FileStream(outputFile.getAbsolutePath(), FileStream.Mode.READ_WRITE_NEW);
                 DocumentAssembler assembler = new DocumentAssembler(outputStream)) {

                // Copy page range (pages are 1-indexed in the API, both inclusive)
                assembler.append(sourceDoc, startPage, endPage);

                // Assemble the output
                assembler.assemble();
            }

            responses.add(createFileResponse(outputFile));
        }

        return responses;
    }

    /**
     * Splits PDF at specific page numbers (creates files between split points).
     *
     * @param sourceDoc source document
     * @param splitPoints list of page numbers where splits occur
     * @param baseFileName base name for output files
     * @return list of FileResponse
     */
    private List<FileResponse> splitByPages(Document sourceDoc, List<String> splitPoints, String baseFileName)
            throws Exception {
        List<FileResponse> responses = new ArrayList<>();
        int totalPages = sourceDoc.getPageCount();

        List<Integer> points = new ArrayList<>();
        points.add(1); // Start from page 1

        // Parse split points
        for (String point : splitPoints) {
            points.add(Integer.parseInt(point.trim()));
        }

        points.add(totalPages + 1); // End after last page

        // Create documents between split points
        for (int i = 0; i < points.size() - 1; i++) {
            int startPage = points.get(i);
            int endPage = points.get(i + 1) - 1;

            if (startPage > endPage) continue;

            String outputFileName = generateOutputFileName(baseFileName, i + 1, startPage, endPage);
            File outputFile = new File(outputDir, outputFileName);

            try (FileStream outputStream = new FileStream(outputFile.getAbsolutePath(), FileStream.Mode.READ_WRITE_NEW);
                 DocumentAssembler assembler = new DocumentAssembler(outputStream)) {

                // Copy page range
                assembler.append(sourceDoc, startPage, endPage);

                // Assemble the output
                assembler.assemble();
            }

            responses.add(createFileResponse(outputFile));
        }

        return responses;
    }

    /**
     * Generates output filename for split PDF.
     *
     * @param baseFileName base name
     * @param partNumber part number
     * @param startPage start page
     * @param endPage end page
     * @return generated filename
     */
    private String generateOutputFileName(String baseFileName, int partNumber, int startPage, int endPage) {
        if (baseFileName == null || baseFileName.isEmpty()) {
            baseFileName = "split_" + UUID.randomUUID().toString();
        }
        return String.format("%s_part%d_pages%d-%d.pdf", baseFileName, partNumber, startPage, endPage);
    }

    /**
     * Creates FileResponse from output file.
     *
     * @param file the output file
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
