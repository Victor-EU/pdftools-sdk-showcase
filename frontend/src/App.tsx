import { useState } from 'react'
import { Box, Container, Paper, Tabs, Tab, Typography } from '@mui/material'
import { Description as PdfIcon } from '@mui/icons-material'
import { PDFViewer } from './components/PDFViewer/PDFViewer'
import { MergePanel } from './components/Operations/MergePanel/MergePanel'
import { SplitPanel } from './components/Operations/SplitPanel/SplitPanel'
import { CompressPanel } from './components/Operations/CompressPanel/CompressPanel'
import { ConvertPanel } from './components/Operations/ConvertPanel/ConvertPanel'

function App() {
  const [selectedTab, setSelectedTab] = useState(0)

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setSelectedTab(newValue)
  }

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: 'background.default' }}>
      {/* Header */}
      <Box
        component="header"
        sx={{
          bgcolor: 'primary.main',
          color: 'white',
          py: 3,
          boxShadow: 2,
        }}
      >
        <Container maxWidth="xl">
          <Box display="flex" alignItems="center" gap={2}>
            <PdfIcon sx={{ fontSize: 40 }} />
            <Box>
              <Typography variant="h4" fontWeight="bold">
                PDF Editor powered by Pdftools
              </Typography>
              <Typography variant="body2" sx={{ color: 'white' }}>
                View, Edit, Merge, Split, Compress & Convert PDFs
              </Typography>
            </Box>
          </Box>
        </Container>
      </Box>

      {/* Main Content */}
      <Container maxWidth="xl" sx={{ py: 4 }}>
        {/* Tabs */}
        <Paper elevation={2} sx={{ mb: 3 }}>
          <Tabs
            value={selectedTab}
            onChange={handleTabChange}
            variant="scrollable"
            scrollButtons="auto"
            sx={{ borderBottom: 1, borderColor: 'divider' }}
          >
            <Tab label="View & Annotate" />
            <Tab label="Merge PDFs" />
            <Tab label="Split PDF" />
            <Tab label="Compress PDF" />
            <Tab label="Convert to Image" />
          </Tabs>
        </Paper>

        {/* Tab Content */}
        <Paper elevation={2} sx={{ p: 4, minHeight: '60vh' }}>
          {selectedTab === 0 && (
            <Box>
              <Typography variant="h5" gutterBottom>
                View, Annotate & Redact PDF
              </Typography>
              <Typography color="text.secondary" sx={{ mb: 3 }}>
                Upload a PDF to view, add annotations, and redact sensitive information
              </Typography>
              <PDFViewer />
            </Box>
          )}

          {selectedTab === 1 && (
            <Box>
              <Typography variant="h5" gutterBottom>
                Merge Multiple PDFs
              </Typography>
              <Typography color="text.secondary" sx={{ mb: 3 }}>
                Combine multiple PDF files into a single document
              </Typography>
              <MergePanel />
            </Box>
          )}

          {selectedTab === 2 && (
            <Box>
              <Typography variant="h5" gutterBottom>
                Split PDF File
              </Typography>
              <Typography color="text.secondary" sx={{ mb: 3 }}>
                Divide a PDF into multiple files by pages or ranges
              </Typography>
              <SplitPanel />
            </Box>
          )}

          {selectedTab === 3 && (
            <Box>
              <Typography variant="h5" gutterBottom>
                Compress PDF File
              </Typography>
              <Typography color="text.secondary" sx={{ mb: 3 }}>
                Reduce PDF file size with optimized compression
              </Typography>
              <CompressPanel />
            </Box>
          )}

          {selectedTab === 4 && (
            <Box>
              <Typography variant="h5" gutterBottom>
                Convert PDF to Image
              </Typography>
              <Typography color="text.secondary" sx={{ mb: 3 }}>
                Convert PDF pages to PNG, JPEG, or TIFF images
              </Typography>
              <ConvertPanel />
            </Box>
          )}
        </Paper>

        {/* Footer */}
        <Box sx={{ mt: 4, textAlign: 'center', color: 'text.secondary' }}>
          <Typography variant="body2">
            Powered by PDFTools' SDK
          </Typography>
        </Box>
      </Container>
    </Box>
  )
}

export default App
