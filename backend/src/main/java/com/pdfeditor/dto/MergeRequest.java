package com.pdfeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for PDF merge operation.
 * Contains the output filename for the merged PDF.
 *
 * @author PDF Editor Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergeRequest {
    private String outputFileName;
}
