# PDF Tools SDK Integration Guide

This document details the **complete PDF Tools SDK integration** in the codebase.

---

## ✅ SDK Integration Status - 100% COMPLETE

### ✅ Frontend - Viewer SDK

**Package**: `@pdf-tools/four-heights-pdf-web-viewer`
**License**: `<VIEWWEB,V5,H8A8N0HNQB4KUAHDBJ>`
**Status**: ✅ **FULLY INTEGRATED AND WORKING**

### ✅ Backend - Java SDK

**Package**: `pdftools-sdk-java-macos-arm64-1.14.0`
**License**: `<PDFSDK,V1,MGAAS0GPQFL3W2XUDBL>`
**Status**: ✅ **100% COMPLETE - ALL SERVICES IMPLEMENTED**

---

## 📍 Frontend SDK Integration - COMPLETE

### ✅ PDF Viewer Component

**File**: `frontend/src/components/PDFViewer/PDFViewer.tsx`
**Lines**: 1-154
**Status**: ✅ **FULLY FUNCTIONAL**

```typescript
import { PdfWebViewer } from '@pdf-tools/four-heights-pdf-web-viewer'
import '@pdf-tools/four-heights-pdf-web-viewer/css/pdf-web-viewer.css'

const LICENSE_KEY = '<VIEWWEB,V5,H8A8N0HNQB4KUAHDBJ>'

// Initialize viewer (lines 23-56)
useEffect(() => {
  const viewer = new PdfWebViewer(
    viewerContainerRef.current,
    LICENSE_KEY,
    {
      allowedInteractionModes: ['PanAndZoom', 'Annotate'],
      enableFileDrop: true,
    }
  )

  pdfViewerRef.current = viewer
  setIsViewerReady(true)

  return () => viewer.destroy()
}, [])

// Load PDF file (line 80)
pdfViewerRef.current.open({ data: file })
```

**Features Implemented**:
- ✅ Viewer initialization with license key
- ✅ File upload and viewing
- ✅ Pan and zoom controls
- ✅ Annotation support enabled
- ✅ Proper cleanup on unmount
- ✅ Error handling

**Documentation**: https://docs.pdf-tools.com/viewer/

---

## 📍 Backend SDK Integration - ALL COMPLETE

### ✅ 1. SDK Initialization

**File**: `backend/src/main/java/com/pdfeditor/config/PdfToolsConfig.java`
**Lines**: 33-61
**Status**: ✅ **WORKING**

```java
@PostConstruct
public void initialize() {
    try {
        logger.info("Initializing PDF Tools SDK...");

        // Load native library (line 40)
        String absoluteLibPath = new File(nativeLibPath).getAbsolutePath();
        System.load(absoluteLibPath + "/libPdfToolsSdk.dylib");

        // Initialize SDK with license key (line 45)
        com.pdftools.Sdk.initialize(licenseKey);
        logger.info("PDF Tools SDK initialized successfully");

        // Create working directories
        createDirectoryIfNotExists(uploadDir);
        createDirectoryIfNotExists(outputDir);

    } catch (Exception e) {
        logger.error("Failed to initialize PDF Tools SDK", e);
        throw new RuntimeException("PDF Tools SDK initialization failed", e);
    }
}
```

**Implementation Details**:
- ✅ Native library loaded from `lib/osx-arm64/libPdfToolsSdk.dylib`
- ✅ SDK initialized with license key
- ✅ Working directories created
- ✅ Proper error handling
- ✅ Tested and verified working

---

### ✅ 2. PDF Merge Service

**File**: `backend/src/main/java/com/pdfeditor/service/PdfMergeService.java`
**Lines**: 50-110
**Status**: ✅ **FULLY FUNCTIONAL**

```java
// Create output stream for merged PDF (line 82-96)
try (FileStream outputStream = new FileStream(outputFile.getAbsolutePath(), FileStream.Mode.READ_WRITE_NEW);
     DocumentAssembler docAssembler = new DocumentAssembler(outputStream)) {

    // Append all documents
    for (File tempFile : tempFiles) {
        try (FileStream inputStream = new FileStream(tempFile.getAbsolutePath(), FileStream.Mode.READ_ONLY);
             Document inputDocument = Document.open(inputStream, null)) {

            // Append entire document
            docAssembler.append(inputDocument);
        }
    }

    // Create the final structure of the output PDF
    docAssembler.assemble();
}
```

**SDK Classes Used**:
- ✅ `com.pdftools.pdf.Document`
- ✅ `com.pdftools.documentassembly.DocumentAssembler`
- ✅ `com.pdftools.sys.FileStream`

**Features**:
- ✅ Merges multiple PDFs into one
- ✅ Proper file stream handling
- ✅ Resource cleanup with try-with-resources
- ✅ Error handling and logging
- ✅ Tested and working

---

### ✅ 3. PDF Split Service

**File**: `backend/src/main/java/com/pdfeditor/service/PdfSplitService.java`
**Lines**: 51-200
**Status**: ✅ **FULLY FUNCTIONAL WITH ENHANCED VALIDATION**

```java
// Open source document (line 67-70)
FileStream sourceStream = new FileStream(tempFile.getAbsolutePath(), FileStream.Mode.READ_ONLY);
Document sourceDoc = Document.open(sourceStream, null);
int totalPages = sourceDoc.getPageCount();

// Split by ranges (lines 107-154)
private List<FileResponse> splitByRanges(Document sourceDoc, List<String> ranges, String baseFileName) {
    int totalPages = sourceDoc.getPageCount();

    for (String range : ranges) {
        String[] parts = range.split("-");
        int startPage = Integer.parseInt(parts[0].trim());
        int endPage = (parts.length > 1) ? Integer.parseInt(parts[1].trim()) : startPage;

        // Enhanced validation (lines 128-137)
        if (startPage < 1 || startPage > totalPages) {
            String errorMsg = "Start page " + startPage + " is out of range. PDF has " + totalPages + " pages (valid range: 1-" + totalPages + ")";
            throw new PdfProcessingException(errorMsg);
        }

        // Extract pages using DocumentAssembler (lines 140-151)
        try (FileStream outputStream = new FileStream(outputFile.getAbsolutePath(), FileStream.Mode.READ_WRITE_NEW);
             DocumentAssembler docAssembler = new DocumentAssembler(outputStream)) {

            PageCopyOptions options = new PageCopyOptions();
            docAssembler.appendPages(sourceDoc, startPage, endPage, options);
            docAssembler.assemble();
        }
    }
}
```

**SDK Classes Used**:
- ✅ `com.pdftools.pdf.Document`
- ✅ `com.pdftools.documentassembly.DocumentAssembler`
- ✅ `com.pdftools.documentassembly.PageCopyOptions`
- ✅ `com.pdftools.sys.FileStream`

**Features**:
- ✅ Split by page ranges or specific pages
- ✅ Enhanced validation with clear error messages
- ✅ 1-based page indexing correctly handled
- ✅ Proper resource management
- ✅ Tested and working

---

### ✅ 4. PDF Compress Service

**File**: `backend/src/main/java/com/pdfeditor/service/PdfCompressService.java`
**Lines**: 51-150
**Status**: ✅ **FULLY FUNCTIONAL**

```java
// Open source document (line 67-68)
FileStream inputStream = new FileStream(tempFile.getAbsolutePath(), FileStream.Mode.READ_ONLY);
Document sourceDoc = Document.open(inputStream, null);

// Select compression profile (line 82)
Profile profile = selectCompressionProfile(compressRequest);

// Create optimizer and optimize (lines 85-93)
Optimizer optimizer = new Optimizer();
FileStream outputStream = new FileStream(outputFile.getAbsolutePath(), FileStream.Mode.READ_WRITE_NEW);
Document optimizedDoc = optimizer.optimizeDocument(sourceDoc, outputStream, profile);

optimizedDoc.close();
outputStream.close();

// Calculate compression ratio (lines 95-96)
long compressedSize = outputFile.length();
double compressionRatio = ((originalSize - compressedSize) / (double) originalSize) * 100;
```

**Compression Profiles** (lines 123-133):
```java
private Profile selectCompressionProfile(CompressRequest request) {
    switch (request.compressionProfile.toLowerCase()) {
        case "web":
            return new Web();  // Optimized for web viewing
        case "print":
            return new Print();  // High quality for printing
        default:
            return new Web();
    }
}
```

**SDK Classes Used**:
- ✅ `com.pdftools.pdf.Document`
- ✅ `com.pdftools.optimization.Optimizer`
- ✅ `com.pdftools.optimization.profiles.Profile`
- ✅ `com.pdftools.optimization.profiles.Web`
- ✅ `com.pdftools.optimization.profiles.Print`
- ✅ `com.pdftools.sys.FileStream`

**Features**:
- ✅ Web profile for online viewing
- ✅ Print profile for high-quality output
- ✅ Compression ratio calculation
- ✅ Tested and working

---

### ✅ 5. PDF Convert Service

**File**: `backend/src/main/java/com/pdfeditor/service/PdfConvertService.java`
**Lines**: 53-200
**Status**: ✅ **FULLY FUNCTIONAL**

```java
// Open source document (line 69-71)
FileStream inputStream = new FileStream(tempFile.getAbsolutePath(), FileStream.Mode.READ_ONLY);
Document sourceDoc = Document.open(inputStream, null);
int totalPages = sourceDoc.getPageCount();

// Create converter (line 79)
Converter converter = new Converter();

// Convert each page (lines 82-96)
for (int pageNum : pagesToConvert) {
    File outputFile = new File(outputDir, outputFileName);
    renderPageToImage(converter, sourceDoc, pageNum, outputFile, convertRequest);
}

// Render page to image (lines 126-146)
private void renderPageToImage(Converter converter, Document sourceDoc, int pageNum,
                               File outputFile, ConvertRequest request) throws Exception {
    Profile profile = createConversionProfile(request);
    FileStream outputStream = new FileStream(outputFile.getAbsolutePath(), FileStream.Mode.READ_WRITE_NEW);

    try {
        // Convert single page using profiles (line 136)
        converter.convertPage(sourceDoc, outputStream, profile, pageNum);
        logger.debug("Successfully converted page {} to {}", pageNum, outputFile.getName());
    } finally {
        outputStream.close();
    }
}

// Conversion profiles (lines 154-160)
private Profile createConversionProfile(ConvertRequest request) {
    if ("tiff".equalsIgnoreCase(request.imageFormat)) {
        return new Archive();  // 300 DPI, high quality for archival
    } else {
        return new Viewing();  // 150 DPI, web-optimized for PNG/JPEG
    }
}
```

**SDK Classes Used**:
- ✅ `com.pdftools.pdf.Document`
- ✅ `com.pdftools.pdf2image.Converter`
- ✅ `com.pdftools.pdf2image.profiles.Profile`
- ✅ `com.pdftools.pdf2image.profiles.Viewing`
- ✅ `com.pdftools.pdf2image.profiles.Archive`
- ✅ `com.pdftools.sys.FileStream`

**Features**:
- ✅ PNG format support (Viewing profile, 150 DPI)
- ✅ JPEG format support (Viewing profile, 150 DPI)
- ✅ TIFF format support (Archive profile, 300 DPI)
- ✅ Page range selection
- ✅ Individual page conversion
- ✅ Tested and working

---

## 📊 Integration Summary

| Component | File | Status | Lines |
|-----------|------|--------|-------|
| **SDK Init** | PdfToolsConfig.java | ✅ Complete | 33-61 |
| **Merge** | PdfMergeService.java | ✅ Complete | 50-110 |
| **Split** | PdfSplitService.java | ✅ Complete | 51-200 |
| **Compress** | PdfCompressService.java | ✅ Complete | 51-150 |
| **Convert** | PdfConvertService.java | ✅ Complete | 53-200 |
| **Viewer** | PDFViewer.tsx | ✅ Complete | 1-154 |

---

## 🎯 All SDK Features Implemented

### Backend Operations ✅
- ✅ **Merge PDFs**: Combines multiple documents using DocumentAssembler
- ✅ **Split PDF**: Extracts pages by ranges or specific pages
- ✅ **Compress PDF**: Optimizes with Web/Print profiles
- ✅ **Convert to Image**: Renders to PNG/JPEG (150 DPI) or TIFF (300 DPI)

### Frontend Features ✅
- ✅ **PDF Viewing**: Four Heights Web Viewer fully integrated
- ✅ **Annotations**: Pan, zoom, and annotate enabled
- ✅ **File Upload**: Drag-and-drop and click-to-upload
- ✅ **Error Handling**: Comprehensive validation and user feedback

---

## 🔧 Configuration

### Backend License Key
Set via environment variable:
```bash
export PDFTOOLS_LICENSE_KEY="<PDFSDK,V1,MGAAS0GPQFL3W2XUDBL>"
```

Or in `application.properties`:
```properties
pdftools.sdk.license-key=<PDFSDK,V1,MGAAS0GPQFL3W2XUDBL>
pdftools.sdk.native-lib-path=./lib/osx-arm64
```

### Frontend License Key
Configured in `PDFViewer.tsx`:
```typescript
const LICENSE_KEY = '<VIEWWEB,V5,H8A8N0HNQB4KUAHDBJ>'
```

### Static Assets
Located in `public/pdfwebviewer/`:
- PdfViewing_Main.wasm (1.9 MB)
- PdfViewing_Worker.wasm (9.4 MB)
- PdfViewing_Main.js
- PdfViewing_Worker.js
- pdf-web-viewer.css

Base URL configured in `index.html`:
```javascript
window.PDFTOOLS_FOURHEIGHTS_PDFVIEWING_BASEURL = '/pdfwebviewer/'
```

---

## 📖 Documentation References

- **PDF Tools SDK Java**: https://docs.pdf-tools.com/sdk/java/
- **PDF Tools Web Viewer**: https://docs.pdf-tools.com/viewer/
- **Convert PDF to Image**: https://docs.pdf-tools.com/sdk/convert/
- **Optimization Profiles**: https://docs.pdf-tools.com/sdk/optimization/

---

## ✅ Testing Status

All PDF operations have been tested and verified working:

1. ✅ **PDF Viewer**: Files upload and display correctly
2. ✅ **Merge**: Multiple PDFs combine successfully
3. ✅ **Split**: Pages extract correctly with validation
4. ✅ **Compress**: File size reduces with Web/Print profiles
5. ✅ **Convert**: PDFs render to PNG/JPEG/TIFF images

---

## 🎉 Completion Status

**Backend SDK Integration**: ✅ **100% COMPLETE**
**Frontend SDK Integration**: ✅ **100% COMPLETE**
**All Services**: ✅ **TESTED AND WORKING**

The PDF Tools SDK is **fully integrated** and all operations are **production-ready**.

---

**Powered by PDFTools' SDK** ✨
