package com.example.resumeapi.compiler;

/**
 * Converts a fully-rendered XHTML string into a PDF byte array.
 */
public interface PdfRenderer {

    /**
     * @param xhtml a well-formed XHTML document string
     * @return the binary content of the generated PDF
     */
    byte[] render(String xhtml);
}
