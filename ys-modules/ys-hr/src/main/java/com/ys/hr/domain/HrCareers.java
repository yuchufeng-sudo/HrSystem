package com.ys.hr.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import javax.validation.constraints.Size;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ys.common.core.web.domain.BaseEntity;
import com.ys.common.core.annotation.Excel;
import javax.validation.constraints.NotNull;

/**
 * Company careers information entity hr_careers
 *
 * @author ys
 * @date 2025-09-27
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrCareers extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary key ID */
    @TableId(value = "id", type =  IdType.ASSIGN_UUID )
    private String id;
    /** Company name */
    @Excel(name = "Company name")
    @NotNull(message = "Company name cannot be empty")
    private String careersName;
    /** Company logo URL */
    @Excel(name = "Company logo URL")
    private String logo;
    /** Company category/label */
    @Excel(name = "Company category/label")
    private String label;
    /** Company introduction */
    @Excel(name = "Company introduction")
    private String about;
    /** Company description */
    @Excel(name = "Company description")
    private String description;
    /** Company images (JSON array) */
    @Excel(name = "Company images (JSON array)")
    private String introImages;
    /** Social media links (JSON) */
    @Excel(name = "Social media links (JSON)")
    private String socials;
    /** Show logo in preview */
    @Excel(name = "Show logo in preview")
    private Boolean showPreviewLogo;
    /** Show about in preview */
    @Excel(name = "Show about in preview")
    private Boolean showPreviewAbout;
    /** Show images in preview */
    @Excel(name = "Show images in preview")
    private Boolean showPreviewImages;
    /** Show socials in preview */
    @Excel(name = "Show socials in preview")
    private Boolean showPreviewSocials;
    /** enterprise ID */
    @Excel(name = "enterprise ID")
    private String enterpriseId;

}
