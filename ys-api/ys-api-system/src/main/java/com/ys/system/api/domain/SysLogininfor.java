package com.ys.system.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.annotation.Excel.ColumnType;
import com.ys.common.core.web.domain.BaseEntity;

import java.util.Date;

/**
 * System Access Record Table sys_logininfor
 *
 */
public class SysLogininfor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "Serial Number", cellType = ColumnType.NUMERIC)
    private Long infoId;

    /** USER Account */
    @Excel(name = "USER Account")
    private String userName;

    /** Status 0SUCCESS 1Failure */
    @Excel(name = "Status", readConverterExp = "0=SUCCESS,1=Failure")
    private String status;

    /** Address */
    @Excel(name = "Address")
    private String ipaddr;

    /** Description */
    @Excel(name = "Description")
    private String msg;

    /** Access TIME  */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "Access TIME ", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date accessTime;

    public Long getInfoId()
    {
        return infoId;
    }

    public void setInfoId(Long infoId)
    {
        this.infoId = infoId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Date getAccessTime()
    {
        return accessTime;
    }

    public void setAccessTime(Date accessTime)
    {
        this.accessTime = accessTime;
    }
}
