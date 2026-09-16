import type { Metadata, Viewport } from 'next'
import './styles.css'

export const metadata: Metadata = { title: 'Dukaan Digital Kit | دکان ڈیجیٹل کٹ', description: 'Offline-first inventory, sales, and khata for Pakistani shop owners.', manifest: '/manifest.json' }
export const viewport: Viewport = { themeColor: '#065f46', userScalable: true }

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return <html lang="ur" dir="rtl"><body>{children}</body></html>
}
