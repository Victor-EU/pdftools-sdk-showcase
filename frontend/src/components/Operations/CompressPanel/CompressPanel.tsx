import { useState } from 'react'
import {
  Box, Button, Typography, TextField, Alert, RadioGroup,
  FormControlLabel, Radio, Chip, Paper, Slider, LinearProgress
} from '@mui/material'
import {
  Upload as UploadIcon,
  Compress as CompressIcon
} from '@mui/icons-material'
import { apiService } from '../../../services/api'
import styles from './CompressPanel.module.css'

export const CompressPanel = () => {
  const [file, setFile] = useState<File | null>(null)
  const [profile, setProfile] = useState<'web' | 'print' | 'custom'>('web')
  const [quality, setQuality] = useState(75)
  const [outputName, setOutputName] = useState('compressed.pdf')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)
  const [compressionInfo, setCompressionInfo] = useState<{
    originalSize: number
    compressedSize: number
    ratio: number
  } | null>(null)
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
      } else {
        setError('Please drop a PDF file')
      }
    }
  }

  const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      setFile(event.target.files[0])
      setError(null)
    }
  }

  const handleCompress = async () => {
    if (!file) {
      setError('Please select a PDF file to compress')
      return
    }

    setLoading(true)
    setError(null)
    setSuccess(null)
    setCompressionInfo(null)

    try {
      const response = await apiService.compressPdf({
        file,
        compressionProfile: profile,
        imageQuality: profile === 'custom' ? quality : undefined,
        outputFileName: outputName
      })

      const originalSize = file.size
      const compressedSize = response.data.fileSize
      const ratio = response.data.compressionRatio || 0

      setCompressionInfo({
        originalSize,
        compressedSize,
        ratio
      })

      setSuccess(response.message)

      // Download compressed file
      const blob = await apiService.downloadFile(response.data.fileName)
      apiService.triggerDownload(blob, response.data.fileName)

      // Reset file but keep settings
      setFile(null)
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to compress PDF')
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
              Select a PDF file to compress
            </Typography>
            <input
              type="file"
              accept=".pdf"
              onChange={handleFileSelect}
              style={{ display: 'none' }}
              id="compress-upload"
            />
            <label htmlFor="compress-upload">
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
          {/* Compression Profile */}
          <Box sx={{ mt: 3 }}>
            <Typography variant="subtitle1" gutterBottom>
              Compression Profile
            </Typography>
            <RadioGroup
              value={profile}
              onChange={(e) => setProfile(e.target.value as any)}
            >
              <FormControlLabel
                value="web"
                control={<Radio />}
                label={
                  <Box>
                    <Typography variant="body1">Web (Recommended)</Typography>
                    <Typography variant="caption" color="text.secondary">
                      Optimized for web viewing - smaller file size
                    </Typography>
                  </Box>
                }
              />
              <FormControlLabel
                value="print"
                control={<Radio />}
                label={
                  <Box>
                    <Typography variant="body1">Print</Typography>
                    <Typography variant="caption" color="text.secondary">
                      Optimized for printing - better quality
                    </Typography>
                  </Box>
                }
              />
              <FormControlLabel
                value="custom"
                control={<Radio />}
                label={
                  <Box>
                    <Typography variant="body1">Custom</Typography>
                    <Typography variant="caption" color="text.secondary">
                      Adjust compression settings manually
                    </Typography>
                  </Box>
                }
              />
            </RadioGroup>
          </Box>

          {/* Quality Slider for Custom */}
          {profile === 'custom' && (
            <Box sx={{ mt: 3 }}>
              <Typography variant="subtitle1" gutterBottom>
                Image Quality: {quality}%
              </Typography>
              <Slider
                value={quality}
                onChange={(_, value) => setQuality(value as number)}
                min={1}
                max={100}
                marks={[
                  { value: 25, label: 'Low' },
                  { value: 50, label: 'Medium' },
                  { value: 75, label: 'High' },
                  { value: 100, label: 'Max' }
                ]}
                sx={{ mt: 2 }}
              />
              <Typography variant="caption" color="text.secondary">
                Higher quality = larger file size
              </Typography>
            </Box>
          )}

          {/* Output Name */}
          <TextField
            fullWidth
            label="Output Filename"
            value={outputName}
            onChange={(e) => setOutputName(e.target.value)}
            sx={{ mt: 3 }}
          />

          {/* Compress Button */}
          <Button
            variant="contained"
            size="large"
            startIcon={<CompressIcon />}
            onClick={handleCompress}
            disabled={loading}
            fullWidth
            sx={{ mt: 3 }}
          >
            {loading ? 'Compressing PDF...' : 'Compress PDF'}
          </Button>

          {/* Progress */}
          {loading && (
            <Box sx={{ mt: 2 }}>
              <LinearProgress />
              <Typography variant="caption" color="text.secondary" sx={{ mt: 1 }}>
                Compressing... This may take a moment
              </Typography>
            </Box>
          )}
        </>
      )}

      {/* Compression Info */}
      {compressionInfo && (
        <Paper sx={{ p: 2, mt: 2, bgcolor: 'success.light' }}>
          <Typography variant="h6" gutterBottom>
            Compression Results
          </Typography>
          <Box display="flex" justifyContent="space-between" mb={1}>
            <Typography>Original Size:</Typography>
            <Typography fontWeight="bold">
              {formatFileSize(compressionInfo.originalSize)}
            </Typography>
          </Box>
          <Box display="flex" justifyContent="space-between" mb={1}>
            <Typography>Compressed Size:</Typography>
            <Typography fontWeight="bold" color="success.dark">
              {formatFileSize(compressionInfo.compressedSize)}
            </Typography>
          </Box>
          <Box display="flex" justifyContent="space-between">
            <Typography>Size Reduction:</Typography>
            <Typography fontWeight="bold" color="success.dark">
              {compressionInfo.ratio.toFixed(2)}%
            </Typography>
          </Box>
        </Paper>
      )}

      {/* Messages */}
      {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
      {success && <Alert severity="success" sx={{ mt: 2 }}>{success}</Alert>}
    </Box>
  )
}
