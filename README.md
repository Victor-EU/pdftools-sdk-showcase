# PDF Editor 📄

<div align="center">

[![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)](https://reactjs.org/)
[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Material-UI](https://img.shields.io/badge/Material--UI-0081CB?style=for-the-badge&logo=mui&logoColor=white)](https://mui.com/)

**A modern, full-stack PDF editing application with a beautiful UI**

[Features](#-features) • [Architecture](#-architecture) • [Quick Start](#-quick-start) • [Documentation](#-documentation) • [License](#-license)

</div>

---

## 🌟 Overview

PDF Editor is a comprehensive web application that provides professional-grade PDF manipulation capabilities. Built with modern technologies and following industry best practices, it offers an intuitive interface for viewing, annotating, merging, splitting, compressing, and converting PDF documents.

### ✨ Key Highlights

- 🎨 **Modern UI Design** - Clean, light blue interface inspired by pdftools.com
- ⚡ **High Performance** - Powered by PDF Tools SDK for enterprise-grade processing
- 🏗️ **Scalable Architecture** - Separation of concerns with clean code principles
- 🔒 **Type-Safe** - Full TypeScript implementation on frontend
- 📱 **Responsive Design** - Works seamlessly across devices
- 🎯 **OOP Best Practices** - Well-structured, maintainable codebase

---

## 🚀 Features

### PDF Viewing & Annotation
- **View** PDFs with high-fidelity rendering
- **Annotate** with highlights, comments, and drawings
- **Redact** sensitive information securely
- Powered by `@pdf-tools/four-heights-pdf-web-viewer`

### PDF Operations
| Feature | Description | API Endpoint |
|---------|-------------|--------------|
| **Merge** | Combine multiple PDFs into one | `POST /api/merge` |
| **Split** | Divide PDF by pages or ranges | `POST /api/split` |
| **Compress** | Reduce file size (web/print profiles) | `POST /api/compress` |
| **Convert** | Export to PNG, JPEG, or TIFF | `POST /api/convert` |

---

## 🏛️ Architecture

### System Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    React Frontend (Port 3000)                │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  Components (TypeScript + CSS Modules)                │  │
│  │  • PDFViewer    • MergePanel   • SplitPanel          │  │
│  │  • CompressPanel • ConvertPanel                      │  │
│  └──────────────────────┬────────────────────────────────┘  │
│  ┌──────────────────────▼────────────────────────────────┐  │
│  │  Services Layer                                       │  │
│  │  • API Client (Axios)  • Type Definitions            │  │
│  └──────────────────────┬────────────────────────────────┘  │
│  ┌──────────────────────▼────────────────────────────────┐  │
│  │  UI Framework                                         │  │
│  │  • Material-UI  • MUI Theme (Light Blue)             │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────┬───────────────────────────────────────┘
                      │ REST API (Axios)
                      ▼
┌─────────────────────────────────────────────────────────────┐
│            Spring Boot Backend (Port 8080)                   │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  REST Controllers                                     │  │
│  │  MergeController │ SplitController │ CompressController│ │
│  └──────────────────────┬────────────────────────────────┘  │
│  ┌──────────────────────▼────────────────────────────────┐  │
│  │  Service Layer (Business Logic)                      │  │
│  │  • PdfMergeService    • PdfSplitService              │  │
│  │  • PdfCompressService • PdfConvertService            │  │
│  └──────────────────────┬────────────────────────────────┘  │
│  ┌──────────────────────▼────────────────────────────────┐  │
│  │  PDF Tools SDK Integration                           │  │
│  │  • DocumentAssembler  • Optimizer  • Converter       │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Technology Stack

#### Frontend
- **React 18** - Modern component-based UI
- **TypeScript** - Type safety and better developer experience
- **Vite** - Lightning-fast development and build tool
- **Material-UI** - Comprehensive React component library
- **PDF Tools Viewer** - Professional PDF viewing and annotation
- **Axios** - Promise-based HTTP client
- **CSS Modules** - Scoped component styling

#### Backend
- **Java 17** - Modern LTS Java version
- **Spring Boot 3.2** - Industry-standard framework
- **PDF Tools SDK** - Enterprise-grade PDF processing
- **Maven** - Dependency management and build automation
- **Lombok** - Reduces boilerplate code

### CSS Architecture

Our CSS architecture is designed for **scalability** and **maintainability**:

#### Global CSS (`src/styles/`)
- **variables.css** - Design tokens (colors, spacing, typography)
- **globals.css** - Base styles and CSS resets
- **typography.css** - Font loading and text utilities
- **utilities.css** - Utility classes for rapid development

#### Local CSS (CSS Modules)
- Component-scoped styles (`ComponentName.module.css`)
- No global namespace pollution
- Type-safe className references in TypeScript

**Example:**
```css
/* Button.module.css */
.button {
  padding: var(--spacing-sm) var(--spacing-lg);
  background-color: var(--color-primary);
  border-radius: var(--radius-md);
  transition: all var(--transition-base);
}
```

```tsx
// Button.tsx
import styles from './Button.module.css'
export const Button = () => <button className={styles.button}>Click</button>
```

---

## 🎯 Quick Start

### Prerequisites

- **Node.js** 18+ and npm
- **Java** 17+
- **Maven** 3.6+
- **macOS** with ARM64 architecture (for PDF Tools SDK)

### Installation

#### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/pdf-editor.git
cd pdf-editor
```

#### 2. Backend Setup

```bash
cd backend

# Install dependencies
mvn clean install

# Set license key (required)
export PDFTOOLS_LICENSE_KEY="<PDFSDK,V1,MGAAS0GPQFL3W2XUDBL>"

# Run the backend
mvn spring-boot:run
```

Backend will start at `http://localhost:8080/api`

#### 3. Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Run development server
npm run dev
```

Frontend will start at `http://localhost:3000`

### 🎬 Using the Application

1. **Open** `http://localhost:3000` in your browser
2. **Select** an operation tab (View, Merge, Split, Compress, Convert)
3. **Upload** your PDF file(s)
4. **Configure** operation settings
5. **Process** and download the result

---

## 📚 Documentation

### API Reference

#### Merge PDFs

```bash
curl -X POST http://localhost:8080/api/merge \
  -F "files=@document1.pdf" \
  -F "files=@document2.pdf" \
  -F "outputFileName=merged.pdf"
```

**Response:**
```json
{
  "success": true,
  "message": "PDF files merged successfully",
  "data": {
    "fileName": "merged.pdf",
    "fileSize": 1048576,
    "downloadUrl": "/download/merged.pdf"
  }
}
```

#### Split PDF

```bash
curl -X POST http://localhost:8080/api/split \
  -F "file=@document.pdf" \
  -F "splitMode=ranges" \
  -F "splitPoints=1-3" \
  -F "splitPoints=4-6"
```

#### Compress PDF

```bash
curl -X POST http://localhost:8080/api/compress \
  -F "file=@document.pdf" \
  -F "compressionProfile=web"
```

#### Convert to Image

```bash
curl -X POST http://localhost:8080/api/convert \
  -F "file=@document.pdf" \
  -F "imageFormat=png" \
  -F "dpi=300" \
  -F "pages=1-3"
```

### Project Structure

```
pdf-editor/
├── backend/                     # Spring Boot backend
│   ├── src/main/java/com/pdfeditor/
│   │   ├── controller/         # REST API endpoints
│   │   ├── service/            # Business logic
│   │   ├── config/             # Spring configuration
│   │   ├── dto/                # Data Transfer Objects
│   │   └── exception/          # Error handling
│   ├── lib/                    # PDF Tools SDK
│   └── pom.xml
│
├── frontend/                    # React frontend
│   ├── src/
│   │   ├── components/         # React components
│   │   ├── services/           # API services
│   │   ├── theme/              # MUI theme
│   │   ├── types/              # TypeScript definitions
│   │   └── styles/             # Global CSS
│   ├── package.json
│   └── vite.config.ts
│
└── README.md
```

---

## 🎨 Design System

### Color Palette

| Color | Hex | Usage |
|-------|-----|-------|
| Primary Blue | `#4A9BD1` | Main actions, headers |
| Light Blue | `#6BB6E8` | Hover states, highlights |
| Dark Blue | `#3A7FAF` | Active states, borders |
| Background | `#F5F9FC` | Page background |
| Text Primary | `#2C3E50` | Headings, important text |
| Text Secondary | `#5A6C7D` | Body text |

### Typography

- **Font Family**: Source Sans Pro
- **Weights**: 300 (Light), 400 (Regular), 600 (Semibold), 700 (Bold)
- **Scale**: Base 16px, modular scale for headings

### Spacing

```
xs:  4px
sm:  8px
md:  16px
lg:  24px
xl:  32px
2xl: 48px
```

---

## 🛠️ Development

### Code Style

- **Java**: Follow Spring Boot conventions, use Lombok for boilerplate reduction
- **TypeScript**: Strict mode enabled, functional components with hooks
- **CSS**: BEM methodology in module.css files, use CSS variables
- **Commits**: Conventional Commits format

### Testing

```bash
# Backend tests
cd backend
mvn test

# Frontend tests
cd frontend
npm run test
```

### Building for Production

```bash
# Backend
cd backend
mvn clean package

# Frontend
cd frontend
npm run build
```

---

## 🔐 License Keys

This project requires valid PDF Tools SDK license keys:

- **Backend SDK**: Set via `PDFTOOLS_LICENSE_KEY` environment variable
- **Viewer SDK**: Configure in frontend viewer initialization

**Note**: The keys included in this repository are for demonstration purposes. For production use, obtain your own licenses from [PDF Tools AG](https://www.pdf-tools.com/).

---

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Code Review Checklist

- [ ] Code follows project style guidelines
- [ ] All tests pass
- [ ] New features include tests
- [ ] Documentation is updated
- [ ] No console errors or warnings
- [ ] TypeScript types are properly defined

---

## 📝 Roadmap

- [x] ✅ Complete PDF viewer integration with Four Heights SDK
- [x] ✅ All PDF operations (merge, split, compress, convert) fully working
- [x] ✅ PDF to image conversion with multiple formats
- [x] ✅ Enhanced error handling and validation
- [ ] Add drag-and-drop file upload enhancements
- [ ] Implement progress bars for long operations
- [ ] Add batch processing support
- [ ] User authentication and file storage
- [ ] Docker containerization
- [ ] CI/CD pipeline
- [ ] Comprehensive test coverage
- [ ] Accessibility improvements (WCAG 2.1 AA)
- [ ] Internationalization (i18n)

---

## ✅ Implemented Features

All core PDF operations are **fully functional**:

- ✅ **PDF Viewing & Annotation** - Complete with Four Heights SDK
- ✅ **Merge PDFs** - Combines multiple documents
- ✅ **Split PDF** - By ranges or specific pages with validation
- ✅ **Compress PDF** - Web and Print optimization profiles
- ✅ **Convert to Images** - PNG, JPEG, TIFF with DPI control

See [Issues](https://github.com/yourusername/pdf-editor/issues) for enhancement requests.

---

## 📧 Support

- **Documentation**: See `/backend/README.md` and `/frontend/README.md`
- **Issues**: [GitHub Issues](https://github.com/yourusername/pdf-editor/issues)
- **Email**: support@yourproject.com

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

**PDF Tools SDK License**: Separate licensing required from [PDF Tools AG](https://www.pdf-tools.com/)

---

## 🙏 Acknowledgments

- [PDF Tools AG](https://www.pdf-tools.com/) for their powerful SDK
- [Material-UI](https://mui.com/) for the excellent component library
- [Spring Boot](https://spring.io/projects/spring-boot) for the robust backend framework
- [Vite](https://vitejs.dev/) for the blazing fast build tool

---

<div align="center">

**Powered by PDFTools' SDK**

⭐ Star this repo if you find it helpful!

[Report Bug](https://github.com/yourusername/pdf-editor/issues) • [Request Feature](https://github.com/yourusername/pdf-editor/issues)

</div>
