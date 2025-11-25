package com.ys.hr.domain.vo;

import lombok.Data;

@Data
public class EmployeeLeaveCountVo {

    /**
     * Total Leave Application Days
     */
    private Long totalDays;

    /**
     * This Month's Leave Application Days
     */
    private Long thisMonthDays;

    /**
     * Last Month's Leave Application Days
     */
    private Long lastMonthDays;
}
