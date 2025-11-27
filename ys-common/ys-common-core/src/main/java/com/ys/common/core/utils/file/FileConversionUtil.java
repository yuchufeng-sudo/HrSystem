package com.ys.common.core.utils.file;

import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.ys.common.core.utils.signature.AsianFontProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 *
 */
@Slf4j
public class FileConversionUtil {

    //
    static {
        try {
            //
            FontFactory.registerDirectories();
            FontFactory.register("fonts"); //
        } catch (Exception e) {
            log.error("Font initialization failure", e);
        }
    }

    /**
     *
     * @param content
     * @return
     */
    public static File convertHtmlToPdf(String content){
        String fullHtmlDocument = getFullHtmlDocument(content);

        fullHtmlDocument = fullHtmlDocument.replaceAll("<p>\\s*(<br\\s*/?>)?\\s*</p>", "<p>&nbsp;</p>");


        fullHtmlDocument = fullHtmlDocument.replaceAll("<br\\s*/?>\\s*<br\\s*/?>", "<br/>");

        File pdfFile = null;
        try {
            pdfFile = File.createTempFile("contract-" + System.currentTimeMillis(), ".pdf");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Create  Temporary PDFFile: {}", pdfFile.getAbsolutePath());

        Document document = new Document(PageSize.A4);
        PdfWriter writer = null;

        try (OutputStream os = new FileOutputStream(pdfFile)) {
            writer = PdfWriter.getInstance(document, os);
            document.open();


            XMLWorkerHelper.getInstance().parseXHtml(
                    writer, document,
                    new ByteArrayInputStream(fullHtmlDocument.getBytes(StandardCharsets.UTF_8)),
                    null, StandardCharsets.UTF_8,
                    new AsianFontProvider()
            );

            document.close();
            log.info("Contract PDFFileGenerateSUCCESS: {}", pdfFile.getAbsolutePath());
            return pdfFile;
        } catch (Exception e) {
            log.error("PDFFileGenerateFailure", e);

            if (document.isOpen()) {
                document.close();
            }

            if (writer != null) {
                writer.close();
            }
            // Delete Temporary File
//            if (pdfFile.exists()) {
//                pdfFile.delete();
//            }
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     *
     * @param content
     * @return
     */
    private static String getFullHtmlDocument(String content) {
        return ""
                + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""
                + "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                + "<head>"
                + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />"
                + "  <style type=\"text/css\">"
                + "    body { font-family: 'STSong-Light', SimSun, sans-serif; font-size:14px; line-height:1.6; }"
                + "    h1, h2, h3, h4 { text-align:center; }"
                + "    .contract-title { font-size:18px; font-weight:bold; margin:20px 0; }"
                + "    /* …  … */"
                + "    .page-break { page-break-after:always; }"
                + "    table { width:100%; border-collapse:collapse; margin:10px 0; }"
                + "    table, th, td { border:1px solid #000; }"
                + "    th, td { padding:8px; text-align:left; }"
                + "  </style>"
                + "</head>"
                + "<body>"
                +      content
                + "</body>"
                + "</html>";
    }


}
