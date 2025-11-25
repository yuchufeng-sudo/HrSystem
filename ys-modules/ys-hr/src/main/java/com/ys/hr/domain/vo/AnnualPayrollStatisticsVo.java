package com.ys.hr.domain.vo;

import com.ys.common.core.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：hzz
 * @Date ：2025/6/12 10:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualPayrollStatisticsVo {
    // salary CLASSIFICATION Name
    @Excel(name = "salary Sort Name")
    private String category;

    // 1-12 Month salary Summary
    @Excel(name = "January")
    private Double jan;
    @Excel(name = "February")
    private Double feb;
    @Excel(name = "March")
    private Double mar;
    @Excel(name = "April")
    private Double apr;
    @Excel(name = "May")
    private Double may;
    @Excel(name = "June")
    private Double jun;
    @Excel(name = "July")
    private Double jul;
    @Excel(name = "August")
    private Double aug;
    @Excel(name = "September")
    private Double sep;
    @Excel(name = "October")
    private Double oct;
    @Excel(name = "November")
    private Double nov;
    @Excel(name = "December")
    private Double dec;

    // Conversion rate (the value of this CLASSIFICATION in December / total annual pre-tax salary)
    @Excel(name = "Conversion Rate")
    private Double conversionRate;
}
