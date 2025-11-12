package com.ys.common.core.utils.signature;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.jsoup.Jsoup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class PdfUtil {

    /*
     * Generatepdf
     * wmy 12:40 2019/8/9
     * @Param [guideBook, pdfPath]
     * @return java.lang.Boolean
     **/
    public static Boolean htmlToPdf(String content, String pdfPath) {
        try {
            // 1.New document
            Document document = new Document();
            // 2.Establish a Writer to associate with the document object. Through the Writer, the document can be written to disk.
            //When creating a PdfWriter object, the first parameter is a reference to the document object, and the second parameter is the actual name of the file, which also includes its output path.
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            // 3.Open the document.
            document.open();
            //The HTML to be parsed.
            //Convert HTML to plain text using the following method:
            //In the following, the style is set in the body, with the default font set to SimSun. This way, the exported HTML language will inherently include the font, ensuring that Chinese characters are exported successfully.
            content = content.replace("&nbsp;", "&#160;");
            content = content.replace("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">", "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></meta>");
            content = content.replace("(filtered)\">", "(filtered)\"></meta>");
            content = content.replaceAll("<br>", "<br></br>");
            content = "<html><body style=\"font-family: SimSun;\">" +
                    content + "</body></html>";
            org.jsoup.nodes.Document contentDoc = Jsoup.parseBodyFragment(content);
            org.jsoup.nodes.Document.OutputSettings outputSettings = new org.jsoup.nodes.Document.OutputSettings();
            outputSettings.syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
            contentDoc.outputSettings(outputSettings);
            String parsedHtml = contentDoc.outerHtml();

            InputStream cssIs = new ByteArrayInputStream("* {font-family: SimSun;}".getBytes("UTF-8"));

            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(parsedHtml.getBytes()), cssIs,new AsianFontProvider());

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
     * File
     * wmy 9:54 2019/8/12
     * @Param [request, response, inputStream, fileName]
     * @return void
     **/
    public static void download(HttpServletRequest request, HttpServletResponse response, InputStream inputStream, String fileName) {
        BufferedOutputStream bos = null;
        try {

            byte[] buffer = new byte[10240];
            fileName = fileName.replaceAll("[\\pP\\p{Punct}]", "-").replace(" ", "-").replaceAll("[-]+", "-") + ".pdf";
            String userAgent = request.getHeader("user-agent").toLowerCase();
            if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            }
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            bos = new BufferedOutputStream(response.getOutputStream());
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
