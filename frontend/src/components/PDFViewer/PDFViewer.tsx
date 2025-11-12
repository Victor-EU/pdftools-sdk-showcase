import { useEffect, useRef, useState } from 'react'
import { Box, Typography, Paper, Button, CircularProgress, Alert } from '@mui/material'
import { Upload as UploadIcon, Description as PdfIcon } from '@mui/icons-material'
import { PdfWebViewer } from '@pdf-tools/four-heights-pdf-web-viewer'
import '@pdf-tools/four-heights-pdf-web-viewer/css/pdf-web-viewer.css'
import styles from './PDFViewer.module.css'

const LICENSE_KEY = '<VIEWWEB,V5,H8A8N0HNQB4KUAHDBJ>'

/**
 * PDF Viewer Component with Annotation Support
 * Uses PDF Tools Four Heights Web Viewer SDK
 */
export const PDFViewer = () => {
  const viewerContainerRef = useRef<HTMLDivElement>(null)
  const pdfViewerRef = useRef<PdfWebViewer | null>(null)
  const [isViewerReady, setIsViewerReady] = useState(false)
  const [currentFile, setCurrentFile] = useState<string | null>(null)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  // Initialize PDF viewer
  useEffect(() => {
    if (!viewerContainerRef.current || pdfViewerRef.current) return

    try {
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
    } catch (err) {
      console.error('Failed to initialize PDF viewer:', err)
      setError('Failed to initialize PDF viewer. Please check console for details.')
    }

    // Cleanup on unmount
    return () => {
      if (pdfViewerRef.current) {
        try {
          pdfViewerRef.current.destroy()
        } catch (err) {
          console.error('Error destroying viewer:', err)
        }
        pdfViewerRef.current = null
      }
    }
  }, [])

  const handleFileUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0]
    if (!file) return

    if (file.type !== 'application/pdf') {
      setError('Please select a valid PDF file')
      return
    }

    if (!pdfViewerRef.current) {
      setError('PDF viewer not initialized')
      return
    }

    setIsLoading(true)
    setError(null)

    try {
      // Load PDF into viewer using the open() method
      // InputFile requires an object with a 'data' property containing the File
      pdfViewerRef.current.open({ data: file })

      setCurrentFile(file.name)
      setIsLoading(false)
    } catch (err) {
      console.error('Error loading PDF:', err)
      setError(`Failed to load PDF: ${err instanceof Error ? err.message : 'Unknown error'}`)
      setIsLoading(false)
    }
  }

  return (
    <Box className={styles.container}>
      {!currentFile && (
        <Paper className={styles.uploadArea}>
          <UploadIcon sx={{ fontSize: 64, color: 'primary.main', mb: 2 }} />
          <Typography variant="h6" gutterBottom>
            Upload PDF to View & Annotate
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
            Select a PDF file to view and annotate
          </Typography>

          {error && (
            <Alert severity="error" sx={{ mb: 2, maxWidth: 500 }}>
              {error}
            </Alert>
          )}

          <input
            type="file"
            accept=".pdf,application/pdf"
            onChange={handleFileUpload}
            style={{ display: 'none' }}
            id="pdf-upload"
            disabled={!isViewerReady || isLoading}
          />
          <label htmlFor="pdf-upload">
            <Button
              variant="contained"
              component="span"
              disabled={!isViewerReady || isLoading}
              startIcon={isLoading ? <CircularProgress size={20} /> : <PdfIcon />}
            >
              {isLoading ? 'Loading...' : 'Select PDF File'}
            </Button>
          </label>

          {!isViewerReady && (
            <Typography variant="caption" color="text.secondary" sx={{ mt: 2, display: 'block' }}>
              Initializing PDF viewer...
            </Typography>
          )}
        </Paper>
      )}

      {/* PDF Viewer Container */}
      <div
        ref={viewerContainerRef}
        style={{
          width: '100%',
          height: currentFile ? '100%' : '0',
          visibility: currentFile ? 'visible' : 'hidden',
          position: 'absolute',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
        }}
      />
    </Box>
  )
}
