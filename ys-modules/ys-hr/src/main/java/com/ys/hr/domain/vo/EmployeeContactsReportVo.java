package com.ys.hr.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/5/26
 */
@Data
public class EmployeeContactsReportVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long employeeId;
    private String fullName;
    private String contactPhone;
}
