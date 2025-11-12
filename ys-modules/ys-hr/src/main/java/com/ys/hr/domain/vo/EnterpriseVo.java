package com.ys.hr.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author：hzz
 * @Date ：2025/6/23 13:55
 */
@Data
public class EnterpriseVo {
    private String id;
    private Long isPlan;

    private String planId;

    private String planOrderId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;

    private Long planTimeNums;
}
