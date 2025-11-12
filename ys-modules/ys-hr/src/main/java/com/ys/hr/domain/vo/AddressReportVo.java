package com.ys.hr.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ys.common.core.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：hzz
 * @Date ：2025/7/8 9:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressReportVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long employeeId;
    @Excel(name = "EMPLOYEE FULL NAME")
    private String fullName;
    private String avatarUrl;
    private Long deptId;
    private String country;
    private String address1;
    private String address2;
    private String city;
    private String province;
    private String postCode;
}
