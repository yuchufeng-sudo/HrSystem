package com.ys.system.api.domain;

import com.ys.common.core.annotation.Excel;
import com.ys.common.core.annotation.Excel.ColumnType;
import com.ys.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 *
 */
public class SysDictType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Dictionary primary key */
    @Excel(name = "Dictionary primary key", cellType = ColumnType.NUMERIC)
    private Long dictId;

    /** Dictionary Name */
    @Excel(name = "Dictionary Name")
    private String dictName;

    /**  DICTIONARY TYPE  */
    @Excel(name = " DICTIONARY TYPE ")
    private String dictType;

    /** Status (0 for normal, 1 for inactive) */
    @Excel(name = "Status", readConverterExp = "0 = Normal, 1 = Disabled")
    private String status;

    public Long getDictId()
    {
        return dictId;
    }

    public void setDictId(Long dictId)
    {
        this.dictId = dictId;
    }

    @NotBlank(message = "Dictionary NameCANNOT BE EMPTY.")
    @Size(min = 0, max = 100, message = " DICTIONARY TYPE The name LENGTH cannot exceed 100 characters.")
    public String getDictName()
    {
        return dictName;
    }

    public void setDictName(String dictName)
    {
        this.dictName = dictName;
    }

    @NotBlank(message = " DICTIONARY TYPE CANNOT BE EMPTY.")
    @Size(min = 0, max = 100, message = " DICTIONARY TYPE The type LENGTH cannot exceed 100 characters.")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = " DICTIONARY TYPE It must start with a letter and can only contain lowercase letters, numbers, and underscores")
    public String getDictType()
    {
        return dictType;
    }

    public void setDictType(String dictType)
    {
        this.dictType = dictType;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dictId", getDictId())
            .append("dictName", getDictName())
            .append("dictType", getDictType())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
