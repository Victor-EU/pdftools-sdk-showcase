package com.pdfeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for PDF compression operation.
 *
 * @author PDF Editor Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompressRequest {
    /**
     * Compression profile: "web", "print", "custom"
     * - web: optimized for web viewing (smaller file size)
     * - print: optimized for printing (better quality)
     * - custom: custom compression settings
     */
    public String compressionProfile;

    /**
     * Image quality (1-100) for custom compression.
     * Higher values = better quality, larger file size.
     */
    public Integer imageQuality;

    /**
     * Output filename for compressed PDF.
     */
    public String outputFileName;
}
