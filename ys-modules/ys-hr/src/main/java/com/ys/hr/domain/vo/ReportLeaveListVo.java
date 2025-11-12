package com.ys.hr.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/21
 */
@Data
public class ReportLeaveListVo {
    private String employeeName;
    private String deptName;
    private String leaveType;
    private Integer opening;
    private Integer taken;
}
