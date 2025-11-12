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

export type OperationType = 'view' | 'merge' | 'split' | 'compress' | 'convert';

export interface OperationStatus {
  type: OperationType;
  status: 'idle' | 'processing' | 'success' | 'error';
  progress?: number;
  message?: string;
  result?: FileResponse | FileResponse[];
}
