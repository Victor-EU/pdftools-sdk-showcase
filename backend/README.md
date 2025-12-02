# PDF Tools SDK Showcase - Backend

Backend service for PDF editing operations using PDF Tools SDK.

## 🚀 Live Deployment

| Environment | URL |
|-------------|-----|
| **Production** | https://pdftools-sdk-showcase-production.up.railway.app/api |
| **Health Check** | https://pdftools-sdk-showcase-production.up.railway.app/api/health |

**Hosted on**: [Railway](https://railway.app)

## Features

- **Merge PDFs**: Combine multiple PDF files into a single document
- **Split PDF**: Divide a PDF into multiple files by pages or ranges
- **Compress PDF**: Reduce file size with web/print optimization profiles
- **Convert to Image**: Transform PDF pages to PNG, JPEG, or TIFF formats
- **Health Check**: `/api/health` endpoint for monitoring
- **PDF/A Validation**: Validate PDF/A conformance
- **PDF/A Conversion**: Convert PDFs to PDF/A format
- **Metadata Extraction**: Extract document metadata
- **Data Extraction**: Extract text and images from PDFs

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Boot Actuator** for health monitoring
- **PDF Tools SDK 1.14.0**
- **Maven** for dependency management

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PDF Tools SDK license key
- Linux x64 (production) or macOS ARM64 (development)

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

The server will start at `http://localhost:5001/api`

## API Endpoints

### Health Check

**GET** `/api/health`

Returns the application health status.

**Example:**
```bash
curl http://localhost:5001/api/health
```

**Response:**
```json
{
  "status": "UP",
  "components": {
    "diskSpace": { "status": "UP" },
    "ping": { "status": "UP" }
  }
}
```

### Merge PDFs

**POST** `/api/merge`

Merges multiple PDF files into one.

**Parameters:**
- `files` (multipart): List of PDF files to merge
- `outputFileName` (optional): Desired output filename

**Example:**
```bash
curl -X POST http://localhost:5001/api/merge \
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
curl -X POST http://localhost:5001/api/split \
  -F "file=@document.pdf" \
  -F "splitMode=ranges" \
  -F "splitPoints=1-3" \
  -F "splitPoints=4-6" \
  -F "outputFileNameBase=section"
```

**Example (by pages):**
```bash
curl -X POST http://localhost:5001/api/split \
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
curl -X POST http://localhost:5001/api/compress \
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
curl -X POST http://localhost:5001/api/convert \
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
curl -O http://localhost:5001/api/download/merged.pdf
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
server.port=5001
server.servlet.context-path=/api

# File upload limits
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB

# Working directories
app.upload.dir=./uploads
app.output.dir=./outputs

# PDF Tools SDK
pdftools.sdk.license-key=${PDFTOOLS_LICENSE_KEY}
pdftools.sdk.native-lib-path=${NATIVE_LIB_PATH:./lib/linux-x64}

# CORS (adjust for production)
cors.allowed-origins=${CORS_ORIGINS:http://localhost:5000,http://localhost:3000}

# Actuator (health endpoint)
management.endpoints.web.exposure.include=health
management.endpoints.web.base-path=/
management.endpoint.health.show-details=always
```

## Railway Deployment

The backend is configured for Railway deployment with:

1. **Dockerfile**: Multi-stage build with Maven and JRE
2. **railway.toml**: Deployment configuration
3. **Health check**: `/api/health` endpoint for Railway health probes

### Deploy to Railway

```bash
# Install Railway CLI
npm install -g @railway/cli

# Login and link
railway login
railway link

# Set environment variables
railway variables --set "PDFTOOLS_LICENSE_KEY=your-license-key"
railway variables --set "CORS_ORIGINS=https://your-frontend.vercel.app"

# Deploy
railway up

# Get domain
railway domain
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
