# Claude Code Guidelines - PDF Editor Web Application

## Project Overview
These guidelines ensure consistent, maintainable, and scalable development of a web-based PDF editing application using PDF Tools SDK.

## Core Requirements

### 1. PDF Tools SDK Integration
- **Always use PDF Tools SDK** for all PDF-related operations
- **Don't use open source PDF libraries** (e.g., PDF.js, pdfkit, jsPDF, react-pdf, etc.)
- Rely exclusively on PDF Tools SDK for complete functionality and support

### 2. Object-Oriented Programming (OOP) Principles

#### SOLID Principles
- **Single Responsibility**: Each class should have one reason to change
- **Open/Closed**: Classes should be open for extension, closed for modification
- **Liskov Substitution**: Derived classes must be substitutable for base classes
- **Interface Segregation**: Prefer specific interfaces over general-purpose ones
- **Dependency Inversion**: Depend on abstractions, not concretions


### 3. Microservices Architecture

#### Service Decomposition
- **PDF Processing Service**: Handles all PDF manipulation operations

#### API Design
- Use RESTful principles for service communication
- Implement API versioning (e.g., `/api/v1/`)
- Use consistent HTTP status codes
- Implement proper error handling and messaging

#### Service Communication
```javascript
// API Gateway pattern
const APIGateway = {
  pdf: 'https://api.domain.com/pdf-service'
};
```

### 4. Code Organization

#### Project Structure
```
/src
  /components
    /pdf-viewer
    /pdf-editor
    /annotations
  /services
    /pdf-service
  /models
  /utils
  /hooks (for React)
  /store (state management)
  /styles
  /tests
  /types (TypeScript)
```

### 5. State Management
- Use centralized state management (Redux, MobX, or Zustand)
- Implement proper state immutability
- Separate UI state from business logic state

### 6. Error Handling

#### Global Error Boundary
```javascript
class ErrorBoundary {
  static logError(error, errorInfo) {
    // Log to monitoring service
    console.error('Error:', error, errorInfo);
  }
  
  static handlePDFError(error) {
    // Specific PDF error handling
    return {
      type: 'PDF_ERROR',
      message: this.getUserFriendlyMessage(error),
      code: error.code
    };
  }
}
```

### 7. Performance Optimization

#### PDF Loading Strategy
- Implement lazy loading for PDF pages
- Use virtualization for large documents
- Cache rendered pages
- Implement progressive loading

#### Code Optimization
- Use code splitting and dynamic imports
- Implement proper memoization
- Optimize bundle size
- Use Web Workers for heavy PDF operations

### 8. Security Best Practices

#### Application Security
- Implement Content Security Policy (CSP)
- Use HTTPS for all communications
- Sanitize user inputs
- Implement rate limiting

### 9. Testing Strategy

#### Test Coverage Requirements
- Unit tests: Minimum 80% coverage
- Integration tests for all API endpoints
- E2E tests for critical user workflows
- Performance tests for PDF operations

#### Testing Structure
```javascript
describe('PDFEditor', () => {
  describe('Annotation Features', () => {
    it('should add text annotation', () => {});
    it('should delete annotation', () => {});
    it('should update annotation properties', () => {});
  });
});
```

### 10. Documentation Standards

#### Code Documentation
```javascript
/**
 * Loads and renders a PDF document
 * @param {string} documentId - Unique identifier for the PDF
 * @param {Object} options - Rendering options
 * @param {number} options.scale - Zoom level (default: 1.0)
 * @param {number} options.rotation - Rotation angle (0, 90, 180, 270)
 * @returns {Promise<PDFDocument>} Loaded PDF document instance
 * @throws {PDFLoadError} If document cannot be loaded
 */
async function loadPDFDocument(documentId, options = {}) {
  // Implementation
}
```

### 11. Version Control & CI/CD

#### Git Workflow
- Use feature branches
- Implement conventional commits
- Require PR reviews before merging
- Maintain a clean commit history

#### CI/CD Pipeline
```yaml
# Example pipeline stages
stages:
  - lint
  - test
  - build
  - security-scan
  - deploy
```

### 12. Monitoring and Logging

#### Logging Strategy
```javascript
const logger = {
  info: (message, metadata) => {},
  warn: (message, metadata) => {},
  error: (message, error, metadata) => {},
  debug: (message, metadata) => {}
};

// Usage
logger.info('PDF loaded successfully', { 
  documentId, 
  pageCount, 
  loadTime 
});
```

### 13. Internationalization (i18n)
- Externalize all user-facing strings
- Support RTL languages
- Handle date/time formatting
- Consider PDF text direction

## Development Workflow

### Pre-Development Checklist
- [ ] Review PDF Tools SDK documentation
- [ ] Identify required microservices
- [ ] Define API contracts
- [ ] Set up CSS architecture
- [ ] Configure development environment

### Code Review Checklist
- [ ] Follows OOP principles
- [ ] Uses PDF Tools SDK appropriately
- [ ] CSS uses centralized variables
- [ ] Proper error handling implemented
- [ ] Tests written and passing
- [ ] Documentation complete
- [ ] Security considerations addressed
- [ ] Performance optimized

## Conclusion
These guidelines ensure the development of a robust, scalable, and maintainable PDF editing application. Always prioritize code quality, user experience, and security while leveraging the full capabilities of the PDF Tools SDK.