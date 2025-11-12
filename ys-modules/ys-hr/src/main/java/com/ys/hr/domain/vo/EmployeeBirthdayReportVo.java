package com.ys.hr.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/5/26
 */
@Data
public class EmployeeBirthdayReportVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long employeeId;
    private String fullName;
    private String departments;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
}
