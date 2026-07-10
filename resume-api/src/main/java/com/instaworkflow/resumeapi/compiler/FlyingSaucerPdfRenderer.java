package com.instaworkflow.resumeapi.compiler;

import com.instaworkflow.resumeapi.exception.PdfRenderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Renders XHTML → PDF using Flying Saucer 10.x + bundled OpenPDF 3.x.
 *
 * <p>Flying Saucer parses valid XHTML with CSS 2.1 and delegates PDF production
 * to OpenPDF (the LGPL fork of iText). No external processes are involved.
 *
 * <p>Note: {@link ITextRenderer} is not thread-safe; a new instance is created per request.
 */
@Component
public class FlyingSaucerPdfRenderer implements PdfRenderer {

    private static final Logger log = LoggerFactory.getLogger(FlyingSaucerPdfRenderer.class);

    @Override
    public byte[] render(String xhtml) {
        log.debug("Rendering PDF from XHTML ({} chars)", xhtml.length());
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(xhtml);
            renderer.layout();
            renderer.createPDF(out);
            byte[] pdf = out.toByteArray();
            log.debug("PDF generated successfully ({} bytes)", pdf.length);
            return pdf;
        } catch (IOException e) {
            log.error("PDF rendering failed", e);
            throw new PdfRenderException("PDF rendering failed: " + e.getMessage(), e);
        }
    }
}
