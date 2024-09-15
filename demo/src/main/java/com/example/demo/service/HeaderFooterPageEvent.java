package com.example.demo.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HeaderFooterPageEvent extends PdfPageEventHelper {

    private Image headerImage;
    private Image footerImage;
    private float headerImageHeight;

    public HeaderFooterPageEvent(String headerImagePath, String footerImagePath) {
        try {
            InputStream headerImageStream = getClass().getClassLoader().getResourceAsStream(headerImagePath);
            InputStream footerImageStream = getClass().getClassLoader().getResourceAsStream(footerImagePath);

            if (headerImageStream != null && footerImageStream != null) {
                // Convert InputStream to URL for Image.getInstance()
                URL headerImageUrl = getClass().getClassLoader().getResource(headerImagePath);
                URL footerImageUrl = getClass().getClassLoader().getResource(footerImagePath);

                this.headerImage = Image.getInstance(headerImageUrl);
                this.footerImage = Image.getInstance(footerImageUrl);
            } else {
                throw new IOException("Image resources not found.");
            }
        } catch (BadElementException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            Rectangle pageSize = document.getPageSize();
            float pageWidth = pageSize.getWidth();
            float pageHeight = pageSize.getHeight();

            // Header image centered and a bit larger
            if (headerImage != null) {
                float headerImageWidth = pageWidth * 0.54f;
                headerImageHeight = (headerImageWidth / headerImage.getWidth()) * headerImage.getHeight();
                headerImage.scaleToFit(headerImageWidth, headerImageHeight);
                float headerImageX = (pageWidth - headerImageWidth) / 2; // Center horizontally
                headerImage.setAbsolutePosition(headerImageX, pageHeight - headerImageHeight - 20); // Add some margin from the top
                writer.getDirectContent().addImage(headerImage);
            }

            // Add content after the header image
            super.onEndPage(writer, document); // Call the superclass method to maintain other functionalities

            // Footer image scaled to fit and positioned at the bottom with space from the border
            if (footerImage != null) {
                float footerImageWidth = 100; // Adjust the width as needed
                float footerImageHeight = (footerImageWidth / footerImage.getWidth()) * footerImage.getHeight();
                float footerImageMargin = 20; // Adjust the margin as needed
                footerImage.scaleToFit(footerImageWidth, footerImageHeight);
                footerImage.setAbsolutePosition(pageWidth - footerImageWidth - footerImageMargin, footerImageMargin);
                writer.getDirectContent().addImage(footerImage);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public float getHeaderImageHeight() {
        return headerImageHeight;
    }
}
