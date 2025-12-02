# PDF Editor Backend

Backend service for PDF editing operations using PDF Tools SDK.

## Features

- **Merge PDFs**: Combine multiple PDF files into a single document
- **Split PDF**: Divide a PDF into multiple files by pages or ranges
- **Compress PDF**: Reduce file size with web/print optimization profiles
- **Convert to Image**: Transform PDF pages to PNG, JPEG, or TIFF formats

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **PDF Tools SDK 1.14.0** (macOS ARM64)
- **Maven** for dependency management

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PDF Tools SDK license key
- macOS with ARM64 architecture

## Setup

### 1. Install Dependencies

```bash
mvn clean install
```

### 2. Configure License Key

Set the PDF Tools SDK license key as an environment variable:

```bash
export PDFTOOLS_LICENSE_KEY="<PDFSDK,V1,MGAAS0GPQFL3W2XUDBL>"
```

Alternatively, update `src/main/resources/application.properties`:

```properties
pdftools.sdk.license-key=<PDFSDK,V1,MGAAS0GPQFL3W2XUDBL>
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The server will start at `http://localhost:8080/api`

## API Endpoints

### Merge PDFs

**POST** `/api/merge`

Merges multiple PDF files into one.

**Parameters:**
- `files` (multipart): List of PDF files to merge
- `outputFileName` (optional): Desired output filename

**Example:**
```bash
curl -X POST http://localhost:8080/api/merge \
  -F "files=@file1.pdf" \
  -F "files=@file2.pdf" \
  -F "outputFileName=merged.pdf"
```

### Split PDF

**POST** `/api/split`

Splits a PDF file into multiple files.

**Parameters:**
- `file` (multipart): PDF file to split
- `splitMode`: "pages" or "ranges"
- `splitPoints`: Page numbers or ranges (e.g., ["1-3", "4-6"] or ["3", "6", "9"])
- `outputFileNameBase` (optional): Base name for output files

**Example (by ranges):**
```bash
curl -X POST http://localhost:8080/api/split \
  -F "file=@document.pdf" \
  -F "splitMode=ranges" \
  -F "splitPoints=1-3" \
  -F "splitPoints=4-6" \
  -F "outputFileNameBase=section"
```

**Example (by pages):**
```bash
curl -X POST http://localhost:8080/api/split \
  -F "file=@document.pdf" \
  -F "splitMode=pages" \
  -F "splitPoints=5" \
  -F "splitPoints=10"
```

### Compress PDF

**POST** `/api/compress`

Compresses a PDF to reduce file size.

**Parameters:**
- `file` (multipart): PDF file to compress
- `compressionProfile`: "web" (default), "print", or "custom"
- `imageQuality` (optional): Quality for custom profile (1-100)
- `outputFileName` (optional): Desired output filename

**Example:**
```bash
curl -X POST http://localhost:8080/api/compress \
  -F "file=@large.pdf" \
  -F "compressionProfile=web" \
  -F "outputFileName=compressed.pdf"
```

### Convert PDF to Image

**POST** `/api/convert`

Converts PDF pages to image format.

**Parameters:**
- `file` (multipart): PDF file to convert
- `imageFormat`: "png" (default), "jpeg", or "tiff"
- `dpi`: Resolution (default 150)
- `pages` (optional): Page specification (e.g., "1,3,5" or "1-5", null for all pages)
- `outputFileNameBase` (optional): Base name for output files

**Example:**
```bash
curl -X POST http://localhost:8080/api/convert \
  -F "file=@document.pdf" \
  -F "imageFormat=png" \
  -F "dpi=300" \
  -F "pages=1-3"
```

### Download File

**GET** `/api/download/{filename}`

Downloads a processed file.

**Example:**
```bash
curl -O http://localhost:8080/api/download/merged.pdf
```

## Project Structure

```
backend/
├── src/main/java/com/pdfeditor/
│   ├── controller/          # REST API endpoints
│   ├── service/            # Business logic
│   ├── config/             # Configuration classes
│   ├── dto/                # Data Transfer Objects
│   ├── exception/          # Exception handling
│   └── PdfEditorApplication.java
├── src/main/resources/
│   └── application.properties
├── lib/                    # PDF Tools SDK
│   ├── com.pdftools.jar
│   └── osx-arm64/
├── uploads/                # Temporary upload directory
├── outputs/                # Processed files directory
└── pom.xml
```

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# Server port
server.port=8080

# File upload limits
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB

# Working directories
app.upload.dir=./uploads
app.output.dir=./outputs

# PDF Tools SDK
pdftools.sdk.license-key=${PDFTOOLS_LICENSE_KEY}
pdftools.sdk.native-lib-path=./lib/osx-arm64

# CORS (adjust for production)
cors.allowed-origins=http://localhost:3000,http://localhost:5173
```

## Logging

Logs are output to console with DEBUG level for application code:

```
2025-11-12 14:30:00 - Initializing PDF Tools SDK...
2025-11-12 14:30:01 - Received merge request for 3 files
2025-11-12 14:30:05 - PDF merge completed successfully: merged.pdf
```

## Error Handling

All endpoints return consistent error responses:

```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

HTTP status codes:
- `200 OK`: Success
- `400 Bad Request`: Invalid input
- `413 Payload Too Large`: File size exceeds limit
- `500 Internal Server Error`: Processing error

## Development

### Build

```bash
mvn clean package
```

### Run Tests

```bash
mvn test
```

### Code Style

- Follow Java naming conventions
- Use clear, descriptive variable names
- Add JavaDoc comments to public methods
- Apply OOP principles (SOLID)

## License

PDF Tools SDK requires a valid license key. Contact PDF Tools AG for licensing information.

## Author

PDF Editor Team
