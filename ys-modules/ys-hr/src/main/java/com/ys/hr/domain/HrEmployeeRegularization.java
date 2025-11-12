package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Employee Regularization Management entity hr_employee_regularization
 *
 * @author ys
 * @date 2025-06-23
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrEmployeeRegularization extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Record ID */
    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;
    /** Employee ID */
    @Excel(name = "Employee ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long employeeId;
    /** Probation start date */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Probation start date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date probationStartDate;
    /** Probation end date */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Probation end date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date probationEndDate;
    /** Probation salary */
    @Excel(name = "Probation salary")
    private BigDecimal probationSalary;
    /** Regularization application date */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Regularization application date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applicationDate;
    /** Post-regularization salary */
    @Excel(name = "Post-regularization salary")
    private BigDecimal regularSalary;
    /** Approval status */
    @Excel(name = "Approval status")
    @Size(max = 20, message = "Approval status length cannot exceed 20 characters")
    private String status;

}
