package com.pdfeditor.service;

import com.pdfeditor.dto.DataExtractionResponse;
import com.pdfeditor.dto.DataExtractionResponse.PageContent;
import com.pdfeditor.exception.PdfProcessingException;
import com.pdftools.pdf.Document;
import com.pdftools.extraction.Extractor;
import com.pdftools.extraction.TextOptions;
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
import java.util.UUID;

/**
 * Service for extracting data from PDF documents.
 * Uses PDF Tools SDK Extractor for text extraction.
 *
 * @author PDF Editor Team
 */
@Service
public class PdfDataExtractionService {

    private static final Logger logger = LoggerFactory.getLogger(PdfDataExtractionService.class);

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.output.dir}")
    private String outputDir;

    /**
     * Extracts text and data from a PDF file.
     *
     * @param file the PDF file to extract from
     * @param extractImages whether to extract images
     * @param pages specific pages to extract (null for all)
     * @return DataExtractionResponse containing extracted content
     * @throws PdfProcessingException if extraction fails
     */
    public DataExtractionResponse extractData(MultipartFile file, boolean extractImages, String pages) {
        logger.info("Starting data extraction for file: {}, extractImages: {}",
                   file.getOriginalFilename(), extractImages);

        if (file == null || file.isEmpty()) {
            throw new PdfProcessingException("No file provided for data extraction");
        }

        File tempFile = null;
        File outputTextFile = null;

        try {
            // Save uploaded file temporarily
            tempFile = saveUploadedFile(file);

            // Open the PDF document
            FileStream inputStream = new FileStream(tempFile.getAbsolutePath(), FileStream.Mode.READ_ONLY);
            Document document = Document.open(inputStream, null);

            DataExtractionResponse response = new DataExtractionResponse();
            response.pages = new ArrayList<>();
            response.images = new ArrayList<>();

            int pageCount = document.getPageCount();

            // Create text extractor
            Extractor extractor = new Extractor();
            TextOptions textOptions = new TextOptions();

            // Create output file for text extraction
            String outputFileName = UUID.randomUUID().toString() + ".txt";
            outputTextFile = new File(outputDir, outputFileName);
            Files.createDirectories(outputTextFile.getParentFile().toPath());

            FileStream outputStream = new FileStream(outputTextFile.getAbsolutePath(), FileStream.Mode.READ_WRITE_NEW);

            // Extract text from document
            extractor.extractText(document, outputStream, textOptions);

            outputStream.close();

            // Read extracted text
            String fullText = new String(Files.readAllBytes(outputTextFile.toPath()));

            // Split by page markers if available, otherwise use full text
            int totalWordCount = countWords(fullText);
            int totalCharCount = fullText.length();

            // Create page content - since SDK extracts all text together,
            // we'll create a single page entry with all content
            for (int i = 1; i <= pageCount; i++) {
                if (pages != null && !pages.isEmpty() && !shouldProcessPage(i, pages)) {
                    continue;
                }

                PageContent pageContent = new PageContent();
                pageContent.pageNumber = i;
                // For now, we put all text in page 1 and empty for others
                // A more sophisticated implementation would use page markers
                if (i == 1) {
                    pageContent.text = fullText;
                    pageContent.wordCount = totalWordCount;
                } else {
                    pageContent.text = "";
                    pageContent.wordCount = 0;
                }
                response.pages.add(pageContent);
            }

            // Close resources
            document.close();
            inputStream.close();

            response.textContent = fullText;
            response.wordCount = totalWordCount;
            response.characterCount = totalCharCount;
            response.imageCount = 0; // Image extraction would require additional implementation
            response.tableCount = 0;

            logger.info("Data extraction completed: {} pages, {} words",
                       pageCount, response.wordCount);

            return response;

        } catch (Exception e) {
            logger.error("Error extracting data from PDF", e);
            throw new PdfProcessingException("Failed to extract data from PDF file", e);
        } finally {
            // Clean up temp files
            if (tempFile != null && tempFile.exists()) {
                try {
                    Files.delete(tempFile.toPath());
                } catch (IOException e) {
                    logger.warn("Failed to delete temp file", e);
                }
            }
            if (outputTextFile != null && outputTextFile.exists()) {
                try {
                    Files.delete(outputTextFile.toPath());
                } catch (IOException e) {
                    logger.warn("Failed to delete output text file", e);
                }
            }
        }
    }

    /**
     * Counts words in a string.
     */
    private int countWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }

    /**
     * Checks if a page number should be processed based on page specification.
     */
    private boolean shouldProcessPage(int pageNum, String pagesSpec) {
        if (pagesSpec == null || pagesSpec.isEmpty()) {
            return true;
        }

        String[] parts = pagesSpec.split(",");
        for (String part : parts) {
            part = part.trim();
            if (part.contains("-")) {
                String[] range = part.split("-");
                int start = Integer.parseInt(range[0].trim());
                int end = Integer.parseInt(range[1].trim());
                if (pageNum >= start && pageNum <= end) {
                    return true;
                }
            } else {
                if (pageNum == Integer.parseInt(part)) {
                    return true;
                }
            }
        }
        return false;
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
