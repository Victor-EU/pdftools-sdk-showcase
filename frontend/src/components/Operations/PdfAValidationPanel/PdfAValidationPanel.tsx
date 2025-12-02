import { useState } from 'react'
import {
  Box, Button, Typography, Alert, Chip, Paper, LinearProgress,
  Accordion, AccordionSummary, AccordionDetails, Divider
} from '@mui/material'
import {
  Upload as UploadIcon,
  VerifiedUser as ValidateIcon,
  CheckCircle as CheckIcon,
  Cancel as CancelIcon,
  ExpandMore as ExpandMoreIcon,
  Error as ErrorIcon,
  Warning as WarningIcon
} from '@mui/icons-material'
import { apiService } from '../../../services/api'
import { PdfAValidationResponse } from '../../../types'
import styles from './PdfAValidationPanel.module.css'

export const PdfAValidationPanel = () => {
  const [file, setFile] = useState<File | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [result, setResult] = useState<PdfAValidationResponse | null>(null)
  const [dragActive, setDragActive] = useState(false)

  const handleDrag = (e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true)
    } else if (e.type === 'dragleave') {
      setDragActive(false)
    }
  }

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    setDragActive(false)

    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      const droppedFile = e.dataTransfer.files[0]
      if (droppedFile.type === 'application/pdf') {
        setFile(droppedFile)
        setError(null)
        setResult(null)
      } else {
        setError('Please drop a PDF file')
      }
    }
  }

  const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      setFile(event.target.files[0])
      setError(null)
      setResult(null)
    }
  }

  const handleValidate = async () => {
    if (!file) {
      setError('Please select a PDF file')
      return
    }

    setLoading(true)
    setError(null)

    try {
      const response = await apiService.validatePdfA(file)
      setResult(response.data)
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to validate PDF/A')
    } finally {
      setLoading(false)
    }
  }

  const formatFileSize = (bytes: number) => {
    if (bytes === 0) return '0 Bytes'
    const k = 1024
    const sizes = ['Bytes', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + ' ' + sizes[i]
  }

  return (
    <Box className={styles.container}>
      {/* File Upload Area */}
      <Paper
        className={`${styles.uploadArea} ${dragActive ? styles.dragActive : ''}`}
        onDragEnter={handleDrag}
        onDragLeave={handleDrag}
        onDragOver={handleDrag}
        onDrop={handleDrop}
      >
        {!file ? (
          <>
            <UploadIcon sx={{ fontSize: 48, color: 'primary.main', mb: 2 }} />
            <Typography variant="h6" gutterBottom>
              Drop PDF here or click to select
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
              Select a PDF file to validate PDF/A conformance
            </Typography>
            <input
              type="file"
              accept=".pdf"
              onChange={handleFileSelect}
              style={{ display: 'none' }}
              id="validate-upload"
            />
            <label htmlFor="validate-upload">
              <Button variant="outlined" component="span">
                Select PDF File
              </Button>
            </label>
          </>
        ) : (
          <Box textAlign="center">
            <Typography variant="h6" gutterBottom>
              Selected File
            </Typography>
            <Chip
              label={`${file.name} (${formatFileSize(file.size)})`}
              onDelete={() => { setFile(null); setResult(null); }}
              color="primary"
              sx={{ maxWidth: '100%' }}
            />
          </Box>
        )}
      </Paper>

      {file && !result && (
        <Button
          variant="contained"
          size="large"
          startIcon={<ValidateIcon />}
          onClick={handleValidate}
          disabled={loading}
          fullWidth
          sx={{ mt: 3 }}
        >
          {loading ? 'Validating...' : 'Validate PDF/A Conformance'}
        </Button>
      )}

      {loading && (
        <Box sx={{ mt: 2 }}>
          <LinearProgress />
          <Typography variant="caption" color="text.secondary" sx={{ mt: 1 }}>
            Analyzing PDF/A conformance...
          </Typography>
        </Box>
      )}

      {/* Results Display */}
      {result && (
        <Paper sx={{ p: 3, mt: 3 }}>
          {/* Header */}
          <Box className={styles.resultHeader}>
            <Box className={`${styles.complianceIcon} ${result.isCompliant ? styles.compliant : styles.nonCompliant}`}>
              {result.isCompliant ? (
                <CheckIcon sx={{ fontSize: 40 }} />
              ) : (
                <CancelIcon sx={{ fontSize: 40 }} />
              )}
            </Box>
            <Box>
              <Typography variant="h5">
                {result.isCompliant ? 'PDF/A Compliant' : 'Not PDF/A Compliant'}
              </Typography>
              {result.conformanceLevel && (
                <Typography variant="body1" color="text.secondary">
                  Conformance Level: {result.conformanceLevel}
                </Typography>
              )}
            </Box>
          </Box>

          <Divider sx={{ my: 2 }} />

          {/* Summary */}
          <Typography variant="body1" sx={{ mb: 2 }}>
            {result.summary}
          </Typography>

          {/* Stats */}
          <Box sx={{ display: 'flex', gap: 2, mb: 3 }}>
            <Chip
              icon={<ErrorIcon />}
              label={`${result.errorCount} Errors`}
              color={result.errorCount > 0 ? 'error' : 'default'}
              variant="outlined"
            />
            <Chip
              icon={<WarningIcon />}
              label={`${result.warningCount} Warnings`}
              color={result.warningCount > 0 ? 'warning' : 'default'}
              variant="outlined"
            />
          </Box>

          {/* Errors */}
          {result.errors.length > 0 && (
            <Accordion defaultExpanded>
              <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                <Typography color="error" sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  <ErrorIcon /> Errors ({result.errors.length})
                </Typography>
              </AccordionSummary>
              <AccordionDetails>
                {result.errors.map((issue, index) => (
                  <Box key={index} className={`${styles.issueItem} ${styles.issueError}`}>
                    <div className={styles.issueCode}>
                      {issue.code}
                      {issue.pageNumber && ` (Page ${issue.pageNumber})`}
                    </div>
                    <div className={styles.issueMessage}>{issue.message}</div>
                    {issue.context && (
                      <Typography variant="caption" color="text.secondary">
                        Context: {issue.context}
                      </Typography>
                    )}
                  </Box>
                ))}
              </AccordionDetails>
            </Accordion>
          )}

          {/* Warnings */}
          {result.warnings.length > 0 && (
            <Accordion>
              <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                <Typography color="warning.main" sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  <WarningIcon /> Warnings ({result.warnings.length})
                </Typography>
              </AccordionSummary>
              <AccordionDetails>
                {result.warnings.map((issue, index) => (
                  <Box key={index} className={`${styles.issueItem} ${styles.issueWarning}`}>
                    <div className={styles.issueCode}>
                      {issue.code}
                      {issue.pageNumber && ` (Page ${issue.pageNumber})`}
                    </div>
                    <div className={styles.issueMessage}>{issue.message}</div>
                    {issue.context && (
                      <Typography variant="caption" color="text.secondary">
                        Context: {issue.context}
                      </Typography>
                    )}
                  </Box>
                ))}
              </AccordionDetails>
            </Accordion>
          )}

          <Button
            variant="outlined"
            onClick={() => { setResult(null); setFile(null); }}
            sx={{ mt: 3 }}
          >
            Validate Another File
          </Button>
        </Paper>
      )}

      {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
    </Box>
  )
}
