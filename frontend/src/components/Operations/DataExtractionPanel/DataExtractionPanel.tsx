import { useState } from 'react'
import {
  Box, Button, Typography, Alert, Chip, Paper, LinearProgress,
  TextField, FormControlLabel, Checkbox, Accordion, AccordionSummary,
  AccordionDetails, Tabs, Tab
} from '@mui/material'
import {
  Upload as UploadIcon,
  TextFields as TextIcon,
  ExpandMore as ExpandMoreIcon,
  ContentCopy as CopyIcon
} from '@mui/icons-material'
import { apiService } from '../../../services/api'
import { DataExtractionResponse } from '../../../types'
import styles from './DataExtractionPanel.module.css'

export const DataExtractionPanel = () => {
  const [file, setFile] = useState<File | null>(null)
  const [pages, setPages] = useState('')
  const [extractImages, setExtractImages] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [result, setResult] = useState<DataExtractionResponse | null>(null)
  const [dragActive, setDragActive] = useState(false)
  const [viewTab, setViewTab] = useState(0)
  const [copied, setCopied] = useState(false)

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

  const handleExtract = async () => {
    if (!file) {
      setError('Please select a PDF file')
      return
    }

    setLoading(true)
    setError(null)

    try {
      const response = await apiService.extractData({
        file,
        extractImages,
        pages: pages || undefined
      })
      setResult(response.data)
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to extract data')
    } finally {
      setLoading(false)
    }
  }

  const handleCopyText = () => {
    if (result?.textContent) {
      navigator.clipboard.writeText(result.textContent)
      setCopied(true)
      setTimeout(() => setCopied(false), 2000)
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
              Select a PDF file to extract text and data
            </Typography>
            <input
              type="file"
              accept=".pdf"
              onChange={handleFileSelect}
              style={{ display: 'none' }}
              id="extract-upload"
            />
            <label htmlFor="extract-upload">
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
        <>
          {/* Options */}
          <Box sx={{ mt: 3 }}>
            <TextField
              fullWidth
              label="Page Range (optional)"
              placeholder="e.g., 1-5, 8, 10-12"
              value={pages}
              onChange={(e) => setPages(e.target.value)}
              helperText="Leave empty to extract from all pages"
              sx={{ mb: 2 }}
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={extractImages}
                  onChange={(e) => setExtractImages(e.target.checked)}
                />
              }
              label="Extract images (experimental)"
            />
          </Box>

          <Button
            variant="contained"
            size="large"
            startIcon={<TextIcon />}
            onClick={handleExtract}
            disabled={loading}
            fullWidth
            sx={{ mt: 2 }}
          >
            {loading ? 'Extracting Data...' : 'Extract Data'}
          </Button>
        </>
      )}

      {loading && (
        <Box sx={{ mt: 2 }}>
          <LinearProgress />
          <Typography variant="caption" color="text.secondary" sx={{ mt: 1 }}>
            Extracting text and data...
          </Typography>
        </Box>
      )}

      {/* Results Display */}
      {result && (
        <Paper sx={{ p: 3, mt: 3 }}>
          {/* Stats */}
          <Box className={styles.statsGrid}>
            <Box className={styles.statItem}>
              <div className={styles.statValue}>{result.pages.length}</div>
              <div className={styles.statLabel}>Pages</div>
            </Box>
            <Box className={styles.statItem}>
              <div className={styles.statValue}>{result.wordCount.toLocaleString()}</div>
              <div className={styles.statLabel}>Words</div>
            </Box>
            <Box className={styles.statItem}>
              <div className={styles.statValue}>{result.characterCount.toLocaleString()}</div>
              <div className={styles.statLabel}>Characters</div>
            </Box>
            <Box className={styles.statItem}>
              <div className={styles.statValue}>{result.imageCount}</div>
              <div className={styles.statLabel}>Images</div>
            </Box>
          </Box>

          {/* View Tabs */}
          <Tabs value={viewTab} onChange={(_, v) => setViewTab(v)} sx={{ mb: 2 }}>
            <Tab label="Full Text" />
            <Tab label="By Page" />
          </Tabs>

          {viewTab === 0 && (
            <Box>
              <Box sx={{ display: 'flex', justifyContent: 'flex-end', mb: 1 }}>
                <Button
                  size="small"
                  startIcon={<CopyIcon />}
                  onClick={handleCopyText}
                >
                  {copied ? 'Copied!' : 'Copy All'}
                </Button>
              </Box>
              <Box className={styles.textContent}>
                {result.textContent || 'No text content extracted'}
              </Box>
            </Box>
          )}

          {viewTab === 1 && (
            <Box>
              {result.pages.map((page) => (
                <Accordion key={page.pageNumber} defaultExpanded={page.pageNumber === 1}>
                  <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                    <Typography>
                      Page {page.pageNumber}
                      <Chip
                        label={`${page.wordCount} words`}
                        size="small"
                        sx={{ ml: 2 }}
                      />
                    </Typography>
                  </AccordionSummary>
                  <AccordionDetails>
                    <Box className={styles.textContent} sx={{ maxHeight: 200 }}>
                      {page.text || 'No text on this page'}
                    </Box>
                  </AccordionDetails>
                </Accordion>
              ))}
            </Box>
          )}

          <Button
            variant="outlined"
            onClick={() => { setResult(null); setFile(null); }}
            sx={{ mt: 3 }}
          >
            Extract Another File
          </Button>
        </Paper>
      )}

      {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
    </Box>
  )
}
