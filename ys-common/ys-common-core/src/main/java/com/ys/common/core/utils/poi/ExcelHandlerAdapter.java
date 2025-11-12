package com.ys.common.core.utils.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel Data Format Processing Adapter
 * 
 * @author ruoyi
 */
public interface ExcelHandlerAdapter
{
    /**
     * Formatting
     * 
     * @param value Cell data value
     * @param The args parameter of the Excel annotation is an array.
     * @param cell The cell object.
     * @param wbWorkbook Object
     *
     * @return Processed value
     */
    Object format(Object value, String[] args, Cell cell, Workbook wb);
}
