package com.pdfeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for PDF split operation.
 * Supports splitting by page ranges or specific split points.
 *
 * @author PDF Editor Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SplitRequest {
    /**
     * Split mode: "pages" or "ranges"
     * - pages: split at specific page numbers [1, 5, 10]
     * - ranges: split by page ranges ["1-3", "4-6", "7-10"]
     */
    public String splitMode;

    /**
     * Page numbers or ranges based on split mode.
     */
    public List<String> splitPoints;

    /**
     * Base name for output files (will be suffixed with page numbers).
     */
    public String outputFileNameBase;
}
