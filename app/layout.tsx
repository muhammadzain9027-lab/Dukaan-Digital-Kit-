import type { Metadata, Viewport } from 'next'

export const metadata: Metadata = { title: 'دوکان | پروڈکٹس', description: 'اپنی دکان کے پروڈکٹس کا آسان نظم کریں' }
export const viewport: Viewport = { themeColor: '#123f38', userScalable: true }

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return <html lang="ur" dir="rtl"><body>{children}</body></html>
}
