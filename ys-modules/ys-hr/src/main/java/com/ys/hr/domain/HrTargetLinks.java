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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Table storing relationships between targets entity hr_target_links
 *
 * @author ys
 * @date 2025-06-18
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrTargetLinks extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary key identifier */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** Reference to the source target */
    @Excel(name = "Reference to the source target")
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "Source target ID cannot be null")
    private Long sourceTargetId;

    /** Reference to the linked target */
    @Excel(name = "Reference to the linked target")
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "Linked target ID cannot be null")
    private Long linkedTargetId;

    /** Type of relationship between targets */
    @Excel(name = "Type of relationship between targets")
    @NotNull(message = "Relationship type cannot be null")
    @Size(max = 50, message = "Relationship type must be less than 50 characters")
    private String relationType;

    @TableField(exist = false)
    private String linkedTargetName;
}
