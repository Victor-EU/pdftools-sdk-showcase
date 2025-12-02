package com.pdfeditor.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class PdfToolsConfig {

    private static final Logger logger = LoggerFactory.getLogger(PdfToolsConfig.class);

    @Value("${pdftools.sdk.license-key}")
    private String licenseKey;

    @Value("${pdftools.sdk.native-lib-path}")
    private String nativeLibPath;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.output.dir}")
    private String outputDir;

    @PostConstruct
    public void initialize() {
        try {
            logger.info("Initializing PDF Tools SDK...");

            // Set native library path - detect OS for correct library extension
            String absoluteLibPath = new File(nativeLibPath).getAbsolutePath();
            String osName = System.getProperty("os.name").toLowerCase();
            String libExtension = osName.contains("mac") ? ".dylib" : ".so";
            String libPath = absoluteLibPath + "/libPdfToolsSdk" + libExtension;
            logger.info("Loading native library from: {} (OS: {})", libPath, osName);
            System.load(libPath);

            // Initialize SDK with license key
            if (licenseKey != null && !licenseKey.isEmpty()) {
                logger.info("Initializing PDF Tools SDK with license key...");
                com.pdftools.Sdk.initialize(licenseKey);
                logger.info("PDF Tools SDK initialized successfully");
            } else {
                logger.warn("No license key provided - SDK may have limited functionality");
            }

            // Create working directories
            createDirectoryIfNotExists(uploadDir);
            createDirectoryIfNotExists(outputDir);

            logger.info("PDF Editor backend initialized successfully");

        } catch (Exception e) {
            logger.error("Failed to initialize PDF Tools SDK", e);
            throw new RuntimeException("PDF Tools SDK initialization failed", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            logger.info("Cleaning up resources...");
            logger.info("Cleanup completed");
        } catch (Exception e) {
            logger.error("Error during cleanup", e);
        }
    }

    private void createDirectoryIfNotExists(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                logger.info("Created directory: {}", path.toAbsolutePath());
            }
        } catch (Exception e) {
            logger.error("Failed to create directory: {}", dirPath, e);
        }
    }
}
