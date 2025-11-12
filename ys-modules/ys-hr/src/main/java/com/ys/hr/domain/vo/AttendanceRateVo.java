package com.ys.hr.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AttendanceRateVo {

    /**
     * 
     */
    private String date;

    /**
     * 
     */
    private String month;

    /**
     *  
     */
    private Long attendanceCount;

    /**
     * 
     */
    private BigDecimal attendanceRate;

}
