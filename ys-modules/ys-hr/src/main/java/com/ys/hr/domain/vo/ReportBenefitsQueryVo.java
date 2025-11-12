package com.ys.hr.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/21
 */
@Data
public class ReportBenefitsQueryVo extends ReportQueryVo{
    private Long userId;
    private String country;
}
