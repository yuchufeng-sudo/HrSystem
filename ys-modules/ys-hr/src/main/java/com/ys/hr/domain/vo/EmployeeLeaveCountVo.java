package com.ys.hr.domain.vo;

import lombok.Data;

@Data
public class EmployeeLeaveCountVo {

    /**
     * Total LEAVE APPLICATION Days
     */
    private Long totalDays;

    /**
     * This Month's LEAVE APPLICATION Days
     */
    private Long thisMonthDays;

    /**
     * Last Month's LEAVE APPLICATION Days
     */
    private Long lastMonthDays;
}
