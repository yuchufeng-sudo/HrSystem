package com.ys.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *  Message Template management Object tb_message_template
 *
 * @author ys
 * @date 2024-10-23
 */
@Data
@TableName("tb_message_template")
@NoArgsConstructor
@AllArgsConstructor
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

}
