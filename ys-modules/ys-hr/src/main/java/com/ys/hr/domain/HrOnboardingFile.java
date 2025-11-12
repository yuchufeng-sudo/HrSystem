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
 * Onboarding File entity hr_onboarding_file
 *
 * @author ys
 * @date 2025-10-13
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrOnboardingFile extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type =  IdType.ASSIGN_UUID )
    private String id;
    /** Name */
    @Excel(name = "Name")
    private String name;
    /** File Name */
    @Excel(name = "File Name")
    private String fileName;
    /** File Url */
    @Excel(name = "File Url")
    private String fileUrl;
    /** File Type */
    @Excel(name = "File Type")
    private String fileType;
    /** Mandatory or optional */
    @Excel(name = "Mandatory or optional")
    private Boolean isMandatory;
    /** Step */
    @Excel(name = "Step")
    private Integer step;
    /** Enterprise Id */
    @Excel(name = "Enterprise Id")
    private String enterpriseId;

    @TableField(exist = false)
    private String employeesId;

    @TableField(exist = false)
    private String resultId;

    @TableField(exist = false)
    private Integer isResult;

    @TableField(exist = false)
    private String result;

}
