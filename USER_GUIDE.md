# PDF Editor - User Guide

Welcome to PDF Editor, your complete solution for PDF processing! This guide will walk you through all the features and show you how to get the most out of the application.

## Table of Contents

1. [Getting Started](#getting-started)
2. [Interface Overview](#interface-overview)
3. [View & Annotate PDFs](#view--annotate-pdfs)
4. [Merge Multiple PDFs](#merge-multiple-pdfs)
5. [Split PDFs](#split-pdfs)
6. [Compress PDFs](#compress-pdfs)
7. [Convert PDFs to Images](#convert-pdfs-to-images)
8. [Tips & Best Practices](#tips--best-practices)
9. [Troubleshooting](#troubleshooting)
10. [FAQ](#frequently-asked-questions)

---

## Getting Started

### Accessing the Application

1. Open your web browser
2. Navigate to: `http://localhost:5000`
3. You'll see the PDF Editor home screen with five main tabs

### System Requirements

**Browser Requirements:**
- Modern web browser (Chrome, Firefox, Safari, Edge)
- JavaScript enabled
- Minimum 4GB RAM recommended
- Stable internet connection

**File Requirements:**
- Supported format: PDF files only
- Maximum file size: 100MB per file
- Maximum total size for merge: 200MB

---

## Interface Overview

The application consists of **five main sections**, accessible via tabs at the top:

1. **View & Annotate** - View PDFs and add annotations
2. **Merge PDFs** - Combine multiple PDF files
3. **Split PDF** - Divide a PDF into multiple files
4. **Compress PDF** - Reduce file size
5. **Convert to Image** - Export PDF pages as images

Each section has its own dedicated interface optimized for that specific operation.

---

## View & Annotate PDFs

The PDF Viewer allows you to view, annotate, and redact PDF documents directly in your browser.

### Opening a PDF

1. Click on the **"View & Annotate"** tab
2. Click the **"Select PDF File"** button
3. Choose a PDF from your computer
4. The PDF will load in the viewer

### Navigation Controls

Once your PDF is loaded:

- **Zoom In/Out**: Use the zoom controls or mouse wheel
- **Pan**: Click and drag to move around the document
- **Page Navigation**: Use arrow buttons or page selector
- **Fit to Width/Page**: Adjust view to fit your screen

### Adding Annotations

The viewer supports multiple annotation types:

#### Text Notes
1. Select the **Note** tool from the toolbar
2. Click anywhere on the PDF to place a note
3. Type your comment
4. Click outside to save

#### Highlighting
1. Select the **Highlight** tool
2. Click and drag over text to highlight
3. Choose a color from the palette
4. Release to apply

#### Drawing
1. Select the **Draw** tool
2. Click and drag to draw freehand
3. Adjust line width and color as needed

### Redaction (Removing Sensitive Information)

⚠️ **Important**: Redaction permanently removes information!

1. Select the **Redact** tool
2. Click and drag over the area to redact
3. Apply redaction (this is permanent)
4. Save the document

### Saving Your Work

After annotating:
1. Click **"Save"** or **"Export"** in the viewer toolbar
2. Choose save location
3. Your annotated PDF is ready!

---

## Merge Multiple PDFs

Combine several PDF files into one single document.

### Step-by-Step Guide

#### 1. Select Files
1. Click the **"Merge PDFs"** tab
2. Click **"Select PDF Files"**
3. Choose 2 or more PDF files (hold Ctrl/Cmd for multiple selection)
4. Selected files will appear in the list

#### 2. Arrange Files
The files will be merged in the order shown:
- **Reorder**: Drag and drop files to rearrange (if supported)
- **Remove**: Click the ❌ icon next to any file to remove it
- **Add More**: Click "Select PDF Files" again to add additional files

#### 3. Name Your Output
- Enter a custom filename in the **"Output Filename"** field
- Default: `merged.pdf`
- Don't worry about the `.pdf` extension - it's added automatically!

#### 4. Merge!
1. Click the **"Merge PDFs"** button
2. Wait for processing (progress shown)
3. The merged PDF will automatically download

### Example Uses

**Academic Papers**: Combine research articles, supplementary materials, and appendices
```
paper.pdf + appendix_a.pdf + references.pdf → complete_paper.pdf
```

**Invoices**: Merge monthly invoices into a single document
```
invoice_jan.pdf + invoice_feb.pdf + invoice_mar.pdf → q1_invoices.pdf
```

**Reports**: Combine multiple report sections
```
executive_summary.pdf + data_analysis.pdf + conclusions.pdf → full_report.pdf
```

---

## Split PDFs

Extract specific pages or ranges from a PDF document.

### Two Split Modes

#### Mode 1: Split by Ranges
Extract specific page ranges into separate files.

**Example**: Extract chapters from a book
```
Book (100 pages) →
  Chapter 1 (pages 1-20)
  Chapter 2 (pages 21-45)
  Chapter 3 (pages 46-100)
```

**Steps:**
1. Click **"Split PDF"** tab
2. Upload your PDF file
3. Select **"Ranges"** mode
4. Enter page ranges:
   - Format: `1-20` (start-end)
   - One range per entry
   - Click ➕ to add more ranges

**Example Input:**
```
Range 1: 1-20
Range 2: 21-45
Range 3: 46-100
```

#### Mode 2: Split by Pages
Split at specific page numbers, creating files between split points.

**Example**: Split a document at pages 10 and 25
```
Document (50 pages) →
  Part 1 (pages 1-9)
  Part 2 (pages 10-24)
  Part 3 (pages 25-50)
```

**Steps:**
1. Select **"Pages"** mode
2. Enter page numbers where splits should occur
3. Each number creates a break point

**Example Input:**
```
Page 1: 10
Page 2: 25
```
This creates 3 files: 1-9, 10-24, 25-50

### Naming Output Files

- **Base Name**: Enter a base name (e.g., "chapter")
- **Auto-naming**: Files are named: `basename_part1_pages1-20.pdf`
- Leave blank for automatic UUID naming

### Page Number Guidelines

📝 **Important Page Number Rules:**
- Page numbers start at **1** (not 0)
- Range format: `start-end` (both inclusive)
- Valid examples: `1-5`, `10-25`, `50-100`
- Invalid examples: `5-1` (backwards), `0-10` (page 0 doesn't exist)

### Common Split Scenarios

**Extract One Chapter:**
```
Mode: Ranges
Input: 15-30
Output: One file with pages 15-30
```

**Remove First Page:**
```
Mode: Ranges
Input: 2-[last page]
Output: Everything except page 1
```

**Split into Equal Parts (30-page doc):**
```
Mode: Pages
Input: 10, 20
Output: 3 files of 10 pages each
```

**Extract Odd Pages:**
```
Mode: Ranges
Input: 1-1, 3-3, 5-5, 7-7, ...
Output: Multiple single-page files
```

---

## Compress PDFs

Reduce PDF file size while maintaining acceptable quality.

### Compression Profiles

#### 1. Web (Recommended for most cases)
- **Best for**: Viewing on screens, email attachments
- **Quality**: Good for reading on devices
- **Compression**: Highest compression ratio
- **File Size**: 50-80% reduction typical

**Use when:**
- Sharing via email
- Uploading to web platforms
- Archiving documents that won't be printed
- Creating portfolios for online viewing

#### 2. Print
- **Best for**: Documents that will be printed
- **Quality**: High quality maintained
- **Compression**: Moderate compression
- **File Size**: 30-50% reduction typical

**Use when:**
- Preparing documents for printing
- Creating professional presentations
- Maintaining image quality is critical
- Moderate file size reduction needed

#### 3. Custom (Advanced)
- **Best for**: Specific quality requirements
- **Quality**: User-defined (1-100)
- **Compression**: Variable
- **File Size**: Depends on quality setting

**Quality Guide:**
- **90-100**: Near-original quality, minimal compression
- **70-89**: High quality, good compression
- **50-69**: Medium quality, significant compression
- **Below 50**: Lower quality, maximum compression

### How to Compress

1. Click **"Compress PDF"** tab
2. Click **"Select PDF File"**
3. Choose your compression profile:
   - **Quick choice**: Web or Print
   - **Advanced**: Custom + set quality (1-100)
4. (Optional) Set output filename
5. Click **"Compress PDF"**
6. See compression statistics:
   - Original size
   - Compressed size
   - Compression ratio %
7. Download automatically starts

### Compression Examples

**Example 1: Email Attachment**
```
Original: 15 MB (report.pdf)
Profile: Web
Result: 3.2 MB (compressed_report.pdf)
Compression: 78.7% reduction
Perfect for email (under 10MB limit)
```

**Example 2: Print-Ready Document**
```
Original: 25 MB (brochure.pdf)
Profile: Print
Result: 14 MB (brochure_print.pdf)
Compression: 44% reduction
High quality maintained for printing
```

**Example 3: Custom Quality**
```
Original: 50 MB (portfolio.pdf)
Profile: Custom (Quality: 75)
Result: 12 MB (portfolio_optimized.pdf)
Compression: 76% reduction
Balanced quality and size
```

### When NOT to Compress

❌ Avoid compression when:
- File is already small (< 1MB)
- Maximum quality is absolutely critical
- Contains vector graphics that might be rasterized
- Already compressed (may increase size!)

---

## Convert PDFs to Images

Export PDF pages as image files (PNG, JPEG, or TIFF).

### Image Formats

#### PNG (Recommended for most uses)
- **Type**: Lossless compression
- **Best for**: Screenshots, diagrams, text-heavy documents
- **Pros**: No quality loss, transparent backgrounds supported
- **Cons**: Larger file sizes
- **Use when**: Quality is priority, web publishing

#### JPEG/JPG
- **Type**: Lossy compression
- **Best for**: Photos, presentations, web use
- **Pros**: Smaller file sizes, widely compatible
- **Cons**: Quality loss with compression
- **Use when**: File size is priority, photographs

#### TIFF
- **Type**: Lossless, archive quality
- **Best for**: Professional archiving, high-quality prints
- **Pros**: Maximum quality, professional standard
- **Cons**: Very large file sizes
- **Use when**: Archival quality needed, professional printing

### DPI (Resolution) Guide

**DPI** (Dots Per Inch) determines image quality and file size:

| DPI | Quality | Use Case | File Size |
|-----|---------|----------|-----------|
| 72 | Low | Web thumbnails, previews | Small |
| 150 | Medium | General web use, screens | Medium |
| 300 | High | Printing, professional use | Large |
| 600 | Very High | Large format printing | Very Large |

**Recommended Settings:**
- **Web/Email**: 150 DPI PNG or JPEG
- **Printing**: 300 DPI PNG or TIFF
- **Archival**: 600 DPI TIFF
- **Quick preview**: 72 DPI JPEG

### Page Selection

You can convert **all pages** or **specific pages**:

#### All Pages (Default)
- Leave "Pages" field empty
- All pages converted to individual images

#### Specific Pages
Multiple formats supported:

**Single Page:**
```
Input: 5
Output: Page 5 only
```

**Multiple Pages (comma-separated):**
```
Input: 1,3,5,7
Output: Pages 1, 3, 5, and 7
```

**Page Range:**
```
Input: 1-5
Output: Pages 1 through 5
```

**Mixed (combine both):**
```
Input: 1-3,7,9-12
Output: Pages 1, 2, 3, 7, 9, 10, 11, 12
```

### How to Convert

1. Click **"Convert to Image"** tab
2. Upload your PDF
3. Choose **Image Format** (PNG/JPEG/TIFF)
4. Set **DPI** (resolution)
   - Default: 150 DPI
   - Range: 72-600 DPI
5. (Optional) Specify **Pages**
   - Leave empty for all pages
   - Enter specific pages/ranges
6. (Optional) Set **Base Filename**
   - Default: auto-generated
   - Output: `basename_page_01.png`, `basename_page_02.png`, etc.
7. Click **"Convert to Image"**
8. All images download as individual files

### Conversion Examples

**Example 1: Create Web Thumbnails**
```
PDF: presentation.pdf (20 pages)
Format: JPEG
DPI: 150
Pages: (empty - all pages)
Output: 20 JPEG files
  presentation_page_01.jpg
  presentation_page_02.jpg
  ...
  presentation_page_20.jpg
```

**Example 2: Extract Specific Diagrams**
```
PDF: technical_manual.pdf
Format: PNG
DPI: 300
Pages: 5,12,18,25
Output: 4 PNG files (pages 5, 12, 18, 25)
```

**Example 3: High-Quality Print**
```
PDF: poster.pdf (1 page)
Format: TIFF
DPI: 600
Pages: 1
Output: poster_page_01.tiff (print-ready)
```

**Example 4: Convert Chapter**
```
PDF: book.pdf (200 pages)
Format: PNG
DPI: 150
Pages: 50-75
Output: 26 PNG files (chapter 3)
  book_page_50.png through book_page_75.png
```

### File Naming

Output files are automatically named:
```
{basename}_page_{number}.{extension}
```

**Examples:**
- `document_page_01.png`
- `report_page_05.jpg`
- `diagram_page_12.tiff`

**Number Padding:**
- Pages 1-9: `page_01`, `page_02`, ... (2 digits)
- Pages 10-99: `page_10`, `page_11`, ... (2 digits)
- Pages 100+: `page_100`, `page_101`, ... (3 digits)

---

## Tips & Best Practices

### General Tips

#### File Management
- **Organize your PDFs**: Keep source files in dedicated folders
- **Backup originals**: Always keep a copy of the original file
- **Use descriptive names**: Makes finding files easier later
- **Check file sizes**: Be aware of size limits (100MB max)

#### Processing Large Files
- **Split first**: For very large PDFs, split into smaller parts before other operations
- **Compress before sending**: Reduce file size before emailing or uploading
- **Batch similar operations**: Process similar files together for efficiency

#### Quality vs Size Trade-offs
- **Web viewing**: Prioritize smaller sizes (Web compression, 150 DPI)
- **Printing**: Prioritize quality (Print compression, 300+ DPI)
- **Archival**: Use lossless formats (TIFF, high DPI)

### Operation-Specific Tips

#### Merging PDFs
✅ **Do:**
- Verify page order before merging
- Use consistent naming conventions
- Check individual files open correctly first
- Keep total size under 200MB

❌ **Don't:**
- Merge protected/encrypted PDFs (remove protection first)
- Mix different page orientations (can cause issues)
- Merge corrupted files

#### Splitting PDFs
✅ **Do:**
- Preview the PDF first to know page count
- Use logical split points (chapters, sections)
- Test with a small file first
- Double-check page ranges

❌ **Don't:**
- Use ranges that exceed total pages
- Overlap ranges in split mode
- Forget that pages start at 1, not 0

#### Compressing PDFs
✅ **Do:**
- Start with Web profile for most cases
- Compare before/after quality
- Test compressed files before deleting originals
- Use Custom profile for fine control

❌ **Don't:**
- Compress already-compressed files
- Use lowest quality for important documents
- Compress without checking output quality

#### Converting to Images
✅ **Do:**
- Choose appropriate DPI for use case
- Use PNG for text/diagrams
- Use JPEG for photos
- Convert only needed pages

❌ **Don't:**
- Use 600 DPI unless necessary (huge files)
- Convert entire document if only need some pages
- Use 72 DPI for anything that might be printed

### Workflow Examples

#### Scenario 1: Preparing a Report for Email
```
1. Merge all report sections → full_report.pdf
2. Compress with Web profile → full_report_compressed.pdf (3MB)
3. Email the compressed version
```

#### Scenario 2: Archiving Old Documents
```
1. Scan documents → scanned_docs.pdf
2. Split by date/category → docs_2023.pdf, docs_2024.pdf
3. Compress with Print profile → docs_2023_compressed.pdf
4. Store in organized folders
```

#### Scenario 3: Creating Presentation Materials
```
1. Have master PDF → presentation_master.pdf
2. Extract key pages → Split (pages 1,5,10,15,20)
3. Convert to images → PNG, 300 DPI
4. Use images in PowerPoint/Keynote
```

#### Scenario 4: Preparing for Print
```
1. Have draft document → draft.pdf
2. Split into sections → cover.pdf, content.pdf, back.pdf
3. Compress each with Print profile
4. Review quality
5. Merge final versions → final_print.pdf
6. Convert cover to high-res image for printer
```

---

## Troubleshooting

### Common Issues and Solutions

#### Issue: "File too large" error
**Problem**: File exceeds 100MB limit

**Solutions:**
1. Split the PDF into smaller parts first
2. Compress the PDF before other operations
3. Remove unnecessary pages
4. Use a PDF optimizer tool externally first

#### Issue: Upload fails or times out
**Problem**: Network timeout or server issue

**Solutions:**
1. Check your internet connection
2. Try a smaller file first
3. Refresh the page and try again
4. Clear browser cache
5. Try a different browser

#### Issue: Merged PDF has wrong page order
**Problem**: Files merged in unexpected order

**Solutions:**
1. Check the file list order before merging
2. Rename files with numbers (01_, 02_, etc.) for easier sorting
3. Add files one at a time in desired order
4. Use drag-and-drop to reorder (if available)

#### Issue: Split operation fails - "Page out of range"
**Problem**: Invalid page numbers entered

**Solutions:**
1. Check total page count first (use View tab)
2. Ensure page numbers start at 1
3. Verify end page ≤ total pages
4. Check range format: `start-end` (both inclusive)
5. Example: For 10-page PDF, valid range is 1-10

#### Issue: Compressed PDF is larger than original
**Problem**: File doesn't compress well

**Solutions:**
1. File may already be compressed
2. Contains vector graphics (don't compress well)
3. Try different compression profile
4. Skip compression for this file

#### Issue: Converted images are blurry
**Problem**: DPI setting too low

**Solutions:**
1. Increase DPI (try 300 instead of 150)
2. Use PNG instead of JPEG
3. Check original PDF quality
4. Use TIFF for maximum quality

#### Issue: PDF Viewer doesn't load
**Problem**: Viewer initialization failed

**Solutions:**
1. Refresh the browser page
2. Clear browser cache
3. Try a different browser (Chrome recommended)
4. Check browser console for errors
5. Ensure JavaScript is enabled

#### Issue: Downloaded file won't open
**Problem**: Corrupted download or wrong application

**Solutions:**
1. Download again
2. Use a PDF reader (Adobe Acrobat, Preview, etc.)
3. Check file isn't corrupted
4. Verify file extension is `.pdf`
5. Try opening in a different PDF reader

#### Issue: Operation stuck at "Processing..."
**Problem**: Long operation or server issue

**Solutions:**
1. Wait longer (large files take time)
2. Check browser console for errors
3. Refresh page and try with smaller file
4. Check server is running (for developers)

### Browser-Specific Issues

#### Chrome/Edge
- Works best, recommended browser
- Enable popups if downloads are blocked
- Check Downloads folder for completed files

#### Firefox
- May need to adjust download settings
- Allow popups from localhost
- Clear cache if issues persist

#### Safari
- Some features may require latest version
- Check Safari preferences for download location
- Enable JavaScript in preferences

### Getting Help

If problems persist:

1. **Check the logs**: Browser console (F12) for errors
2. **Verify setup**: Ensure backend is running (http://localhost:5001/api)
3. **Test with sample**: Try with a known-good small PDF
4. **Review security**: Check SECURITY_AUDIT.md
5. **Report issue**: Include:
   - Browser and version
   - File size and page count
   - Operation attempted
   - Error message (if any)
   - Console logs

---

## Frequently Asked Questions

### General Questions

**Q: What file formats are supported?**
A: PDF files only for input. Output can be PDF or images (PNG/JPEG/TIFF).

**Q: Is there a file size limit?**
A: Yes, 100MB per file, 200MB total for merge operations.

**Q: Are my files stored on the server?**
A: Files are temporarily stored during processing and automatically cleaned up after download. For production use, review the security documentation.

**Q: Can I process password-protected PDFs?**
A: No, remove password protection before uploading.

**Q: Does this work offline?**
A: No, requires connection to the backend server.

**Q: Can I batch process multiple files?**
A: Merge operation supports multiple files. Other operations process one file at a time.

### Merge Questions

**Q: How many files can I merge?**
A: Up to 50 files, total size under 200MB.

**Q: Will bookmarks be preserved?**
A: Basic merge preserves content but may not preserve all metadata.

**Q: Can I merge files with different page sizes?**
A: Yes, but be aware pages will maintain their original sizes.

**Q: Does order matter?**
A: Yes, files are merged in the order shown in the list.

### Split Questions

**Q: What's the difference between split modes?**
A:
- **Ranges**: You specify exact page ranges (e.g., 1-10, 11-20)
- **Pages**: You specify split points, and files are created between them

**Q: Can I extract a single page?**
A: Yes, use ranges mode with a single-page range (e.g., 5-5).

**Q: Can split points overlap?**
A: No, ensure ranges don't overlap in ranges mode.

**Q: How are output files named?**
A: Format: `basename_part{number}_pages{start}-{end}.pdf`

### Compress Questions

**Q: Which profile should I use?**
A:
- **Web**: For email, web viewing (highest compression)
- **Print**: For documents that will be printed
- **Custom**: When you need specific quality control

**Q: Can I compress an already compressed PDF?**
A: Yes, but you might not see much size reduction.

**Q: Will compression affect print quality?**
A: Use Print or Custom (high quality) profile to maintain print quality.

**Q: How much compression can I expect?**
A: Typically 50-80% reduction with Web profile, 30-50% with Print profile. Varies by document.

### Convert Questions

**Q: Can I convert just one page?**
A: Yes, specify the page number in the Pages field.

**Q: What DPI should I use?**
A:
- Web viewing: 150 DPI
- Printing: 300 DPI
- Large format printing: 600 DPI

**Q: Why are TIFF files so large?**
A: TIFF is lossless and uncompressed, providing maximum quality at the cost of file size.

**Q: Can I convert to other formats like Word or Excel?**
A: Not currently supported. This tool converts to image formats only.

**Q: What's the maximum number of pages I can convert?**
A: No specific limit, but consider file sizes and processing time for very large documents.

### Viewer Questions

**Q: Can I save annotations back to the PDF?**
A: Yes, use the Save/Export function in the viewer toolbar.

**Q: Are redactions reversible?**
A: No, redactions permanently remove information. Always keep a backup.

**Q: Can I print from the viewer?**
A: Yes, use the print function in the viewer toolbar.

**Q: Why isn't my PDF loading in the viewer?**
A: Check file isn't corrupted, isn't password-protected, and is a valid PDF.

### Technical Questions

**Q: What browsers are supported?**
A: Chrome, Firefox, Safari, Edge (latest versions). Chrome recommended.

**Q: Is this secure for sensitive documents?**
A: Review SECURITY_AUDIT.md before using with sensitive information.

**Q: Can I use this in production?**
A: Requires security hardening first. See SECURITY_AUDIT.md for critical issues.

**Q: Does this require an internet connection?**
A: Requires connection to localhost server (backend must be running).

**Q: Can I deploy this on my own server?**
A: Yes, see README.md deployment section.

---

## Keyboard Shortcuts

### PDF Viewer
- `Ctrl/Cmd + +`: Zoom in
- `Ctrl/Cmd + -`: Zoom out
- `Ctrl/Cmd + 0`: Reset zoom
- `←` / `→`: Previous/Next page
- `Home` / `End`: First/Last page
- `Ctrl/Cmd + P`: Print
- `Ctrl/Cmd + S`: Save

### Browser
- `F5`: Refresh page
- `F12`: Open developer console (for debugging)
- `Ctrl/Cmd + S`: Save page (when viewing results)

---

## Glossary

**DPI** (Dots Per Inch): Resolution measurement for images. Higher DPI = better quality but larger file size.

**Compression**: Reducing file size by removing redundant data or lowering quality.

**Lossless**: Compression that preserves all original data (PNG, TIFF).

**Lossy**: Compression that discards some data to achieve smaller sizes (JPEG).

**Page Range**: Consecutive pages specified as start-end (e.g., 1-10).

**Split Point**: Page number where a document is divided.

**Annotation**: Markup added to a PDF (notes, highlights, drawings).

**Redaction**: Permanent removal of content from a PDF.

**UUID**: Universally Unique Identifier, used for automatic file naming.

**Profile**: Predefined settings for compression or conversion operations.

---

## Additional Resources

- **Main Documentation**: See `README.md` for technical details
- **Security**: Review `SECURITY_AUDIT.md` before production use
- **Backend API**: See `backend/README.md` for API documentation
- **Frontend**: See `frontend/README.md` for developer information

---

## Feedback & Support

Found a bug? Have a suggestion? Need help?

- Check the [Troubleshooting](#troubleshooting) section
- Review the [FAQ](#frequently-asked-questions)
- Consult the `README.md` for technical details
- Report issues with:
  - Browser and version
  - Steps to reproduce
  - Error messages
  - File details (size, pages)

---

**Last Updated**: November 2024
**Version**: 1.0.0
**PDF Editor Team**

---

*Happy PDF processing! 📄✨*
