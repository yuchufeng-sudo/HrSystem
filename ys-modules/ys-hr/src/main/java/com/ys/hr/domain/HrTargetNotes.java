package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * Table storing notes associated with targets entity hr_target_notes
 *
 * @author ys
 * @date 2025-06-18
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrTargetNotes extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * Primary key identifier
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Reference to the parent target
     */
    @Excel(name = "Reference to the parent target")
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "Target ID cannot be null")
    private Long targetId;

    /**
     * Content of the note
     */
    @Excel(name = "Content of the note")
    @NotNull(message = "Note content cannot be null")
    @Size(max = 2000, message = "Note content must be less than 2000 characters")
    private String content;

    /**
     * Author of the note
     */
    @Excel(name = "Author of the note")
    @NotNull(message = "Author cannot be null")
    @Size(max = 100, message = "Author name must be less than 100 characters")
    private String author;
}
