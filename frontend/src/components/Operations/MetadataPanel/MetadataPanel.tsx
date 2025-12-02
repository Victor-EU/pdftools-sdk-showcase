import { useState } from 'react'
import {
  Box, Button, Typography, Alert, Chip, Paper, LinearProgress, Grid
} from '@mui/material'
import {
  Upload as UploadIcon,
  Info as InfoIcon,
  Lock as LockIcon,
  Description as DescriptionIcon
} from '@mui/icons-material'
import { apiService } from '../../../services/api'
import { MetadataResponse } from '../../../types'
import styles from './MetadataPanel.module.css'

export const MetadataPanel = () => {
  const [file, setFile] = useState<File | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [metadata, setMetadata] = useState<MetadataResponse | null>(null)
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
        setMetadata(null)
      } else {
        setError('Please drop a PDF file')
      }
    }
  }

  const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      setFile(event.target.files[0])
      setError(null)
      setMetadata(null)
    }
  }

  const handleExtractMetadata = async () => {
    if (!file) {
      setError('Please select a PDF file')
      return
    }

    setLoading(true)
    setError(null)

    try {
      const response = await apiService.extractMetadata(file)
      setMetadata(response.data)
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to extract metadata')
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

  const formatDate = (dateStr: string | null) => {
    if (!dateStr) return 'N/A'
    try {
      return new Date(dateStr).toLocaleString()
    } catch {
      return dateStr
    }
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
              Select a PDF file to extract metadata
            </Typography>
            <input
              type="file"
              accept=".pdf"
              onChange={handleFileSelect}
              style={{ display: 'none' }}
              id="metadata-upload"
            />
            <label htmlFor="metadata-upload">
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
              onDelete={() => { setFile(null); setMetadata(null); }}
              color="primary"
              sx={{ maxWidth: '100%' }}
            />
          </Box>
        )}
      </Paper>

      {file && !metadata && (
        <Button
          variant="contained"
          size="large"
          startIcon={<InfoIcon />}
          onClick={handleExtractMetadata}
          disabled={loading}
          fullWidth
          sx={{ mt: 3 }}
        >
          {loading ? 'Extracting Metadata...' : 'Extract Metadata'}
        </Button>
      )}

      {loading && (
        <Box sx={{ mt: 2 }}>
          <LinearProgress />
          <Typography variant="caption" color="text.secondary" sx={{ mt: 1 }}>
            Analyzing document...
          </Typography>
        </Box>
      )}

      {/* Metadata Display */}
      {metadata && (
        <Paper sx={{ p: 3, mt: 3 }}>
          <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <DescriptionIcon color="primary" />
            Document Metadata
          </Typography>

          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={12} sm={6}>
              <Box className={styles.metadataItem}>
                <span className={styles.metadataLabel}>Title</span>
                <span className={styles.metadataValue}>{metadata.title || 'N/A'}</span>
              </Box>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Box className={styles.metadataItem}>
                <span className={styles.metadataLabel}>Author</span>
                <span className={styles.metadataValue}>{metadata.author || 'N/A'}</span>
              </Box>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Box className={styles.metadataItem}>
                <span className={styles.metadataLabel}>Subject</span>
                <span className={styles.metadataValue}>{metadata.subject || 'N/A'}</span>
              </Box>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Box className={styles.metadataItem}>
                <span className={styles.metadataLabel}>Keywords</span>
                <span className={styles.metadataValue}>{metadata.keywords || 'N/A'}</span>
              </Box>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Box className={styles.metadataItem}>
                <span className={styles.metadataLabel}>Creator</span>
                <span className={styles.metadataValue}>{metadata.creator || 'N/A'}</span>
              </Box>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Box className={styles.metadataItem}>
                <span className={styles.metadataLabel}>Producer</span>
                <span className={styles.metadataValue}>{metadata.producer || 'N/A'}</span>
              </Box>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Box className={styles.metadataItem}>
                <span className={styles.metadataLabel}>Creation Date</span>
                <span className={styles.metadataValue}>{formatDate(metadata.creationDate)}</span>
              </Box>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Box className={styles.metadataItem}>
                <span className={styles.metadataLabel}>Modification Date</span>
                <span className={styles.metadataValue}>{formatDate(metadata.modificationDate)}</span>
              </Box>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Box className={styles.metadataItem}>
                <span className={styles.metadataLabel}>PDF Version</span>
                <span className={styles.metadataValue}>{metadata.pdfVersion || 'N/A'}</span>
              </Box>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Box className={styles.metadataItem}>
                <span className={styles.metadataLabel}>Page Count</span>
                <span className={styles.metadataValue}>{metadata.pageCount}</span>
              </Box>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Box className={styles.metadataItem}>
                <span className={styles.metadataLabel}>File Size</span>
                <span className={styles.metadataValue}>{formatFileSize(metadata.fileSize)}</span>
              </Box>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Box className={styles.metadataItem}>
                <span className={styles.metadataLabel}>PDF/A Conformance</span>
                <span className={styles.metadataValue}>{metadata.pdfaConformance || 'Not PDF/A'}</span>
              </Box>
            </Grid>
          </Grid>

          {/* Document Properties */}
          <Box sx={{ mt: 3, display: 'flex', gap: 1, flexWrap: 'wrap' }}>
            <Chip
              icon={<LockIcon />}
              label={metadata.isEncrypted ? 'Encrypted' : 'Not Encrypted'}
              color={metadata.isEncrypted ? 'warning' : 'default'}
              variant="outlined"
            />
            <Chip
              label={metadata.isTagged ? 'Tagged (Accessible)' : 'Not Tagged'}
              color={metadata.isTagged ? 'success' : 'default'}
              variant="outlined"
            />
            <Chip
              label={metadata.hasForms ? 'Has Forms' : 'No Forms'}
              variant="outlined"
            />
            <Chip
              label={metadata.isLinearized ? 'Fast Web View' : 'Standard'}
              variant="outlined"
            />
          </Box>

          <Button
            variant="outlined"
            onClick={() => { setMetadata(null); setFile(null); }}
            sx={{ mt: 3 }}
          >
            Analyze Another File
          </Button>
        </Paper>
      )}

      {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
    </Box>
  )
}
