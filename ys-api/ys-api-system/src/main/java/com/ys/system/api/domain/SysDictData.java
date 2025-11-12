package com.ys.system.api.domain;

import com.ys.common.core.annotation.Excel;
import com.ys.common.core.annotation.Excel.ColumnType;
import com.ys.common.core.constant.UserConstants;
import com.ys.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *  DICTIONARY DATA Table sys_dict_data
 */
public class SysDictData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Dictionary Coding */
    @Excel(name = "Dictionary Coding", cellType = ColumnType.NUMERIC)
    private Long dictCode;

    /** Lexicographical Order */
    @Excel(name = "Lexicographical Order", cellType = ColumnType.NUMERIC)
    private Long dictSort;

    /** Dictionary Label */
    @Excel(name = "Dictionary Label")
    private String dictLabel;

    /** Dictionary Label */
    @Excel(name = "Dictionary Label")
    private String dictValue;

    /**  DICTIONARY TYPE  */
    @Excel(name = " DICTIONARY TYPE ")
    private String dictType;

    /** Style attributes (other style extensions)） */
    private String cssClass;

    /** Table Dictionary Style */
    private String listClass;

    /** Whether it is the default (Y for Yes, N for No) */
    @Excel(name = "Whether it is the default", readConverterExp = "Y for Yes,N for No")
    private String isDefault;

    /** Status (0 for normal, 1 for inactive) */
    @Excel(name = "State", readConverterExp = "0 = Normal, 1 = Disabled")
    private String status;

    public Long getDictCode()
    {
        return dictCode;
    }

    public void setDictCode(Long dictCode)
    {
        this.dictCode = dictCode;
    }

    public Long getDictSort()
    {
        return dictSort;
    }

    public void setDictSort(Long dictSort)
    {
        this.dictSort = dictSort;
    }

    @NotBlank(message = "Dictionary LabelCANNOT BE EMPTY.")
    @Size(min = 0, max = 100, message = "Dictionary LabelLENGTH ")
    public String getDictLabel()
    {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel)
    {
        this.dictLabel = dictLabel;
    }

    @NotBlank(message = "Dictionary LabelCANNOT BE EMPTY.")
    @Size(min = 0, max = 100, message = "Dictionary LabelLENGTH Please translate the following text into English. It should not exceed 100 characters.")
    public String getDictValue()
    {
        return dictValue;
    }

    public void setDictValue(String dictValue)
    {
        this.dictValue = dictValue;
    }

    @NotBlank(message = " DICTIONARY TYPE CANNOT BE EMPTY.")
    @Size(min = 0, max = 100, message = " DICTIONARY TYPE LENGTH Please translate the following text into English. It should not exceed 100 characters.")
    public String getDictType()
    {
        return dictType;
    }

    public void setDictType(String dictType)
    {
        this.dictType = dictType;
    }

    @Size(min = 0, max = 100, message = "The style attribute LENGTH cannot exceed 100 characters.")
    public String getCssClass()
    {
        return cssClass;
    }

    public void setCssClass(String cssClass)
    {
        this.cssClass = cssClass;
    }

    public String getListClass()
    {
        return listClass;
    }

    public void setListClass(String listClass)
    {
        this.listClass = listClass;
    }

    public boolean getDefault()
    {
        return UserConstants.YES.equals(this.isDefault);
    }

    public String getIsDefault()
    {
        return isDefault;
    }

    public void setIsDefault(String isDefault)
    {
        this.isDefault = isDefault;
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
            .append("dictCode", getDictCode())
            .append("dictSort", getDictSort())
            .append("dictLabel", getDictLabel())
            .append("dictValue", getDictValue())
            .append("dictType", getDictType())
            .append("cssClass", getCssClass())
            .append("listClass", getListClass())
            .append("isDefault", getIsDefault())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
