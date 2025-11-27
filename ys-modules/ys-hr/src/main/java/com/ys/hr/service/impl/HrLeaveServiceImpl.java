package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.*;
import com.ys.hr.mapper.*;
import com.ys.hr.service.IHrLeaveService;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
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
    private HrSchedulingEmpMapper hrSchedulingEmpMapper;

    @Resource
    private HrLeaveMapper hrLeaveMapper;

    @Resource
    private HrSettingsMapper hrSettingsMapper;

    @Resource
    private HrEmpHolidayMapper hrEmpHolidayMapper;

    @Resource
    private HrHolidayServiceImpl hrHolidayService;

    @Resource
    private HrEmpSchedulingMapper hrEmpSchedulingMapper;

    @Resource
    private RemoteMessageService remoteMessageService;

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
    @Transactional
    public int updateByLeaveId(HrLeave hrLeave) {
        Long userId = SecurityUtils.getUserId();
        hrLeave.setUpdateBy(String.valueOf(userId));
        hrLeave.setUpdateTime(DateUtils.getNowDate());
        int i = baseMapper.updateByLeaveId(hrLeave);
        //1 同意 4 申请 name rejectReason
        if(i>0){
            HrLeave byId = getById(hrLeave.getLeaveId());
            if(ObjectUtils.isNotEmpty(byId)){
                HrEmployees leavePerson = hrEmployeesMapper.selectHrEmployeesByUserId(byId.getUserId());
                if("1".equals(hrLeave.getLeaveStatus())){
                    SysMessage sysMessage = new SysMessage();
                    sysMessage.setMessageRecipient(byId.getUserId());
                    sysMessage.setMessageStatus("0");
                    sysMessage.setMessageType(5);
                    sendMessage(byId, leavePerson, sysMessage);
                }else if("4".equals(hrLeave.getLeaveStatus())){
                    SysMessage sysMessage = new SysMessage();
                    sysMessage.setMessageRecipient(byId.getUserId());
                    sysMessage.setMessageStatus("0");
                    sysMessage.setMessageType(6);
                    sendMessage(byId, leavePerson, sysMessage);
                }
            }
        }
        return i;
    }

    private void sendMessage(HrLeave byId, HrEmployees leavePerson, SysMessage sysMessage) {
        sysMessage.setCreateTime(DateUtils.getNowDate());
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map1.put("name",leavePerson.getFullName());
        if(ObjectUtils.isNotEmpty(byId.getRejectReason())){
            map1.put("rejectReason",byId.getRejectReason());
        }else{
            map1.put("rejectReason","no reason");
        }
        sysMessage.setMap1(map1);
        sysMessage.setMap2(map2);
        remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
    }

    @Override
    @Transactional
    public Map<String, Object> leaveCount(HrLeave hrLeave) {
        HrLeave leave = new HrLeave();
        leave.setEnterpriseId(hrLeave.getEnterpriseId());
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
        // Obtain this month's data
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
                .findFirst()
                .map(HrHoliday::getMaxDay)
                .orElse(0L));

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

    @Override
    @Transactional
    public boolean save(HrLeave hrLeave){
        Long leader = selectLeader(SecurityUtils.getUserId());
        hrLeave.setManagerId(leader);
        hrLeave.setUserId(SecurityUtils.getUserId());
        hrLeave.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        hrLeave.setLeaveStatus("3");
        hrLeave.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        hrLeave.setCreateTime(DateUtils.getNowDate());
        HrHoliday hrHoliday = new HrHoliday();
        hrHoliday.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        hrHoliday.setHolidayType(hrLeave.getLeaveType());
        List<HrHoliday> hrHolidays = hrHolidayService.selectHrHolidayList(hrHoliday);
        if(ObjectUtils.isNotEmpty(hrHolidays)){
            hrLeave.setPaidLeave(hrHolidays.get(0).getPaidLeave());
        }else{
            hrLeave.setPaidLeave("2");
        }
        int i = baseMapper.insert(hrLeave);
        if(i>0){
            HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesById(leader);
            HrEmployees leavePerson = hrEmployeesMapper.selectHrEmployeesByUserId(SecurityUtils.getUserId());
            AjaxResult info = remoteMessageService.getInfo(SecurityUtils.getUserId(), SecurityConstants.INNER);
            Map<String,String> setting = (Map<String, String>) info.get("data");
            String leaveRequestNotification = setting.get("leaveRequestNotification");
            if(ObjectUtils.isNotEmpty(hrEmployees) && "1".equals(leaveRequestNotification)){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                SysMessage sysMessage = new SysMessage();
                sysMessage.setMessageRecipient(hrEmployees.getUserId());
                sysMessage.setMessageStatus("0");
                sysMessage.setMessageType(4);
                sysMessage.setCreateTime(DateUtils.getNowDate());
                Map<String, Object> map1 = new HashMap<>();
                Map<String, Object> map2 = new HashMap<>();
                Date stateTime = hrLeave.getStateTime();
                Date endTime = hrLeave.getEndTime();
                LocalDate stateTime2 = stateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endTime2 = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String state = stateTime2.format(formatter);
                String end = endTime2.format(formatter);
                Map<Integer, String> leaveTypeMap = new HashMap<>();
                leaveTypeMap.put(1, "Annual Leave");
                leaveTypeMap.put(2, "Sick Leave");
                leaveTypeMap.put(3, "Maternity Leave");
                leaveTypeMap.put(6, "Personal Leave");
                leaveTypeMap.put(5, "Paid Leave");
                map1.put("name",leavePerson.getFullName());
                map1.put("laeveType",leaveTypeMap.get(Integer.valueOf(hrLeave.getLeaveType())));
                map1.put("leaveTime",state + "~" + end);
                sysMessage.setMap1(map1);
                sysMessage.setMap2(map2);
                remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
            }
        }
        return i>0;
    }
}
