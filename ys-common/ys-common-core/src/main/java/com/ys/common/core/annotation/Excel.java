package com.ys.common.core.annotation;

import com.ys.common.core.utils.poi.ExcelHandlerAdapter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

/**
 * Custom Export Excel Data Annotation
 *
 * @author ys
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel
{
    /**
     * When exporting, sorting in Excel.
     */
    public int sort() default Integer.MAX_VALUE;

    /**
     * Export the Name field to Excel.
     */
    public String name() default "";

    /**
     * Date format, such as: yyyy-MM-dd
     */
    public String dateFormat() default "";

    /**
     * Read the Content and convert it into an Expression (e.g., 0 = Male, 1 = Female, 2 = Unknown)
     */
    public String readConverterExp() default "";

    /**
     * Delimiter, read Character string group Content
     */
    public String separator() default ",";

    /**
     * BigDecimal Precision Default: -1 (By default, BigDecimal formatting is not enabled)
     */
    public int scale() default -1;

    /**
     * BigDecimal rounding rule Default: BigDecimal.ROUND_HALF_EVEN
     */
    public int roundingMode() default BigDecimal.ROUND_HALF_EVEN;

    /**
     * The height of each column in Excel when exporting.
     */
    public double height() default 14;

    /**
     * When exporting, the width of each column in Excel.
     */
    public double width() default 16;

    /**
     *
     */
    public String suffix() default "";

    /**
     *
     */
    public String defaultValue() default "";

    /**
     * Prompt / Tip Information
     */
    public String prompt() default "";

    /**
     * Set columns whose content can only be selected, not input .
     */
    public String[] combo() default {};

    /**
     * Whether to merge cells vertically to meet the requirement: cells containing listSet.
     */
    public boolean needMerge() default false;

    /**
     * Whether to export data. To address the requirement: sometimes we need to export a template where the title is required, but the content needs to be manually filled in by the user.
     */
    public boolean isExport() default true;

    /**
     * The attribute Name in another class supports multi-level OBTAIN, separated by decimal points.
     */
    public String targetAttr() default "";

    /**
     * Whether to automatically count data, and append a row of total statistical data in Finally.
     */
    public boolean isStatistics() default false;

    /**
     * Export Type（0Digital  1 Character string）
     */
    public ColumnType cellType() default ColumnType.STRING;

    /**
     * Export column header background color
     */
    public IndexedColors headerBackgroundColor() default IndexedColors.GREY_50_PERCENT;

    /**
     * Export column header font color.
     */
    public IndexedColors headerColor() default IndexedColors.WHITE;

    /**
     * Export cell background color.
     */
    public IndexedColors backgroundColor() default IndexedColors.WHITE;

    /**
     * Export cell font color.
     */
    public IndexedColors color() default IndexedColors.BLACK;

    /**
     * Export field alignment.
     */
    public HorizontalAlignment align() default HorizontalAlignment.CENTER;

    /**
     * Custom Data Processor
     */
    public Class<?> handler() default ExcelHandlerAdapter.class;

    /**
     * Custom Data ProcessorParameters
     */
    public String[] args() default {};

    /**
     *Field Type (0: EXPORTImport; 1: Export only; 2: Import only)
     */
    Type type() default Type.ALL;

    public enum Type
    {
        ALL(0), Export(1), IMPORT(2);
        private final int value;

        Type(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }

    public enum ColumnType
    {
        NUMERIC(0), STRING(1), IMAGE(2), TEXT(3);
        private final int value;

        ColumnType(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }
}
