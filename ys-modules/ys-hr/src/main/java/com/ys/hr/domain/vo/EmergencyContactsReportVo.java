package com.ys.hr.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ys.common.core.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：hzz
 * @Date ：2025/6/11 14:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactsReportVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long employeeId;
    @Excel(name = "EMPLOYEE FULL NAME")
    private String fullName;
    @Excel(name = "Emergency Contact")
    private String emergencyContact;
    @Excel(name = "Emergency Contact")
    private String emergencyContactName;
    private String avatarUrl;
}
