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
 * KNOWLEDGE BASE entity hr_knowledge_base
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

    /** KNOWLEDGE BASE id */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /** Parent ID */
    @Excel(name = "Parent ID")
    private Long parentId;
    /** KNOWLEDGE BASE Title */
    @Excel(name = "KNOWLEDGE BASE Title")
    private String knowledgeTitle;
    /** KNOWLEDGE BASE Content */
    @Excel(name = "KNOWLEDGE BASE Content")
    private String knowledgeContents;
    /** KNOWLEDGE BASE Tag */
    @Excel(name = "KNOWLEDGE BASE Tag")
    private String knowledgeLabel;
    @Excel(name = "Ancestor-level LIST")
    private String ancestors;
    /** Status 0 Normal 1 Disabled */
    @Excel(name = "Status 0 Normal 1 Disabled")
    private String status;

    private String enterpriseId;
    private String delFlag;
    /** KNOWLEDGE BASE Cover Image */
    @Excel(name = "KNOWLEDGE BASE Cover Image")
    private String knowledgeCover;

    @TableField(exist = false)
    private String parentName;

    @TableField(exist = false)
    private String titleId;

}
