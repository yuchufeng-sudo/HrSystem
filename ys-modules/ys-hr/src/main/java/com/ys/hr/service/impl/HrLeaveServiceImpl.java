package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.*;
import com.ys.hr.mapper.*;
import com.ys.hr.service.IHrLeaveService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Leave Application ServiceBusiness layer processing
 *
 * @author ys
 * @date 2025-05-21
 */
@Service
public class HrLeaveServiceImpl extends ServiceImpl<HrLeaveMapper, HrLeave> implements IHrLeaveService {

    @Resource
    private HrEmployeesMapper hrEmployeesMapper;

    @Resource
    private HrEmpSchedulingMapper hrEmpSchedulingMapper;

    @Resource
    private HrSchedulingEmpMapper hrSchedulingEmpMapper;

    @Resource
    private HrLeaveMapper hrLeaveMapper;

    @Resource
    private HrSettingsMapper hrSettingsMapper;

    @Resource
    private HrEmpHolidayMapper hrEmpHolidayMapper;

    @Resource
    private HrHolidayServiceImpl hrHolidayService;

    /**
     * Query Leave Application list
     *
     *
     * @param hrLeave Leave Application
     * @return Leave Application
     */
    @Override
    public List<HrLeave> selectHrLeaveList(HrLeave hrLeave) {
        return baseMapper.selectHrLeaveList(hrLeave);
    }

    @Override
    public HrLeave selectHrLeaveLastTime(Long userId, Date stateTime,String leaveType) {
        HrLeave leave = baseMapper.selectHrLeaveLastTime(userId);
        Long paidVacationDays = 0L;
        HrHoliday hrHoliday = new HrHoliday();
        hrHoliday.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        hrHoliday.setHolidayType(leaveType);
        List<HrHoliday> hrHolidays = hrHolidayService.selectHrHolidayList(hrHoliday);
        if(ObjectUtils.isNotEmpty(hrHolidays)){
            paidVacationDays = hrHolidays.get(0).getMaxDay();
            HrLeave hrLeave = new HrLeave();
            hrLeave.setLeaveType(leaveType);
            hrLeave.setUserId(userId);
            hrLeave.setLeaveStatus("1");
            List<HrLeave> hrLeaves = baseMapper.selectHrLeaveList(hrLeave);
            if(ObjectUtils.isNotEmpty(hrLeaves)){
                for (HrLeave leaf : hrLeaves) {
                    long days = calculateDaysBetween(leaf.getStateTime(), leaf.getEndTime());
                    paidVacationDays -= days;
                }
            }
        }
        if(ObjectUtils.isNotEmpty(leave)){
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//            String formattedDate = sdf.format(stateTime);
//            HrSchedulingEmp hrSchedulingEmp = new HrSchedulingEmp();
//            YearMonth ym = YearMonth.parse(formattedDate, DateTimeFormatter.ofPattern("yyyy-MM"));
//            hrSchedulingEmp.setSearchTime(ym.atDay(1).format(DateTimeFormatter.ISO_LOCAL_DATE));
//            hrSchedulingEmp.setUserId(userId);
//            List<HrSchedulingEmp> hrSchedulingEmps = hrSchedulingEmpMapper.selectHrSchedulingEmpByUserId(hrSchedulingEmp);
//            HrSchedulingEmp schedulingEmp = new HrSchedulingEmp();
//            if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrSchedulingEmps)) {
//                schedulingEmp = hrSchedulingEmps.get(0);
//            } else {
//                HrSettings hrSettings = hrSettingsMapper.selectHrSettingsByEid(SecurityUtils.getUserEnterpriseId());
//                HrEmpScheduling hrEmpScheduling = hrEmpSchedulingMapper
//                        .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
//                com.ys.common.core.utils.bean.BeanUtils.copyProperties(hrEmpScheduling, schedulingEmp);
//            }
//            paidVacationDays = schedulingEmp.getPaidVacationDays();
//            HrLeave leave1 = new HrLeave();
//            leave1.setUserId(userId);
//            String firstDay = getFirstDayOfMonth(formattedDate);
//            String lastDay = getLastDayOfMonth(formattedDate);
//            Map<String, Object> params1 = new HashMap<>();
//            params1.put("beginCreateTime", firstDay);
//            params1.put("endCreateTime", lastDay);
//            leave.setParams(params1);
//            List<HrLeave> hrLeaves = baseMapper.selectHrLeaveList(leave1);
//            if (hrLeaves.size() >= paidVacationDays) {
//                leave.setRemainingDays(0L);
//            } else {
//                paidVacationDays = paidVacationDays - hrLeaves.size();
//                leave.setRemainingDays(paidVacationDays);
//            }
            leave.setRemainingDays(paidVacationDays);
            return leave;
        }else{
            HrLeave leave1 = new HrLeave();
            leave1.setRemainingDays(paidVacationDays);
            return leave1;
        }
    }

    public static long calculateDaysBetween(Date lastStateTime, Date endTime) {
        if (lastStateTime == null || endTime == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }

        long diffInMillis = endTime.getTime() - lastStateTime.getTime();
        long days = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        return days+1;
    }

    @Override
    public int updateByLeaveId(HrLeave hrLeave) {
        return baseMapper.updateByLeaveId(hrLeave);
    }

    @Override
    public Map<String, Object> leaveCount(HrLeave hrLeave) {
        // Map<String,Object> map = baseMapper.leaveCount(hrLeave);
        HrLeave leave = new HrLeave();
        leave.setEnterpriseId(hrLeave.getEnterpriseId());
        // Long total = (Long) map.get("total");
        // Long approved = (Long) map.get("approved");
        // Long pending = (Long) map.get("pending");
        HashMap<String, Object> map = new HashMap<>();
        // Obtain the current date
        LocalDate today = LocalDate.now();
        // Last month data
        YearMonth lastYearMonth = YearMonth.from(today).minusMonths(1);
        LocalDate firstDayOfLastMonth = lastYearMonth.atDay(1);
        LocalDate lastDayOfLastMonth = lastYearMonth.atEndOfMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        HashMap<String, Object> timeMap = new HashMap<>();
        timeMap.put("beginCreateTime", firstDayOfLastMonth.format(formatter));
        timeMap.put("endCreateTime", lastDayOfLastMonth.format(formatter));
        leave.setParams(timeMap);
        Map<String, Object> LastMonthMap = baseMapper.leaveCount(leave);
        Long LastMonthTotal = (Long) LastMonthMap.get("total");
        Long LastMonthApproved = (Long) LastMonthMap.get("approved");
        Long LastMonthPending = (Long) LastMonthMap.get("pending");
        Long LastMonthRefuse = (Long) LastMonthMap.get("refuse");
        // OBTAIN this month's data
        YearMonth currentYearMonth = YearMonth.from(today);
        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth();
        timeMap.put("beginCreateTime", firstDayOfMonth.format(formatter));
        timeMap.put("endCreateTime", lastDayOfMonth.format(formatter));
        leave.setParams(timeMap);
        Map<String, Object> NowMonthMap = baseMapper.leaveCount(leave);
        Long NowTotal = (Long) NowMonthMap.get("total");
        Long NowApproved = (Long) NowMonthMap.get("approved");
        Long NowPending = (Long) NowMonthMap.get("pending");
        Long NowRefuse = (Long) NowMonthMap.get("refuse");
        map.put("NowTotal", NowTotal);
        map.put("NowApproved", NowApproved);
        map.put("NowPending", NowPending);
        map.put("NowRefuse", NowRefuse);
        map.put("LastMonthTotal", NowTotal - LastMonthTotal);
        map.put("LastMonthApproved", NowApproved - LastMonthApproved);
        map.put("LastMonthPending", NowPending - LastMonthPending);
        map.put("LastMonthRefuse", NowRefuse - LastMonthRefuse);
        if (NowTotal == 0) {
            String result = "0.00%";
            map.put("refuseRate", result);
        } else {
            BigDecimal rate = new BigDecimal(NowRefuse)
                    .multiply(new BigDecimal(100))
                    .divide(new BigDecimal(NowTotal), 4, RoundingMode.HALF_UP);
            String result = rate.setScale(2, RoundingMode.HALF_UP) + "%";
            map.put("refuseRate", result);
        }

        // double lastMonthRate = (LastMonthApproved.doubleValue() / LastMonthTotal) *
        // 100;
        // double currentMonthRate = (NowApproved.doubleValue() / NowTotal) * 100;
        // double rateChange = currentMonthRate - lastMonthRate;
        // double absoluteChange = Math.abs(rateChange);
        // String trend;
        // if (LastMonthTotal == 0 || NowTotal == 0) {
        // trend = (NowTotal > 0 && LastMonthTotal == 0) ? " " : " ";
        // absoluteChange = 0.0;
        // } else {
        // if (rateChange > 0) {
        // trend = "+";
        // } else if (rateChange < 0) {
        // trend = "-";
        // } else {
        // trend = "+";
        // }
        // }
        // DecimalFormat df = new DecimalFormat("#.##");
        // String formattedChange = df.format(absoluteChange);
        // map.put("MonthLeaveRate",trend+formattedChange);
        return map;
    }

    @Override
    public Long selectLeader(Long userId) {
        return baseMapper.selectLeader(userId);
    }

    @Override
   public Map leaveCountByUser(HrLeave hrLeave) {
        HashMap<Object, Object> hashMap = new HashMap<>();
        // Annual leave
        hrLeave.setLeaveType("1");
        hrLeave.setLeaveStatus("1");
        Map<String, Object> map = baseMapper.leaveCountByUser(hrLeave);
        hashMap.put("AnnualLeaveTotal", map.get("totaldays"));
        hashMap.put("AnnualLeaveTotalPending", map.get("pending"));
        // Sick leave
        hrLeave.setLeaveType("2");
        Map<String, Object> map2 = baseMapper.leaveCountByUser(hrLeave);
        hashMap.put("SickLeaveTotal", map2.get("totaldays"));
        hashMap.put("SickLeaveTotalPending", map2.get("pending"));
        // Personal leave
        hrLeave.setLeaveType("6");
        Map<String, Object> map3 = baseMapper.leaveCountByUser(hrLeave);
        hashMap.put("PersonalLeaveTotal", map3.get("totaldays"));
        hashMap.put("PersonalLeaveTotalPending", map3.get("pending"));
        List<HrHoliday> hrHolidays = baseMapper.selectLeaveDaysByEid(SecurityUtils.getUserEnterpriseId());
        hashMap.put("AnnualLeave", hrHolidays.stream()
                .filter(holiday -> "1".equals(holiday.getHolidayType()))
                .findFirst()  // 取第一个匹配项（如果有）
                .map(HrHoliday::getMaxDay)  // 如果存在，获取 maxDay
                .orElse(0L));  // 如果不存在，返回 0

        hashMap.put("SickLeave", hrHolidays.stream()
                .filter(holiday -> "2".equals(holiday.getHolidayType()))
                .findFirst()
                .map(HrHoliday::getMaxDay)
                .orElse(0L));

        hashMap.put("PersonalLeave", hrHolidays.stream()
                .filter(holiday -> "6".equals(holiday.getHolidayType()))
                .findFirst()
                .map(HrHoliday::getMaxDay)
                .orElse(0L));
        return hashMap;
    }
    /**
     * Query Employee Leave Application time
     *
     * @param userEnterpriseId
     * @return
     */
    @Override
   public Map<String, Object> selectLeaveTotal(String userEnterpriseId) {
        Character type = '1';
        // Query all employees
        List<HrEmployees> employeesList = hrEmployeesMapper.selectHrEmployeesListByBeforeHireDate(userEnterpriseId);
        // Query if it was a holiday yesterday
        Integer lastHolidayCount = hrEmpHolidayMapper.selectHolidayByLastDay(userEnterpriseId);
        // Query if it is a holiday today
        Integer thisHolidayCount = hrEmpHolidayMapper.selectHolidayByThisDay(userEnterpriseId);
        // Number of leave application days today
        AtomicInteger thisTotalCount = new AtomicInteger();
        // Number of leave application days yesterday
        AtomicInteger lastTotalCount = new AtomicInteger();
        // Obtain current date
        LocalDate today = LocalDate.now();
        // Date of last month
        LocalDate lastMonth = today.minusMonths(1);
        // Employee scheduling information
        employeesList.stream().forEach(emp -> {
            List<String> entryMonthList = getEntryMonthList(emp.getHireDate());
            entryMonthList.forEach(item -> {
                YearMonth localDate = YearMonth.parse(item);
                HrEmpScheduling hrEmpScheduling = null;
                // Query employee's scheduling situation
                HrSchedulingEmp schedulingEmp = hrSchedulingEmpMapper
                        .selectSchedulingEmpByUserIdAndDate(emp.getUserId(), item);
                if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(schedulingEmp)) {
                    // Employee scheduling
                    hrEmpScheduling = hrEmpSchedulingMapper
                            .selectHrEmpSchedulingBySchedulingId(schedulingEmp.getSchedulingId());
                } else {
                    HrSettings hrSettings = hrSettingsMapper.selectHrSettingsByEid(userEnterpriseId);
                    if(ObjectUtils.isNotEmpty(hrSettings)){
                        hrEmpScheduling = hrEmpSchedulingMapper
                                .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
                    }
                }
                if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrEmpScheduling)) {
                    if (type.equals(hrEmpScheduling.getSchedulingType())) {
                        // Monthly scheduling
                        if (Integer.valueOf(0).equals(lastHolidayCount)) {
                            // No holiday
                            // Query number of leave application days yesterday
                            Integer lastLeaveCount = hrLeaveMapper.selectLeaveByLastDay(emp.getUserId(),
                                    hrEmpScheduling.getDefaultStartTime(), hrEmpScheduling.getDefaultEndTime());
                            if (lastLeaveCount > 0) {
                                lastTotalCount.addAndGet(1);
                            }
                        }
                        if (Integer.valueOf(0).equals(thisHolidayCount)) {
                            // Query number of leave application days today
                            Integer thisLeaveCount = hrLeaveMapper.selectLeaveByThisDay(emp.getUserId(),
                                    hrEmpScheduling.getDefaultStartTime(), hrEmpScheduling.getDefaultEndTime());
                            if (thisLeaveCount > 0) {
                                thisTotalCount.addAndGet(1);
                            }
                        }
                    } else {
                        // Weekly scheduling
                        // Obtain working days
                        if (ObjectUtils.isNotEmpty(schedulingEmp) && StringUtils.isNotEmpty(schedulingEmp.getWeekDays())){
                            String weekDays = schedulingEmp.getWeekDays();
                            List<String> weeks = StringUtils.convertNumbersToWeek(weekDays);
                            // Yesterday
                            LocalDate yesterday = today.minusDays(1);
                            String todayToWeek = timeToWeek(today.getDayOfWeek());
                            String yesterdayToWeek = timeToWeek(yesterday.getDayOfWeek());
                            if (weeks.contains(todayToWeek)) {
                                // Query number of leave application days today
                                Integer thisLeaveCount = hrLeaveMapper.selectLeaveByThisDay(emp.getUserId(),
                                        hrEmpScheduling.getDefaultStartTime(), hrEmpScheduling.getDefaultEndTime());
                                if (Integer.valueOf(0).equals(thisLeaveCount)) {
                                    lastTotalCount.addAndGet(1);
                                }
                            }
                            if (weeks.contains(yesterdayToWeek)) {
                                // No holiday
                                // Query number of leave application days yesterday
                                Integer lastLeaveCount = hrLeaveMapper.selectLeaveByLastDay(emp.getUserId(),
                                        hrEmpScheduling.getDefaultStartTime(), hrEmpScheduling.getDefaultEndTime());
                                if (Integer.valueOf(0).equals(lastLeaveCount)) {
                                    thisTotalCount.addAndGet(1);
                                }
                            }
                        }
                    }
                }
            });
        });
        Map<String, Object> map = new HashMap<>();

        BigDecimal thisCount = BigDecimal.valueOf(thisTotalCount.get());
        BigDecimal lastCount = BigDecimal.valueOf(lastTotalCount.get());
        map.put("totalCount", thisCount);
        map.put("ratioType", thisCount.compareTo(lastCount) > 0 ? "1" : "2");
        if (lastCount.compareTo(BigDecimal.ZERO) == 0) {
            map.put("ratio", 100);
        } else {
            // (this - last) / last * 100
            BigDecimal diff = thisCount.subtract(lastCount);
            BigDecimal ratio = diff
                    .divide(lastCount, 1, RoundingMode.HALF_UP) // Calculate the ratio first, keep 1 decimal place
                    .multiply(BigDecimal.valueOf(100)); // Multiply by 100 to get the percentage
            map.put("ratio", ratio);
        }
        return map;
    }

    /**
 * Calculate the entry months
 *
 * @param hireDate
 * @return
 */
public static List<String> getEntryMonthList(Date hireDate) {
    List<String> result = new ArrayList<>();

    LocalDate hireLocalDate;
    if (hireDate instanceof java.sql.Date) {
        // java.sql.Date can directly use toLocalDate (no error)
        hireLocalDate = ((java.sql.Date) hireDate).toLocalDate();
    } else {
        // java.util.Date uses toInstant
        hireLocalDate = hireDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }

    YearMonth start = YearMonth.from(hireLocalDate);
    YearMonth end = YearMonth.from(LocalDate.now());

    while (!start.isAfter(end)) {
        result.add(start.toString()); // Format: yyyy-MM
        start = start.plusMonths(1);
    }

    System.out.println(result);
    return result;
}

private DayOfWeek numberToDayOfWeek(int number) {
    // ISO standard: DayOfWeek.MONDAY = 1, SUNDAY = 7
    if (number < 1 || number > 7) {
        throw new IllegalArgumentException("Working day number must be between 1-7");
    }
    return DayOfWeek.of(number);
}

private int calculateWorkdaysSimple(int[] weekdays, LocalDate startDate, LocalDate endDate) {
    // 1. Convert the working day array to a Set
    Set<DayOfWeek> workdaysSet = new HashSet<>();
    for (int day : weekdays) {
        workdaysSet.add(numberToDayOfWeek(day));
    }

    // 2. Traverse each day within the date range
    int workdayCount = 0;
    LocalDate current = startDate;

    // Ensure the start date is earlier than or equal to the end date
    while (!current.isAfter(endDate)) {
        if (workdaysSet.contains(current.getDayOfWeek())) {
            workdayCount++;
        }
        current = current.plusDays(1);
    }

    return workdayCount;
}

    private String timeToWeek(DayOfWeek dayOfWeek) {
        String[] dayNames = {
                "Monday", // index 0
                "Tues", // index 1
                "Wed", // index 2
                "Thur", // index 3
                "Friday", // index 4
                "Sat", // index 5
                "Sun" // index 6
        };
        // The value range of DayOfWeek is 1-7, where MONDAY=1 and SUNDAY=7.
        int index = dayOfWeek.getValue() - 1;
        return dayNames[index];
    }

    public static String getFirstDayOfMonth(String yearMonth) {
        YearMonth ym = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
        return ym.atDay(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    // Get the last day of the month.
    public static String getLastDayOfMonth(String yearMonth) {
        YearMonth ym = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
        return ym.atEndOfMonth().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
