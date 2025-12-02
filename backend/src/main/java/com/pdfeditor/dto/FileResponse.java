package com.pdfeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO containing file information after processing.
 *
 * @author PDF Editor Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {
    public String fileName;
    public String filePath;
    public long fileSize;
    public String downloadUrl;

    /**
     * Original file size (for compression operations).
     */
    public Long originalSize;

    /**
     * Compression ratio percentage (for compression operations).
     */
    public Double compressionRatio;
}
