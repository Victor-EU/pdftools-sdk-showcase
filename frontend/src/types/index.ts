/**
 * TypeScript Type Definitions
 */

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export interface FileResponse {
  fileName: string;
  filePath: string;
  fileSize: number;
  downloadUrl: string;
  originalSize?: number;
  compressionRatio?: number;
}

export interface MergeRequest {
  files: File[];
  outputFileName?: string;
}

export interface SplitRequest {
  file: File;
  splitMode: 'pages' | 'ranges';
  splitPoints: string[];
  outputFileNameBase?: string;
}

export interface CompressRequest {
  file: File;
  compressionProfile: 'web' | 'print' | 'custom';
  imageQuality?: number;
  outputFileName?: string;
}

export interface ConvertRequest {
  file: File;
  imageFormat: 'png' | 'jpeg' | 'tiff';
  dpi?: number;
  pages?: string;
  outputFileNameBase?: string;
}

export type OperationType = 'view' | 'merge' | 'split' | 'compress' | 'convert' | 'metadata' | 'extract' | 'validate-pdfa' | 'convert-pdfa';

export interface OperationStatus {
  type: OperationType;
  status: 'idle' | 'processing' | 'success' | 'error';
  progress?: number;
  message?: string;
  result?: FileResponse | FileResponse[];
}

// Metadata Extraction Types
export interface MetadataResponse {
  title: string | null;
  author: string | null;
  subject: string | null;
  keywords: string | null;
  creator: string | null;
  producer: string | null;
  creationDate: string | null;
  modificationDate: string | null;
  pdfVersion: string | null;
  pageCount: number;
  fileSize: number;
  isEncrypted: boolean;
  isLinearized: boolean;
  hasForms: boolean;
  isTagged: boolean;
  pdfaConformance: string | null;
}

// Data Extraction Types
export interface DataExtractionRequest {
  file: File;
  extractImages?: boolean;
  pages?: string;
}

export interface PageContent {
  pageNumber: number;
  text: string;
  wordCount: number;
}

export interface ExtractedImage {
  pageNumber: number;
  imageIndex: number;
  fileName: string;
  downloadUrl: string;
  fileSize: number;
  width: number;
  height: number;
  format: string;
}

export interface DataExtractionResponse {
  textContent: string;
  pages: PageContent[];
  imageCount: number;
  images: ExtractedImage[];
  tableCount: number;
  wordCount: number;
  characterCount: number;
}

// PDF/A Validation Types
export interface ValidationIssue {
  code: string;
  message: string;
  severity: string;
  pageNumber: number | null;
  objectType: string | null;
  context: string | null;
}

export interface PdfAValidationResponse {
  isCompliant: boolean;
  conformanceLevel: string | null;
  pdfaPart: number | null;
  pdfaLevel: string | null;
  errorCount: number;
  warningCount: number;
  errors: ValidationIssue[];
  warnings: ValidationIssue[];
  summary: string;
}

// PDF/A Conversion Types
export interface PdfAConversionRequest {
  file: File;
  conformanceLevel: string;
  outputFileName?: string;
  copyMetadata?: boolean;
  embedFonts?: boolean;
}
