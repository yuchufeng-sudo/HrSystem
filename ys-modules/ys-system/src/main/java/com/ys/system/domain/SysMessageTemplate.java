package com.ys.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *  MESSAGE TEMPLATE 
 MANAGEMENT  Object tb_message_template
 *
 * @author ruoyi
 * @date 2024-10-23
 */
@Data
@TableName("tb_message_template")
public class SysMessageTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  ID */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /** Name */
    @Excel(name = "Name")
    private String name;

    /** Content  */
    @Excel(name = "Content ")
    private String contentCn;

    private String contentEn;

   /** Parameter Configuration */
    @Excel(name = "Parameter Configuration")
    private String pathConfig;


    private String icon;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("contentCn", getContentCn())
            .append("contentEn", getContentEn())
            .append("pathConfig", getPathConfig())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
