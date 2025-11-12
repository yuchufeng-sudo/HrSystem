package com.ys.common.core.utils.signature;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;

/**
 * Created by IntelliJ IDEA.
 * 
 * Date: 2021/11/12
 * Time: 14:46
 */
public class AsianFontProvider extends XMLWorkerFontProvider {
    @Override
    public Font getFont(String fontname, String encoding, float size, final int style) {
        try {
            BaseFont bfChinese =BaseFont.createFont( "STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            return new Font(bfChinese, size, style);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.getFont(fontname, encoding, size, style);
    }
}
