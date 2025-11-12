package com.ys.hr.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DailyTimeVo {

    private Long employeeId;

    private String fullName;

    private String avatarUrl;

    private String punchTime;

    private BigDecimal workingHours;

}
