import { useState } from 'react'
import {
  Box, Button, Typography, Alert, Chip, Paper, LinearProgress,
  TextField, FormControlLabel, Checkbox, Grid
} from '@mui/material'
import {
  Upload as UploadIcon,
  Transform as ConvertIcon
} from '@mui/icons-material'
import { apiService } from '../../../services/api'
import styles from './PdfAConversionPanel.module.css'

const CONFORMANCE_LEVELS = [
  { value: '1b', label: 'PDF/A-1b', description: 'Basic visual preservation' },
  { value: '1a', label: 'PDF/A-1a', description: 'Full accessibility' },
  { value: '2b', label: 'PDF/A-2b', description: 'Visual preservation (JPEG2000)' },
  { value: '2a', label: 'PDF/A-2a', description: 'Full accessibility (JPEG2000)' },
  { value: '2u', label: 'PDF/A-2u', description: 'Unicode text extraction' },
  { value: '3b', label: 'PDF/A-3b', description: 'Embedded files support' },
  { value: '3a', label: 'PDF/A-3a', description: 'Full accessibility + files' },
  { value: '3u', label: 'PDF/A-3u', description: 'Unicode + embedded files' },
]

export const PdfAConversionPanel = () => {
  const [file, setFile] = useState<File | null>(null)
  const [conformanceLevel, setConformanceLevel] = useState('2b')
  const [outputName, setOutputName] = useState('')
  const [copyMetadata, setCopyMetadata] = useState(true)
  const [embedFonts, setEmbedFonts] = useState(true)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)
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
        setSuccess(null)
      } else {
        setError('Please drop a PDF file')
      }
    }
  }

  const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      setFile(event.target.files[0])
      setError(null)
      setSuccess(null)
    }
  }

  const handleConvert = async () => {
    if (!file) {
      setError('Please select a PDF file')
      return
    }

    setLoading(true)
    setError(null)
    setSuccess(null)

    try {
      const response = await apiService.convertToPdfA({
        file,
        conformanceLevel,
        outputFileName: outputName || undefined,
        copyMetadata,
        embedFonts
      })

      setSuccess(response.message)

      // Download converted file
      const blob = await apiService.downloadFile(response.data.fileName)
      apiService.triggerDownload(blob, response.data.fileName)

      setFile(null)
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to convert to PDF/A')
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
              Select a PDF file to convert to PDF/A format
            </Typography>
            <input
              type="file"
              accept=".pdf"
              onChange={handleFileSelect}
              style={{ display: 'none' }}
              id="pdfa-convert-upload"
            />
            <label htmlFor="pdfa-convert-upload">
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
              onDelete={() => setFile(null)}
              color="primary"
              sx={{ maxWidth: '100%' }}
            />
          </Box>
        )}
      </Paper>

      {file && (
        <>
          {/* Conformance Level Selection */}
          <Box sx={{ mt: 3 }}>
            <Typography variant="subtitle1" gutterBottom>
              Target PDF/A Conformance Level
            </Typography>
            <Grid container spacing={1} className={styles.conformanceLevels}>
              {CONFORMANCE_LEVELS.map((level) => (
                <Grid item xs={4} key={level.value}>
                  <Box
                    className={`${styles.conformanceOption} ${
                      conformanceLevel === level.value ? styles.conformanceSelected : ''
                    }`}
                    onClick={() => setConformanceLevel(level.value)}
                  >
                    <div className={styles.conformanceTitle}>{level.label}</div>
                    <div className={styles.conformanceDescription}>{level.description}</div>
                  </Box>
                </Grid>
              ))}
            </Grid>
          </Box>

          {/* Options */}
          <Box sx={{ mt: 3 }}>
            <TextField
              fullWidth
              label="Output Filename (optional)"
              placeholder="Leave empty for auto-generated name"
              value={outputName}
              onChange={(e) => setOutputName(e.target.value)}
              sx={{ mb: 2 }}
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={copyMetadata}
                  onChange={(e) => setCopyMetadata(e.target.checked)}
                />
              }
              label="Copy metadata from source document"
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={embedFonts}
                  onChange={(e) => setEmbedFonts(e.target.checked)}
                />
              }
              label="Embed all fonts"
            />
          </Box>

          {/* Convert Button */}
          <Button
            variant="contained"
            size="large"
            startIcon={<ConvertIcon />}
            onClick={handleConvert}
            disabled={loading}
            fullWidth
            sx={{ mt: 3 }}
          >
            {loading ? 'Converting to PDF/A...' : `Convert to PDF/A-${conformanceLevel.toUpperCase()}`}
          </Button>

          {loading && (
            <Box sx={{ mt: 2 }}>
              <LinearProgress />
              <Typography variant="caption" color="text.secondary" sx={{ mt: 1 }}>
                Converting document... This may take a moment
              </Typography>
            </Box>
          )}
        </>
      )}

      {/* Messages */}
      {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
      {success && <Alert severity="success" sx={{ mt: 2 }}>{success}</Alert>}
    </Box>
  )
}
