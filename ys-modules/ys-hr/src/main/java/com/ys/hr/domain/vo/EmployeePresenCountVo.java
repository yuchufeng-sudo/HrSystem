package com.ys.hr.domain.vo;

import lombok.Data;

@Data
public class EmployeePresenCountVo {

    /**
     * This Month's Attendance Days
     */
    private Long thisDays;

    /**
     * Last Month's Attendance Days
     */
    private Long lastDays;

}
