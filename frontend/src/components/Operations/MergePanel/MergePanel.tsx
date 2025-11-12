import { useState } from 'react'
import { Box, Button, Typography, List, ListItem, ListItemText, IconButton, TextField, Alert } from '@mui/material'
import { Upload as UploadIcon, Delete as DeleteIcon, Merge as MergeIcon } from '@mui/icons-material'
import { apiService } from '../../../services/api'
import styles from './MergePanel.module.css'

export const MergePanel = () => {
  const [files, setFiles] = useState<File[]>([])
  const [outputName, setOutputName] = useState('merged.pdf')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)

  const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files) {
      const newFiles = Array.from(event.target.files)
      setFiles(prev => [...prev, ...newFiles])
      setError(null)
    }
  }

  const handleRemoveFile = (index: number) => {
    setFiles(prev => prev.filter((_, i) => i !== index))
  }

  const handleMerge = async () => {
    if (files.length < 2) {
      setError('Please select at least 2 PDF files to merge')
      return
    }

    setLoading(true)
    setError(null)
    setSuccess(null)

    try {
      const response = await apiService.mergePdfs({ files, outputFileName: outputName })
      setSuccess(response.message)

      const blob = await apiService.downloadFile(response.data.fileName)
      apiService.triggerDownload(blob, response.data.fileName)

      setFiles([])
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to merge PDFs')
    } finally {
      setLoading(false)
    }
  }

  return (
    <Box className={styles.container}>
      <Box className={styles.uploadSection}>
        <input
          type="file"
          accept=".pdf"
          multiple
          onChange={handleFileSelect}
          style={{ display: 'none' }}
          id="merge-upload"
        />
        <label htmlFor="merge-upload">
          <Button variant="outlined" component="span" startIcon={<UploadIcon />} fullWidth>
            Select PDF Files
          </Button>
        </label>
      </Box>

      {files.length > 0 && (
        <>
          <Typography variant="subtitle1" sx={{ mt: 3, mb: 2 }}>
            Selected Files ({files.length})
          </Typography>
          <List className={styles.fileList}>
            {files.map((file, index) => (
              <ListItem
                key={index}
                secondaryAction={
                  <IconButton edge="end" onClick={() => handleRemoveFile(index)}>
                    <DeleteIcon />
                  </IconButton>
                }
              >
                <ListItemText
                  primary={file.name}
                  secondary={`${(file.size / 1024 / 1024).toFixed(2)} MB`}
                />
              </ListItem>
            ))}
          </List>

          <TextField
            fullWidth
            label="Output Filename"
            value={outputName}
            onChange={(e) => setOutputName(e.target.value)}
            sx={{ mt: 2 }}
          />

          <Button
            variant="contained"
            startIcon={<MergeIcon />}
            onClick={handleMerge}
            disabled={files.length < 2 || loading}
            fullWidth
            sx={{ mt: 3 }}
          >
            {loading ? 'Merging...' : 'Merge PDFs'}
          </Button>
        </>
      )}

      {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
      {success && <Alert severity="success" sx={{ mt: 2 }}>{success}</Alert>}
    </Box>
  )
}
