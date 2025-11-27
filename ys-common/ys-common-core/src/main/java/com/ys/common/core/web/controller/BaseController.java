package com.ys.common.core.web.controller;

import com.github.pagehelper.PageInfo;
import com.ys.common.core.constant.HttpStatus;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.PageUtils;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author ys
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date  Type Conversion
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     *
     */
    protected void startPage()
    {
        PageUtils.startPage();
    }

    /**
     *
     */
    protected void clearPage()
    {
        PageUtils.clearPage();
    }

    /**
     *
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setRows(list);
        rspData.setMsg("Query Success");
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * Return SUCCESS
     */
    public AjaxResult success()
    {
        return AjaxResult.success();
    }

    /**
     * Return SUCCESSMessage
     */
    public AjaxResult success(String message)
    {
        return AjaxResult.success(message);
    }

    /**
     * Return SUCCESSMessage
     */
    public AjaxResult success(Object data)
    {
        return AjaxResult.success(data);
    }

    /**
     * Return FailureMessage
     */
    public AjaxResult error()
    {
        return AjaxResult.error();
    }

    /**
     * Return FailureMessage
     */
    public AjaxResult error(String message)
    {
        return AjaxResult.error(message);
    }

    /**
     *
     */
    public AjaxResult warn(String message)
    {
        return AjaxResult.warn(message);
    }

    /**
     *
     *
     * @param rows
     * @return OPERATIONResult
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     *
     *
     * @param result Result
     * @return OPERATIONResult
     */
    protected AjaxResult toAjax(boolean result)
    {
        return result ? success() : error();
    }
}
