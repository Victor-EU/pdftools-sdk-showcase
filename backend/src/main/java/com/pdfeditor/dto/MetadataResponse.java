package com.pdfeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Response DTO containing PDF metadata information.
 * Includes standard PDF document properties and XMP metadata.
 *
 * @author PDF Editor Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataResponse {

    /** Document title */
    public String title;

    /** Document author */
    public String author;

    /** Document subject/description */
    public String subject;

    /** Keywords associated with the document */
    public String keywords;

    /** Application that created the document */
    public String creator;

    /** PDF producer application */
    public String producer;

    /** Document creation date */
    public Date creationDate;

    /** Last modification date */
    public Date modificationDate;

    /** PDF version (e.g., "1.4", "1.7") */
    public String pdfVersion;

    /** Total number of pages */
    public int pageCount;

    /** File size in bytes */
    public long fileSize;

    /** Whether the document is encrypted */
    public boolean isEncrypted;

    /** Whether the document is linearized (fast web view) */
    public boolean isLinearized;

    /** Whether the document contains forms */
    public boolean hasForms;

    /** Whether the document is tagged (accessible) */
    public boolean isTagged;

    /** PDF/A conformance level if applicable (e.g., "PDF/A-1b", "PDF/A-2a") */
    public String pdfaConformance;
}
