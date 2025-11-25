package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Knowledge base entity hr_knowledge_base
 *
 * @author ys
 * @date 2025-06-04
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrKnowledgeBase extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Knowledge base id */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /** Parent ID */
    @Excel(name = "Parent ID")
    private Long parentId;
    /** Knowledge base Title */
    @Excel(name = "Knowledge base Title")
    private String knowledgeTitle;
    /** Knowledge base Content */
    @Excel(name = "Knowledge base Content")
    private String knowledgeContents;
    /** Knowledge base Tag */
    @Excel(name = "Knowledge base Tag")
    private String knowledgeLabel;
    @Excel(name = "Ancestor-level list")
    private String ancestors;
    /** Status 0 Normal 1 Disabled */
    @Excel(name = "Status 0 Normal 1 Disabled")
    private String status;

    private String enterpriseId;
    private String delFlag;
    /** Knowledge base Cover Image */
    @Excel(name = "Knowledge base Cover Image")
    private String knowledgeCover;

    @TableField(exist = false)
    private String parentName;

    @TableField(exist = false)
    private String titleId;

}
