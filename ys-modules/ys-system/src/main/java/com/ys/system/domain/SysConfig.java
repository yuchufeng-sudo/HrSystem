package com.ys.system.domain;

import com.ys.common.core.annotation.Excel;
import com.ys.common.core.annotation.Excel.ColumnType;
import com.ys.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Parameter configuration table sys_config
 *
 * @author ruoyi
 */
public class SysConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Parameter primary key */
    @Excel(name = "Parameter primary key", cellType = ColumnType.NUMERIC)
    private Long configId;

    /** Parameter Name */
    @Excel(name = "Parameter Name")
    private String configName;

    /** Parameter keyName */
    @Excel(name = "Parameter keyName")
    private String configKey;

    /** Parameter Key-Value */
    @Excel(name = "Parameter Key-Value")
    private String configValue;

    /** System built-in (Y=Yes N=No) */
    @Excel(name = "System built-in", readConverterExp = "Y=Yes,N=No")
    private String configType;

    public Long getConfigId()
    {
        return configId;
    }

    public void setConfigId(Long configId)
    {
        this.configId = configId;
    }

    @NotBlank(message = "Parameter Name cannot be empty.")
    @Size(min = 0, max = 100, message = "Parameter Name cannot exceed 100 characters.")
    public String getConfigName()
    {
        return configName;
    }

    public void setConfigName(String configName)
    {
        this.configName = configName;
    }

    @NotBlank(message = "Parameter keyName cannot be empty.")
    @Size(min = 0, max = 100, message = "Parameter keyName cannot exceed 100 characters.")
    public String getConfigKey()
    {
        return configKey;
    }

    public void setConfigKey(String configKey)
    {
        this.configKey = configKey;
    }

    @NotBlank(message = "Parameter Key-Value cannot be empty.")
    @Size(min = 0, max = 500, message = "Parameter Key-Value cannot exceed 500 characters.")
    public String getConfigValue()
    {
        return configValue;
    }

    public void setConfigValue(String configValue)
    {
        this.configValue = configValue;
    }

    public String getConfigType()
    {
        return configType;
    }

    public void setConfigType(String configType)
    {
        this.configType = configType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("configId", getConfigId())
            .append("configName", getConfigName())
            .append("configKey", getConfigKey())
            .append("configValue", getConfigValue())
            .append("configType", getConfigType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
