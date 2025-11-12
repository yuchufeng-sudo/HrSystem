package com.ys.system.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.annotation.Excel.ColumnType;
import com.ys.common.core.web.domain.BaseEntity;

import java.util.Date;

/**
 * OPERATION LOG Record Table oper_log
 *
 */
public class SysOperLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  LOG primary key */
    @Excel(name = "OPERATIONSerial Number", cellType = ColumnType.NUMERIC)
    private Long operId;

    /** OPERATION Module */
    @Excel(name = "OPERATION Module")
    private String title;


    @Excel(name = "Business Type", readConverterExp = "0 = Others, 1 = ADD, 2 = MODIFY, 3 = DELETE, 4 = Authorization, 5 = EXPORT, 6 = Import, 7 = Force Logout, 8 = Generate Code, 9 = CLEAR Data")
    private Integer businessType;

    /** Business Type Array */
    private Integer[] businessTypes;

    /** Request Method */
    @Excel(name = "Request Method")
    private String method;

    /** Request Method */
    @Excel(name = "Request Method")
    private String requestMethod;


    @Excel(name = "OPERATION Category", readConverterExp = "0 = Others, 1 = Background USER, 2 = Mobile USER")
    private Integer operatorType;

    /** OPERATION Staff */
    @Excel(name = "OPERATION Staff")
    private String operName;

    /**  DEPARTMENT
Name */
    @Excel(name = " DEPARTMENT Name")
    private String deptName;

    /**  Requesturl */
    @Excel(name = " RequestAddress")
    private String operUrl;

    /** OPERATIONAddress */
    @Excel(name = "OPERATIONAddress")
    private String operIp;

    /** Request Parameters */
    @Excel(name = "Request Parameters")
    private String operParam;

    /** Return Parameters */
    @Excel(name = "Return Parameters")
    private String jsonResult;

    /** OPERATIONStatus */
    @Excel(name = "Status", readConverterExp = "0 = Normal, 1 = Abnormal")
    private Integer status;

    /** Error Message  */
    @Excel(name = "Error Message ")
    private String errorMsg;

    /** OPERATION TIME  */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "OPERATION TIME ", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    /** Consumption TIME  */
    @Excel(name = "Consumption TIME ", suffix = "Millisecond")
    private Long costTime;

    public Long getOperId()
    {
        return operId;
    }

    public void setOperId(Long operId)
    {
        this.operId = operId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Integer getBusinessType()
    {
        return businessType;
    }

    public void setBusinessType(Integer businessType)
    {
        this.businessType = businessType;
    }

    public Integer[] getBusinessTypes()
    {
        return businessTypes;
    }

    public void setBusinessTypes(Integer[] businessTypes)
    {
        this.businessTypes = businessTypes;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getRequestMethod()
    {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    public Integer getOperatorType()
    {
        return operatorType;
    }

    public void setOperatorType(Integer operatorType)
    {
        this.operatorType = operatorType;
    }

    public String getOperName()
    {
        return operName;
    }

    public void setOperName(String operName)
    {
        this.operName = operName;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getOperUrl()
    {
        return operUrl;
    }

    public void setOperUrl(String operUrl)
    {
        this.operUrl = operUrl;
    }

    public String getOperIp()
    {
        return operIp;
    }

    public void setOperIp(String operIp)
    {
        this.operIp = operIp;
    }

    public String getOperParam()
    {
        return operParam;
    }

    public void setOperParam(String operParam)
    {
        this.operParam = operParam;
    }

    public String getJsonResult()
    {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult)
    {
        this.jsonResult = jsonResult;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public Date getOperTime()
    {
        return operTime;
    }

    public void setOperTime(Date operTime)
    {
        this.operTime = operTime;
    }

    public Long getCostTime()
    {
        return costTime;
    }

    public void setCostTime(Long costTime)
    {
        this.costTime = costTime;
    }
}
