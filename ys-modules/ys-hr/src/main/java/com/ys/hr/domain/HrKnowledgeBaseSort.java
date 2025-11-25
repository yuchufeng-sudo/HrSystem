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

import java.util.ArrayList;
import java.util.List;

/**
 * Knowledge base CLASSIFICATION entity hr_knowledge_base_sort
 *
 * @author ys
 * @date 2025-06-04
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrKnowledgeBaseSort extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Knowledge base CLASSIFICATION id */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /** Parent ID */
    @Excel(name = "Parent ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    /** Tree Level */
    @Excel(name = "Tree Level")
    private Integer level;
    /** Sorting */
    @Excel(name = "Sorting")
    private Integer orderNum;
    private String enterpriseId;
    /** Ancestor-level list */
    @Excel(name = "Ancestor-level list")
    private String ancestors;
    /** CLASSIFICATION Name */
    @Excel(name = "CLASSIFICATION Name")
    private String sortName;
    /** Status 0 Normal 1 Disabled */
    @Excel(name = "Status 0 Normal 1 Disabled")
    private String status;
    /** $column.columnComment */
    private String delFlag;
    /** Child Node list */
    @TableField(exist = false)
    private List<HrKnowledgeBaseSort> children = new ArrayList<>();
    // @TableField(exist = false)
    // private String titleId;
    // @TableField(exist = false)
    // private String konwledgeContents;
    @TableField(exist = false)
    private List<KnowledgeTitle> knowledgeTitles = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KnowledgeTitle extends HrKnowledgeBase {
        private String titleId;
        private String knowledgeContents;
        private String knowledgeTitle;
    }
}
