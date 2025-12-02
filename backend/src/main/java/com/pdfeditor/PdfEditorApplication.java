package com.pdfeditor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for PDF Editor backend service.
 *
 * This application provides REST APIs for PDF operations:
 * - View, Annotate, and Redact (handled by frontend SDK)
 * - Merge multiple PDF files
 * - Split PDF into multiple files
 * - Compress PDF file
 * - Convert PDF to image formats
 *
 * @author PDF Editor Team
 * @version 1.0.0
 */
@SpringBootApplication
public class PdfEditorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PdfEditorApplication.class, args);
    }
}
