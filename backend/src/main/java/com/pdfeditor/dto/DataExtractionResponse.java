package com.pdfeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Response DTO containing extracted data from PDF.
 * Supports text extraction, table extraction, and image extraction.
 *
 * @author PDF Editor Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataExtractionResponse {

    /** Full extracted text content */
    public String textContent;

    /** Text content organized by page */
    public List<PageContent> pages;

    /** Number of images found in the document */
    public int imageCount;

    /** Extracted images (if requested) */
    public List<ExtractedImage> images;

    /** Number of tables found */
    public int tableCount;

    /** Total word count */
    public int wordCount;

    /** Total character count */
    public int characterCount;

    /**
     * Represents content from a single page.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageContent {
        public int pageNumber;
        public String text;
        public int wordCount;
    }

    /**
     * Represents an extracted image.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExtractedImage {
        public int pageNumber;
        public int imageIndex;
        public String fileName;
        public String downloadUrl;
        public long fileSize;
        public int width;
        public int height;
        public String format;
    }
}
