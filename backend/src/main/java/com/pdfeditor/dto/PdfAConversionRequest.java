package com.pdfeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for PDF/A conversion operations.
 * Specifies target conformance level and conversion options.
 *
 * @author PDF Editor Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PdfAConversionRequest {

    /**
     * Target PDF/A conformance level.
     * Supported values: "1a", "1b", "2a", "2b", "2u", "3a", "3b", "3u"
     */
    public String conformanceLevel;

    /** Output filename for the converted PDF */
    public String outputFileName;

    /** Whether to copy metadata from source document */
    public boolean copyMetadata = true;

    /** Whether to embed fonts that are not embedded */
    public boolean embedFonts = true;

    /** Whether to flatten transparency */
    public boolean flattenTransparency = false;

    /** Image quality for recompressed images (1-100) */
    public Integer imageQuality;
}
