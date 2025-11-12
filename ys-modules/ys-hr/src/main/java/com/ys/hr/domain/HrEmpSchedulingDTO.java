package com.ys.hr.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class HrEmpSchedulingDTO extends HrEmpScheduling{
    private List<Integer> weekDayList;
    private List<String> monthDayOffList;
    private Map<String, DailyShift> dailyScheduleMap;
}
