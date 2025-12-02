# PDF Tools SDK Showcase - Complete PDF Processing Solution

![License](https://img.shields.io/badge/license-Proprietary-blue)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)
![React](https://img.shields.io/badge/React-18.2-blue)
![TypeScript](https://img.shields.io/badge/TypeScript-5.2-blue)
![Vercel](https://img.shields.io/badge/Frontend-Vercel-black)
![Railway](https://img.shields.io/badge/Backend-Railway-purple)

A professional-grade PDF processing application built with Spring Boot and React, powered by PDF Tools SDK. Provides comprehensive PDF manipulation capabilities including viewing, merging, splitting, compressing, and converting documents.

## 🚀 Live Demo

| Component | URL |
|-----------|-----|
| **Frontend** | https://frontend-m7eahhoo4-victors-projects-6b496519.vercel.app |
| **Backend API** | https://pdftools-sdk-showcase-production.up.railway.app/api |
| **Health Check** | https://pdftools-sdk-showcase-production.up.railway.app/api/health |

**Repository**: https://github.com/Victor-EU/pdftools-sdk-showcase

## Features

### PDF Viewing & Annotation
- **Interactive PDF Viewer**: Powered by PDF Tools Four Heights Web Viewer SDK
- **Annotation Tools**: Add notes, highlights, and markups
- **Redaction Support**: Permanently remove sensitive information
- **Pan & Zoom**: Smooth navigation and inspection

### PDF Operations

#### 1. Merge PDFs
- Combine multiple PDF files into a single document
- Preserve document structure and formatting
- Support for batch merging (2+ files)
- Custom output naming

#### 2. Split PDF
- **Split by Ranges**: Extract specific page ranges (e.g., 1-5, 10-15)
- **Split by Pages**: Divide at specific page numbers
- Generate multiple output files from one source
- Automatic file naming with page ranges

#### 3. Compress PDF
- **Web Profile**: Optimized for screen viewing (reduced file size)
- **Print Profile**: Maintains quality for printing
- **Custom Profile**: Configurable image quality
- Shows compression ratio and size savings

#### 4. Convert to Image
- **Formats**: PNG, JPEG, TIFF
- **Configurable DPI**: 72-600 DPI resolution
- **Selective Conversion**: Convert specific pages or ranges
- **Batch Export**: Multiple pages to individual images

## Technology Stack

### Backend
- **Java 17**: Modern LTS Java version
- **Spring Boot 3.2.0**: Enterprise-grade framework
- **PDF Tools SDK 1.14.0**: Professional PDF processing library
- **Maven**: Dependency management
- **SLF4J + Logback**: Logging framework

### Frontend
- **React 18**: Modern UI library
- **TypeScript 5.2**: Type-safe development
- **Vite 5.0**: Fast build tool
- **Material-UI (MUI) 5.15**: Professional component library
- **Axios**: HTTP client
- **PDF Tools Four Heights Viewer 4.3.5**: Advanced PDF viewer

## Architecture

### Project Structure

```
pdf-editor/
├── backend/                        # Spring Boot backend
│   ├── src/main/
│   │   ├── java/com/pdfeditor/
│   │   │   ├── config/            # Configuration classes
│   │   │   │   ├── CorsConfig.java
│   │   │   │   └── PdfToolsConfig.java
│   │   │   ├── controller/        # REST API endpoints
│   │   │   │   ├── FileDownloadController.java
│   │   │   │   ├── PdfCompressController.java
│   │   │   │   ├── PdfConvertController.java
│   │   │   │   ├── PdfMergeController.java
│   │   │   │   └── PdfSplitController.java
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   │   ├── ApiResponse.java
│   │   │   │   ├── CompressRequest.java
│   │   │   │   ├── ConvertRequest.java
│   │   │   │   ├── FileResponse.java
│   │   │   │   ├── MergeRequest.java
│   │   │   │   └── SplitRequest.java
│   │   │   ├── exception/         # Exception handling
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── PdfProcessingException.java
│   │   │   ├── service/           # Business logic
│   │   │   │   ├── PdfCompressService.java
│   │   │   │   ├── PdfConvertService.java
│   │   │   │   ├── PdfMergeService.java
│   │   │   │   └── PdfSplitService.java
│   │   │   └── PdfEditorApplication.java
│   │   └── resources/
│   │       └── application.properties
│   ├── lib/                       # PDF Tools SDK
│   │   ├── com.pdftools.jar
│   │   └── osx-arm64/libPdfToolsSdk.dylib
│   ├── uploads/                   # Temporary uploads
│   ├── outputs/                   # Processed files
│   ├── pom.xml                    # Maven configuration
│   ├── README.md                  # Backend documentation
│   └── BACKEND_STATUS.md          # Backend status report
│
├── frontend/                      # React frontend
│   ├── public/                    # Static assets
│   ├── src/
│   │   ├── components/           # React components
│   │   │   ├── Operations/       # PDF operations
│   │   │   │   ├── CompressPanel/
│   │   │   │   ├── ConvertPanel/
│   │   │   │   ├── MergePanel/
│   │   │   │   └── SplitPanel/
│   │   │   └── PDFViewer/        # PDF viewer component
│   │   ├── services/             # API layer
│   │   │   └── api.ts
│   │   ├── theme/                # MUI theme
│   │   │   └── theme.ts
│   │   ├── types/                # TypeScript types
│   │   │   └── index.ts
│   │   ├── App.tsx               # Main component
│   │   └── main.tsx              # Entry point
│   ├── package.json
│   ├── tsconfig.json
│   ├── vite.config.ts
│   └── README.md                 # Frontend documentation
│
├── SECURITY_AUDIT.md             # Security analysis
├── README.md                     # This file
└── USER_GUIDE.md                 # User guide
```

### System Design

```
┌─────────────────┐
│   Frontend      │
│   React + TS    │
│   Port 5000     │
└────────┬────────┘
         │ HTTP/REST
         │ Proxy /api
         ▼
┌─────────────────┐
│   Backend       │
│   Spring Boot   │
│   Port 5001     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  PDF Tools SDK  │
│  Native Library │
└─────────────────┘
```

## Prerequisites

### Backend Requirements
- **Java 17 or higher** ([Download](https://adoptium.net/))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **macOS with ARM64** (or adapt for other platforms)
- **PDF Tools SDK License Key**

### Frontend Requirements
- **Node.js 18+** ([Download](https://nodejs.org/))
- **npm or yarn**

## Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd pdf-editor
```

### 2. Setup Backend

```bash
cd backend

# Set the PDF Tools SDK license key
export PDFTOOLS_LICENSE_KEY="<PDFSDK,V1,YOUR_LICENSE_KEY>"

# Install dependencies
mvn clean install

# Run the backend server
mvn spring-boot:run
```

Backend will start at `http://localhost:5001/api`

### 3. Setup Frontend

```bash
cd frontend

# Install dependencies
npm install

# Run the development server
npm run dev
```

Frontend will start at `http://localhost:5000`

### 4. Access the Application

Open your browser and navigate to:
```
http://localhost:5000
```

## Configuration

### Backend Configuration

Edit `backend/src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=5001
server.servlet.context-path=/api

# File Upload Limits
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB

# Working Directories
app.upload.dir=./uploads
app.output.dir=./outputs

# PDF Tools SDK
pdftools.sdk.license-key=${PDFTOOLS_LICENSE_KEY}
pdftools.sdk.native-lib-path=./lib/osx-arm64

# CORS Configuration
cors.allowed-origins=http://localhost:5000
```

### Frontend Configuration

Edit `frontend/vite.config.ts`:

```typescript
export default defineConfig({
  server: {
    port: 5000,
    proxy: {
      '/api': {
        target: 'http://localhost:5001',
        changeOrigin: true,
      },
    },
  },
})
```

### Environment Variables

Create `.env` files for sensitive configuration:

**Backend** (`.env` or environment):
```bash
export PDFTOOLS_LICENSE_KEY="<PDFSDK,V1,YOUR_LICENSE_KEY>"
```

**Frontend** (`frontend/.env`):
```bash
VITE_API_BASE_URL=http://localhost:5001/api
```

## API Documentation

### Base URL
```
http://localhost:5001/api
```

### Endpoints

#### 1. Merge PDFs
```http
POST /api/merge
Content-Type: multipart/form-data

Parameters:
- files: File[] (required) - PDF files to merge
- outputFileName: string (optional) - Desired output name

Response:
{
  "success": true,
  "message": "PDFs merged successfully",
  "data": {
    "fileName": "merged.pdf",
    "fileSize": 1234567,
    "downloadUrl": "/download/merged.pdf"
  }
}
```

#### 2. Split PDF
```http
POST /api/split
Content-Type: multipart/form-data

Parameters:
- file: File (required) - PDF to split
- splitMode: "pages" | "ranges" (required)
- splitPoints: string[] (required) - Page numbers or ranges
- outputFileNameBase: string (optional)

Response:
{
  "success": true,
  "message": "PDF split successfully",
  "data": [
    {
      "fileName": "output_part1_pages1-5.pdf",
      "fileSize": 123456,
      "downloadUrl": "/download/output_part1_pages1-5.pdf"
    },
    ...
  ]
}
```

#### 3. Compress PDF
```http
POST /api/compress
Content-Type: multipart/form-data

Parameters:
- file: File (required)
- compressionProfile: "web" | "print" | "custom" (required)
- imageQuality: number (optional, for custom profile)
- outputFileName: string (optional)

Response:
{
  "success": true,
  "message": "PDF compressed successfully",
  "data": {
    "fileName": "compressed.pdf",
    "fileSize": 500000,
    "originalSize": 2000000,
    "compressionRatio": 75.0,
    "downloadUrl": "/download/compressed.pdf"
  }
}
```

#### 4. Convert to Image
```http
POST /api/convert
Content-Type: multipart/form-data

Parameters:
- file: File (required)
- imageFormat: "png" | "jpeg" | "tiff" (required)
- dpi: number (optional, default 150)
- pages: string (optional, e.g., "1,3,5" or "1-5")
- outputFileNameBase: string (optional)

Response:
{
  "success": true,
  "message": "PDF converted to images successfully",
  "data": [
    {
      "fileName": "output_page_01.png",
      "fileSize": 123456,
      "downloadUrl": "/download/output_page_01.png"
    },
    ...
  ]
}
```

#### 5. Download File
```http
GET /api/download/{filename}

Response: Binary file data
```

## Usage Examples

### Using cURL

#### Merge PDFs
```bash
curl -X POST http://localhost:5001/api/merge \
  -F "files=@document1.pdf" \
  -F "files=@document2.pdf" \
  -F "outputFileName=merged.pdf"
```

#### Split PDF by Ranges
```bash
curl -X POST http://localhost:5001/api/split \
  -F "file=@document.pdf" \
  -F "splitMode=ranges" \
  -F "splitPoints=1-5" \
  -F "splitPoints=6-10"
```

#### Compress PDF
```bash
curl -X POST http://localhost:5001/api/compress \
  -F "file=@large.pdf" \
  -F "compressionProfile=web"
```

#### Convert to Image
```bash
curl -X POST http://localhost:5001/api/convert \
  -F "file=@document.pdf" \
  -F "imageFormat=png" \
  -F "dpi=300" \
  -F "pages=1-3"
```

### Using JavaScript/TypeScript

```typescript
import { apiService } from './services/api'

// Merge PDFs
const mergeResult = await apiService.mergePdfs({
  files: [file1, file2],
  outputFileName: 'merged.pdf'
})

// Split PDF
const splitResult = await apiService.splitPdf({
  file: pdfFile,
  splitMode: 'ranges',
  splitPoints: ['1-5', '6-10']
})

// Compress PDF
const compressResult = await apiService.compressPdf({
  file: pdfFile,
  compressionProfile: 'web'
})

// Convert to Image
const convertResult = await apiService.convertPdfToImage({
  file: pdfFile,
  imageFormat: 'png',
  dpi: 300,
  pages: '1-5'
})
```

## Development

### Running Tests

**Backend:**
```bash
cd backend
mvn test
```

**Frontend:**
```bash
cd frontend
npm test
```

### Building for Production

**Backend:**
```bash
cd backend
mvn clean package
java -jar target/pdf-editor-backend-1.0.0.jar
```

**Frontend:**
```bash
cd frontend
npm run build
# Output will be in dist/ directory
```

### Code Style

**Backend (Java):**
- Follow Oracle Java Code Conventions
- Use meaningful variable names
- Add JavaDoc comments to public methods
- Apply SOLID principles

**Frontend (TypeScript):**
- Use TypeScript for type safety
- Follow React Hooks best practices
- Use functional components
- CSS Modules for component styling

## Security Considerations

This project has undergone a comprehensive security audit. **IMPORTANT**: Before deploying to production, please review `SECURITY_AUDIT.md` for:

- Critical security vulnerabilities
- Input validation requirements
- File cleanup strategies
- Rate limiting implementation
- Authentication/authorization needs
- CORS configuration
- Error handling improvements

**Key Security Issues to Address:**
1. Input validation on all endpoints
2. Filename sanitization (path traversal prevention)
3. Automated file cleanup (prevent disk exhaustion)
4. Rate limiting (DoS prevention)
5. Authentication/authorization
6. Error message sanitization

See `SECURITY_AUDIT.md` for detailed remediation steps.

## Performance

### File Size Limits
- **Maximum upload size**: 100MB per file
- **Maximum request size**: 100MB total
- **Recommended file size**: < 50MB for optimal performance

### Timeout Settings
- **API timeout**: 2 minutes (120 seconds)
- **Processing timeout**: 5 minutes for large files

### Optimization Tips
- Use web compression profile for smaller files
- Convert only necessary pages to images
- Split large documents before processing
- Use appropriate DPI settings (150-300 for most cases)

## Troubleshooting

### Common Issues

**1. Backend fails to start - "Native library not found"**
```bash
# Ensure the library path is correct
ls backend/lib/osx-arm64/libPdfToolsSdk.dylib

# Remove quarantine attributes (macOS)
xattr -d com.apple.quarantine backend/lib/osx-arm64/libPdfToolsSdk.dylib
```

**2. License key error**
```bash
# Verify the license key is set
echo $PDFTOOLS_LICENSE_KEY

# Set it if missing
export PDFTOOLS_LICENSE_KEY="<PDFSDK,V1,YOUR_LICENSE_KEY>"
```

**3. CORS errors in frontend**
```bash
# Verify CORS origins in backend application.properties
cors.allowed-origins=http://localhost:5000

# Restart backend after changes
```

**4. File upload fails**
```bash
# Check file size limits in application.properties
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
```

**5. PDF Viewer not loading**
```bash
# Verify the Four Heights viewer license key in PDFViewer.tsx
const LICENSE_KEY = '<4H,V6,VIEWWEB,YOUR_LICENSE_KEY>'

# Check browser console for errors
```

## Deployment

### Current Production Setup

This application is deployed using:
- **Frontend**: [Vercel](https://vercel.com) - React app hosting
- **Backend**: [Railway](https://railway.app) - Spring Boot API hosting

### Deploy to Vercel (Frontend)

1. **Install Vercel CLI**:
   ```bash
   npm install -g vercel
   ```

2. **Link and deploy**:
   ```bash
   cd frontend
   vercel link
   vercel env add VITE_API_URL production
   # Enter: https://your-railway-backend.up.railway.app/api
   vercel --prod
   ```

### Deploy to Railway (Backend)

1. **Install Railway CLI**:
   ```bash
   npm install -g @railway/cli
   railway login
   ```

2. **Deploy**:
   ```bash
   cd backend
   railway link
   railway variables --set "PDFTOOLS_LICENSE_KEY=your-license-key"
   railway variables --set "CORS_ORIGINS=https://your-vercel-frontend.vercel.app"
   railway up
   ```

3. **Get your domain**:
   ```bash
   railway domain
   ```

### Environment Variables

**Backend (Railway)**:
| Variable | Description |
|----------|-------------|
| `PDFTOOLS_LICENSE_KEY` | PDF Tools SDK license key |
| `CORS_ORIGINS` | Comma-separated allowed origins |
| `PORT` | Server port (auto-set by Railway) |

**Frontend (Vercel)**:
| Variable | Description |
|----------|-------------|
| `VITE_API_URL` | Backend API URL |

### Docker Deployment (Alternative)

**Backend Dockerfile** (included in `backend/Dockerfile`):
```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY lib ./lib
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/lib/linux-x64 /app/lib/linux-x64
ENV LD_LIBRARY_PATH=/app/lib/linux-x64
EXPOSE 5001
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Docker Compose:**
```yaml
version: '3.8'
services:
  backend:
    build: ./backend
    ports:
      - "5001:5001"
    environment:
      - PDFTOOLS_LICENSE_KEY=${PDFTOOLS_LICENSE_KEY}
      - CORS_ORIGINS=http://localhost:5000
    volumes:
      - ./uploads:/app/uploads
      - ./outputs:/app/outputs

  frontend:
    build: ./frontend
    ports:
      - "5000:5000"
    depends_on:
      - backend
```

### Cloud Deployment Considerations

1. **File Storage**: Use cloud storage (S3, Google Cloud Storage) instead of local filesystem
2. **Load Balancing**: Use multiple backend instances behind a load balancer
3. **CDN**: Serve frontend through a CDN for better performance
4. **Monitoring**: Implement application monitoring (Prometheus, Grafana)
5. **Logging**: Centralized logging (ELK stack, CloudWatch)

## License

This project uses the PDF Tools SDK which requires a commercial license.

- **PDF Tools SDK**: Commercial license required from PDF Tools AG
- **Application Code**: [Your License Here]

## Support

- **Documentation**: See `USER_GUIDE.md` for detailed user instructions
- **Security**: Review `SECURITY_AUDIT.md` before production deployment
- **Backend Details**: See `backend/README.md`
- **Frontend Details**: See `frontend/README.md`
- **Issues**: [GitHub Issues](https://github.com/Victor-EU/pdftools-sdk-showcase/issues)

## Acknowledgments

- **PDF Tools AG** for the powerful PDF processing SDK
- **Spring Boot** team for the excellent framework
- **React** team for the UI library
- **Material-UI** for the component library

## Version History

### v1.1.0 (Current) - December 2025
- Production deployment on Vercel + Railway
- Added health check endpoint for monitoring
- Added Spring Boot Actuator
- CORS configuration for cross-origin requests
- Linux x64 native library support for Railway deployment

### v1.0.0 - November 2025
- Initial release
- PDF viewing and annotation
- Merge multiple PDFs
- Split PDFs by pages/ranges
- Compress PDFs with multiple profiles
- Convert PDFs to PNG/JPEG/TIFF
- Modern React frontend with Material-UI
- RESTful API backend with Spring Boot

## Roadmap

### Planned Features
- [ ] Batch processing queue
- [ ] User authentication and authorization
- [ ] Cloud storage integration (S3, Google Cloud Storage)
- [ ] OCR support for scanned documents
- [ ] Digital signature support
- [ ] Form field editing
- [ ] Watermarking
- [ ] Password protection/encryption
- [ ] Document comparison/diff
- [ ] Advanced redaction tools

## Contributing

[Add your contribution guidelines here]

## Authors

PDF Editor Team

---

**Built with ❤️ using PDF Tools SDK, Spring Boot, and React**
