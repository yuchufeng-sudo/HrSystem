package com.ys.common.core.web.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ys
 */

@Data
@AllArgsConstructor
public class TableDataInfo implements Serializable
{
    private static final long serialVersionUID = 1L;


    private long total;


    private List<?> rows;


    private int code;

    /** Message Content  */
    private String msg;

    /**
     *
     */
    public TableDataInfo()
    {
    }

    /**
     *
     *
     * @param list
     * @param total
     */
    public TableDataInfo(List<?> list, int total)
    {
        this.rows = list;
        this.total = total;
    }
}
