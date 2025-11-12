package com.ys.utils.excel;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class EasypoiExcelUtils {

    /**
     * Function Description: Import an Excel file based on the received Excel file
     * and encapsulate it into an Entity class.
     *
     * @param file
     * @param titleRows  Number of rows for the table title
     * @param headerRows Number of rows for the table header
     * @param pojoClass  ExcelEntity class
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows,
            Class<T> pojoClass) {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);

        List<T> list = null;
        try {
            InputStream inputStream = file.getInputStream();
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new RuntimeException("The data cannot be empty");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

        }
        return list;
    }

}
