/**
 * API Service Layer
 * Handles all HTTP requests to the backend
 */

import axios, { AxiosInstance } from 'axios';
import {
  ApiResponse,
  FileResponse,
  MergeRequest,
  SplitRequest,
  CompressRequest,
  ConvertRequest,
  MetadataResponse,
  DataExtractionRequest,
  DataExtractionResponse,
  PdfAValidationResponse,
  PdfAConversionRequest,
} from '../types';

class ApiService {
  private client: AxiosInstance;

  constructor() {
    const baseURL = import.meta.env.VITE_API_URL || '/api';
    this.client = axios.create({
      baseURL,
      timeout: 300000,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  }

  async mergePdfs(request: MergeRequest): Promise<ApiResponse<FileResponse>> {
    const formData = new FormData();
    request.files.forEach((file) => {
      formData.append('files', file);
    });
    if (request.outputFileName) {
      formData.append('outputFileName', request.outputFileName);
    }

    const response = await this.client.post<ApiResponse<FileResponse>>('/merge', formData);
    return response.data;
  }

  async splitPdf(request: SplitRequest): Promise<ApiResponse<FileResponse[]>> {
    const formData = new FormData();
    formData.append('file', request.file);
    formData.append('splitMode', request.splitMode);
    request.splitPoints.forEach((point) => {
      formData.append('splitPoints', point);
    });
    if (request.outputFileNameBase) {
      formData.append('outputFileNameBase', request.outputFileNameBase);
    }

    const response = await this.client.post<ApiResponse<FileResponse[]>>('/split', formData);
    return response.data;
  }

  async compressPdf(request: CompressRequest): Promise<ApiResponse<FileResponse>> {
    const formData = new FormData();
    formData.append('file', request.file);
    formData.append('compressionProfile', request.compressionProfile);
    if (request.imageQuality) {
      formData.append('imageQuality', request.imageQuality.toString());
    }
    if (request.outputFileName) {
      formData.append('outputFileName', request.outputFileName);
    }

    const response = await this.client.post<ApiResponse<FileResponse>>('/compress', formData);
    return response.data;
  }

  async convertPdfToImage(request: ConvertRequest): Promise<ApiResponse<FileResponse[]>> {
    const formData = new FormData();
    formData.append('file', request.file);
    formData.append('imageFormat', request.imageFormat);
    if (request.dpi) {
      formData.append('dpi', request.dpi.toString());
    }
    if (request.pages) {
      formData.append('pages', request.pages);
    }
    if (request.outputFileNameBase) {
      formData.append('outputFileNameBase', request.outputFileNameBase);
    }

    const response = await this.client.post<ApiResponse<FileResponse[]>>('/convert', formData);
    return response.data;
  }

  async downloadFile(filename: string): Promise<Blob> {
    const response = await this.client.get(`/download/${filename}`, {
      responseType: 'blob',
    });
    return response.data;
  }

  triggerDownload(blob: Blob, filename: string) {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  }

  /**
   * Extracts metadata from a PDF file.
   */
  async extractMetadata(file: File): Promise<ApiResponse<MetadataResponse>> {
    const formData = new FormData();
    formData.append('file', file);

    const response = await this.client.post<ApiResponse<MetadataResponse>>('/metadata', formData);
    return response.data;
  }

  /**
   * Extracts text and data from a PDF file.
   */
  async extractData(request: DataExtractionRequest): Promise<ApiResponse<DataExtractionResponse>> {
    const formData = new FormData();
    formData.append('file', request.file);
    if (request.extractImages !== undefined) {
      formData.append('extractImages', request.extractImages.toString());
    }
    if (request.pages) {
      formData.append('pages', request.pages);
    }

    const response = await this.client.post<ApiResponse<DataExtractionResponse>>('/extract', formData);
    return response.data;
  }

  /**
   * Validates a PDF file for PDF/A conformance.
   */
  async validatePdfA(file: File, conformanceLevel?: string): Promise<ApiResponse<PdfAValidationResponse>> {
    const formData = new FormData();
    formData.append('file', file);
    if (conformanceLevel) {
      formData.append('conformanceLevel', conformanceLevel);
    }

    const response = await this.client.post<ApiResponse<PdfAValidationResponse>>('/validate-pdfa', formData);
    return response.data;
  }

  /**
   * Converts a PDF file to PDF/A format.
   */
  async convertToPdfA(request: PdfAConversionRequest): Promise<ApiResponse<FileResponse>> {
    const formData = new FormData();
    formData.append('file', request.file);
    formData.append('conformanceLevel', request.conformanceLevel);
    if (request.outputFileName) {
      formData.append('outputFileName', request.outputFileName);
    }
    if (request.copyMetadata !== undefined) {
      formData.append('copyMetadata', request.copyMetadata.toString());
    }
    if (request.embedFonts !== undefined) {
      formData.append('embedFonts', request.embedFonts.toString());
    }

    const response = await this.client.post<ApiResponse<FileResponse>>('/convert-pdfa', formData);
    return response.data;
  }
}

export const apiService = new ApiService();
