package com.ys.hr.domain.vo;

import lombok.Data;

/**
 * @Author：马帅帅
 * @Date ：2025/7/3 19:03
 */
@Data
public class CandidateReportVo {
    private String candidateStatus;
    private Integer count;
    private String conversionRate;
    private String avgTime;
}
