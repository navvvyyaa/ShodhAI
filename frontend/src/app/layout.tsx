import type { Metadata } from 'next'
import './globals.css'

export const metadata: Metadata = {
  title: 'Shodh-a-Code Contest Platform',
  description: 'Live coding contest platform with real-time judging',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  )
}
