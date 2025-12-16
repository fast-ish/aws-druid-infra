import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";

const inter = Inter({
  subsets: ["latin"],
  variable: "--font-inter",
});

export const metadata: Metadata = {
  title: "Apache Druid on AWS | Production Analytics Infrastructure",
  description: "Interactive architecture diagram for Apache Druid deployment on AWS EKS. Built with CDK, Helm, PostgreSQL, MSK, and S3 deep storage.",
  keywords: ["Apache Druid", "AWS", "EKS", "Kubernetes", "Architecture", "CDK", "Helm", "Analytics", "OLAP"],
  authors: [{ name: "Platform Engineering" }],
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className="dark">
      <body className={`${inter.variable} font-sans antialiased bg-slate-950 text-white`}>
        {children}
      </body>
    </html>
  );
}
