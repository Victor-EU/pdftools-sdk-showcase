import { useState } from 'react'
import { Box, Container, Paper, Tabs, Tab, Typography } from '@mui/material'
import { PDFViewer } from './components/PDFViewer/PDFViewer'
import { MergePanel } from './components/Operations/MergePanel/MergePanel'
import { SplitPanel } from './components/Operations/SplitPanel/SplitPanel'
import { CompressPanel } from './components/Operations/CompressPanel/CompressPanel'
import { ConvertPanel } from './components/Operations/ConvertPanel/ConvertPanel'
import { MetadataPanel } from './components/Operations/MetadataPanel/MetadataPanel'
import { DataExtractionPanel } from './components/Operations/DataExtractionPanel/DataExtractionPanel'
import { PdfAValidationPanel } from './components/Operations/PdfAValidationPanel/PdfAValidationPanel'
import { PdfAConversionPanel } from './components/Operations/PdfAConversionPanel/PdfAConversionPanel'

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
          bgcolor: 'white',
          py: 2.5,
          boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
          borderBottom: '1px solid #e5e7eb',
        }}
      >
        <Container maxWidth="xl">
          <Box display="flex" alignItems="center" justifyContent="space-between">
            <Box display="flex" alignItems="center" gap={3}>
              <a href="/" style={{ display: 'flex', alignItems: 'center' }}>
                <img
                  src="/pdftools-logo.svg"
                  alt="PDF Tools"
                  style={{ height: 44, cursor: 'pointer' }}
                />
              </a>
              <Box sx={{ borderLeft: '2px solid #e5e7eb', pl: 3, ml: 1 }}>
                <Typography
                  variant="h5"
                  fontWeight="700"
                  color="text.primary"
                  sx={{ letterSpacing: '-0.02em' }}
                >
                  Pdftools SDK Showcase
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mt: 0.25 }}>
                  View, Edit, Merge, Split, Compress, Convert, Extract & Validate PDFs
                </Typography>
              </Box>
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
            <Tab label="Metadata" />
            <Tab label="Extract Data" />
            <Tab label="PDF/A Validation" />
            <Tab label="PDF/A Conversion" />
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

          {selectedTab === 5 && (
            <Box>
              <Typography variant="h5" gutterBottom>
                Extract PDF Metadata
              </Typography>
              <Typography color="text.secondary" sx={{ mb: 3 }}>
                View document properties, author info, and PDF/A conformance details
              </Typography>
              <MetadataPanel />
            </Box>
          )}

          {selectedTab === 6 && (
            <Box>
              <Typography variant="h5" gutterBottom>
                Extract PDF Data
              </Typography>
              <Typography color="text.secondary" sx={{ mb: 3 }}>
                Extract text content from PDF documents
              </Typography>
              <DataExtractionPanel />
            </Box>
          )}

          {selectedTab === 7 && (
            <Box>
              <Typography variant="h5" gutterBottom>
                PDF/A Validation
              </Typography>
              <Typography color="text.secondary" sx={{ mb: 3 }}>
                Validate PDF documents for PDF/A compliance
              </Typography>
              <PdfAValidationPanel />
            </Box>
          )}

          {selectedTab === 8 && (
            <Box>
              <Typography variant="h5" gutterBottom>
                Convert to PDF/A
              </Typography>
              <Typography color="text.secondary" sx={{ mb: 3 }}>
                Convert PDF documents to archival PDF/A format
              </Typography>
              <PdfAConversionPanel />
            </Box>
          )}
        </Paper>

        {/* Footer */}
        <Box sx={{ mt: 4, textAlign: 'center', color: 'text.secondary' }}>
          <Typography variant="body2">
            Powered by <a href="https://www.pdf-tools.com" target="_blank" rel="noopener noreferrer" style={{ color: '#3B82F6', textDecoration: 'none' }}>PDF Tools</a> SDK
          </Typography>
        </Box>
      </Container>
    </Box>
  )
}

export default App
