package com.ys.hr.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/21
 */
@Data
public class ReportQueryVo {
    private String enterpriseId;
    private Long deptId;
    private Date startDate;
    private Date endDate;
}
