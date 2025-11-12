package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Email Templates entity hr_email_template
 *
 * @author ys
 * @date 2025-09-09
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrEmailTemplate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Templates ID */
    @Excel(name = "Templates ID")
    @TableId(value = "template_id", type =  IdType.AUTO )
    private Long templateId;
    /** Templates Name */
    @Excel(name = "Templates Name")
    @NotNull(message = "Templates Name cannot be empty")
    private String templateName;
    /** Templates Subject */
    @Excel(name = "Templates Subject")
    @NotNull(message = "Templates Subject cannot be empty")
    private String templateSubject;
    /** Templates Categories */
    @Excel(name = "Templates Categories")
    private String templateCategories;
    /** Email Body */
    @Excel(name = "Email Body")
    @NotNull(message = "Email Body cannot be empty")
    private String templateBody;
    /** Enterprise ID */
    @Excel(name = "Enterprise ID")
    private String enterpriseId;
    /** Template Attachment */
    @Excel(name = "Template Attachment")
    private String templateAttachment;
    @Excel(name = "Template Attachment")
    private String templateKey;
    @TableField(exist = false)
    private String sendTo;
    @TableField(exist = false)
    private Long[] sendCc;
    @TableField(exist = false)
    private Long[] sendBcc;
    @TableField(exist = false)
    List<String> attachments;
    @TableField(exist = false)
    private Long contactId;
    @TableField(exist = false)
    private Long leadId;
    @TableField(exist = false)
    private Long contactOwner;
}
