package com.ys.hr.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ys.common.core.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author：hzz
 * @Date ：2025/6/11 16:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BirthdayReportVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long employeeId;
    @Excel(name = "Employee FULL NAME")
    private String fullName;
    private String avatarUrl;
    /** DATE OF BIRTH */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "DATE OF BIRTH", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dateOfBirth;
    private Long deptId;
    @Excel(name = " DEPARTMENT Name")
    private String deptName;

    //Week and Month
    private String month;
}
