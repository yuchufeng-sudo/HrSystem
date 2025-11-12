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

/**
 * Document sharing information entity hr_document_share
 *
 * @author ys
 * @date 2025-05-27
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrDocumentShare extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Share record ID */
    @TableId(value = "share_id", type =  IdType.AUTO )
    @JsonSerialize(using = ToStringSerializer.class)
    private Long shareId;
    /** Shared document ID */
    @Excel(name = "Shared document ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long documentId;
    /** User ID who shared the document */
    @Excel(name = "User ID who shared the document")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sharedBy;
    /** User ID who received the shared document */
    @Excel(name = "User ID who received the shared document")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sharedTo;
    /** Permission type (READ, DOWNLOAD) */
    @Excel(name = "Permission type (READ, DOWNLOAD)")
    @NotNull(message = "Permission type (READ, DOWNLOAD) cannot be empty")
    private String permissionType;
    /** Share status (0-disabled, 1-enabled) */
    @Excel(name = "Share status (0-disabled, 1-enabled)")
    private String status;

    @TableField(exist = false)
    private String avatarUrl;

    @TableField(exist = false)
    private String fullName;

    @TableField(exist = false)
    private String email;

}
