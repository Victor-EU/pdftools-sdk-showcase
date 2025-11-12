# Contributing to PDF Editor

Thank you for your interest in contributing to PDF Editor! This document provides guidelines and instructions for contributing.

## 🎯 Project Overview

PDF Editor is a full-stack web application for PDF manipulation, built with:
- **Frontend**: React 18 + TypeScript + Material-UI + Vite
- **Backend**: Spring Boot 3.2 + Java 17 + PDF Tools SDK

## 🚀 Getting Started

### Prerequisites

- Node.js 18+ and npm
- Java 17+
- Maven 3.6+
- macOS ARM64 (for PDF Tools SDK)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/pdf-editor.git
   cd pdf-editor
   ```

2. **Frontend Setup**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

3. **Backend Setup** (requires PDF Tools SDK integration)
   ```bash
   cd backend
   export PDFTOOLS_LICENSE_KEY="your-license-key"
   mvn clean install
   mvn spring-boot:run
   ```

## 📝 Development Guidelines

### Code Style

#### TypeScript/React
- Use **functional components** with hooks
- Follow **React best practices**
- Use **TypeScript strict mode**
- Add **JSDoc comments** for complex functions
- Use **CSS Modules** for component styling

#### Java/Spring Boot
- Follow **Spring Boot conventions**
- Use **Lombok** to reduce boilerplate
- Add **JavaDoc** to all public methods
- Follow **SOLID principles**
- Use **dependency injection**

### CSS Architecture

We use a **dual-CSS** architecture:

**Global CSS** (`frontend/src/styles/`):
- `variables.css` - Design tokens
- `globals.css` - Base styles
- `typography.css` - Text styles
- `utilities.css` - Helper classes

**Local CSS** (Component modules):
```
ComponentName/
├── ComponentName.tsx
└── ComponentName.module.css
```

### Commit Messages

Use **Conventional Commits** format:
```
feat: add PDF merge functionality
fix: resolve file upload issue
docs: update README with setup instructions
style: format code according to style guide
refactor: reorganize service layer
test: add unit tests for PdfMergeService
```

## 🏗️ Project Structure

```
pdf-editor/
├── frontend/          # React application
│   ├── src/
│   │   ├── components/    # React components
│   │   ├── services/      # API services
│   │   ├── theme/         # MUI theme
│   │   └── styles/        # Global CSS
│   └── package.json
│
└── backend/           # Spring Boot application
    ├── src/main/java/com/pdfeditor/
    │   ├── controller/    # REST endpoints
    │   ├── service/       # Business logic
    │   ├── config/        # Configuration
    │   └── dto/           # Data transfer objects
    └── pom.xml
```

## 🔧 Development Workflow

1. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes**
   - Write clean, documented code
   - Follow the code style guidelines
   - Add tests for new features

3. **Test your changes**
   ```bash
   # Frontend
   cd frontend
   npm run lint
   npm run build

   # Backend
   cd backend
   mvn test
   mvn clean package
   ```

4. **Commit your changes**
   ```bash
   git add .
   git commit -m "feat: description of your changes"
   ```

5. **Push and create PR**
   ```bash
   git push origin feature/your-feature-name
   ```
   Then create a Pull Request on GitHub

## 📋 Pull Request Checklist

Before submitting a PR, ensure:

- [ ] Code follows project style guidelines
- [ ] All tests pass
- [ ] New features include tests
- [ ] Documentation is updated
- [ ] No console errors or warnings
- [ ] TypeScript types are properly defined
- [ ] Commit messages follow Conventional Commits
- [ ] PR description explains the changes

## 🎨 Design Guidelines

### Color Palette

- **Primary**: `#4A9BD1` (light blue)
- **Secondary**: `#5A6C7D` (dark gray-blue)
- **Background**: `#F5F9FC` (light blue-gray)
- **Text**: `#2C3E50` (dark blue-gray)

### Typography

- **Font**: Source Sans Pro
- **Sizes**: Use CSS variables (`--font-size-*`)
- **Weights**: 300, 400, 600, 700

### Spacing

Use CSS variables:
- `--spacing-xs` (4px)
- `--spacing-sm` (8px)
- `--spacing-md` (16px)
- `--spacing-lg` (24px)
- `--spacing-xl` (32px)

## 🐛 Reporting Bugs

Use GitHub Issues with the bug template:

**Title**: Clear, concise description

**Description**:
- Steps to reproduce
- Expected behavior
- Actual behavior
- Screenshots (if applicable)
- Browser/OS information

## 💡 Feature Requests

Use GitHub Issues with the feature request template:

- Describe the feature
- Explain the use case
- Suggest implementation (optional)

## 🏆 Areas for Contribution

### High Priority
- [ ] Complete PDF Tools SDK integration
- [ ] Implement all operation components
- [ ] Add comprehensive tests
- [ ] Add drag-and-drop file upload

### Medium Priority
- [ ] Add progress indicators
- [ ] Implement error boundaries
- [ ] Add dark mode support
- [ ] Improve accessibility (WCAG 2.1 AA)

### Low Priority
- [ ] Add internationalization (i18n)
- [ ] Create Docker containers
- [ ] Add CI/CD pipeline
- [ ] Performance optimizations

## 📚 Resources

- [React Documentation](https://reactjs.org/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Material-UI Docs](https://mui.com/)
- [Spring Boot Reference](https://spring.io/projects/spring-boot)
- [PDF Tools SDK Docs](https://docs.pdf-tools.com/)

## 📧 Questions?

- Open a GitHub Discussion
- Check existing Issues
- Review project documentation

## 📄 License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

**Thank you for contributing to PDF Editor!** 🙏

Your contributions help make this project better for everyone.
