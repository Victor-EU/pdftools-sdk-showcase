/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_PDFTOOLS_VIEWER_LICENSE: string
  readonly VITE_API_BASE_URL?: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
