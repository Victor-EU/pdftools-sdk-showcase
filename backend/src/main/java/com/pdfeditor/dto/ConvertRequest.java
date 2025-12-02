package com.pdfeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for PDF to image conversion.
 *
 * @author PDF Editor Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConvertRequest {
    /**
     * Output image format: "png", "jpeg", "tiff"
     */
    public String imageFormat;

    /**
     * DPI (dots per inch) for image resolution.
     * Common values: 72 (screen), 150 (standard), 300 (high quality)
     */
    public Integer dpi;

    /**
     * Page numbers to convert (comma-separated, e.g., "1,3,5" or "1-5").
     * If null or empty, converts all pages.
     */
    public String pages;

    /**
     * Output filename base (will be suffixed with page number for multi-page).
     */
    public String outputFileNameBase;
}
