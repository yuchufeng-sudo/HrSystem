package com.ys.hr.domain;

import lombok.Data;

import java.util.List;

@Data
public class DailyShift {
    private String shiftType;
    private List<String> workTime;
    private List<String> breakTime;
}
