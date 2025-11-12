package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Employee certification issuance entity hr_employee_certification
 *
 * @author ys
 * @date 2025-06-24
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrEmployeeCertification extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;
    /** Certification ID */
    @Excel(name = "Certification ID")
    private Long certificationId;
    /** Employee ID */
    @Excel(name = "Employee ID")
    private Long employeeId;
    /** Issue date */
    @Excel(name = "Issue date")
    private String issueDate;

}
