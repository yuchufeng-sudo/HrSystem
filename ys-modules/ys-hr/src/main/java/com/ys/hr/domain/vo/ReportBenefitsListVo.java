package com.ys.hr.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/21
 */
@Data
public class ReportBenefitsListVo {
    private String employeeName;
    private String deptName;
    private String country;
    private String email;
    private String position;
    private String benefitName;
    private Double benefitAmount;
    private String benefitFrequency;
    private Date startDate;
    private Date endDate;
}
