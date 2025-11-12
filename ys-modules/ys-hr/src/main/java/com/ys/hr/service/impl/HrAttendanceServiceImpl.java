package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.*;
import com.ys.hr.domain.vo.AttendanceRateVo;
import com.ys.hr.domain.vo.EmployeePresenCountVo;
import com.ys.hr.mapper.*;
import com.ys.utils.vo.HrAttendanceVo;
import com.ys.hr.service.IHrAttendanceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author ys
 * @date 2025-05-26
 */
@Service
public class HrAttendanceServiceImpl extends ServiceImpl<HrAttendanceMapper, HrAttendance>
        implements IHrAttendanceService {

    @Autowired
    private HrAttendanceMapper hrAttendanceMapper;

    @Autowired
    private HrLeaveMapper hrLeaveMapper;

    @Autowired
    private HrSettingsMapper hrSettingsMapper;

    @Autowired
    private HrEmployeesMapper hrEmployeesMapper;

    @Autowired
    private HrEmpHolidayMapper hrEmpHolidayMapper;

    @Autowired
    private HrEmpTimeMapper hrEmpTimeMapper;

    @Autowired
    private HrPayrollMapper hrPayrollMapper;

    @Autowired
    private HrEmpSchedulingMapper hrEmpSchedulingMapper;

    @Autowired
    private HrSchedulingEmpMapper hrSchedulingEmpMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * @param hrAttendance Attendance Record
     * @return Attendance Record
     */
    @Override
    public List<HrAttendance> selectHrAttendanceList(HrAttendance hrAttendance) {

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<HrAttendance> hrAttendances = new ArrayList<>();
        if ("1".equals(hrAttendance.getSearchValue())) {
            LocalDate sevenDaysAgo = today.minusDays(7);
            HashMap<String, Object> timeMap = new HashMap<>();
            timeMap.put("beginCreateTime", sevenDaysAgo.format(formatter));
            timeMap.put("endCreateTime", today.format(formatter));
            hrAttendance.setParams(timeMap);
            hrAttendances = baseMapper.selectHrAttendanceList(hrAttendance);
        } else if ("2".equals(hrAttendance.getSearchValue())) {
            // Data from 14 days ago
            LocalDate sevenDaysAgo = today.minusDays(14);
            HashMap<String, Object> timeMap = new HashMap<>();
            timeMap.put("beginCreateTime", sevenDaysAgo.format(formatter));
            timeMap.put("endCreateTime", today.format(formatter));
            hrAttendance.setParams(timeMap);
            hrAttendances = baseMapper.selectHrAttendanceList(hrAttendance);
        } else if ("3".equals(hrAttendance.getSearchValue())) {
            // Data from 7 days ago
            LocalDate sevenDaysAgo = today.minusDays(30);
            HashMap<String, Object> timeMap = new HashMap<>();
            timeMap.put("beginCreateTime", sevenDaysAgo.format(formatter));
            timeMap.put("endCreateTime", today.format(formatter));
            hrAttendance.setParams(timeMap);
            hrAttendances = baseMapper.selectHrAttendanceList(hrAttendance);
        } else {
            if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrAttendance.getAttendanceTime())) {
                Date attendanceTime = hrAttendance.getAttendanceTime();
                LocalDate localDate = attendanceTime.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                String format = localDate.format(formatter);
                hrAttendance.setSearchTime(format);
            }
            hrAttendances = baseMapper.selectHrAttendanceList(hrAttendance);
        }

        for (HrAttendance attendance : hrAttendances) {
            if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(attendance.getPresentStatus())) {
                String string = attendance.getPresentStatus();
                String content = string.substring(1, string.length() - 1);
                String[] array = content.split(",");
                StringBuilder temp = new StringBuilder();
                for (String s : array) {
                    s = s.trim();
                    String presentStatus = baseMapper.selectByPresentStatus(s);
                    temp.append(presentStatus).append(" ");
                }
                attendance.setPresentStatusString(temp.toString());
            }
            Date attendanceTime = attendance.getAttendanceTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(attendanceTime);
            attendance.setTime(formattedDate);
        }
        return hrAttendances;
    }

    @Override
    public String importData(List<HrAttendanceVo> HrAttendanceList, String eid) {
        // Query whether the corresponding employee exists, and if so, get the employee's hire date
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (HrAttendanceVo trAttendanceVo : HrAttendanceList) {
            if (StringUtils.isEmpty(trAttendanceVo.getJobnumber())) {
                failureNum++;
                String msg = "<br/>Failed to import the " + failureNum
                        + " message：The getJob Number cannot be empty!";
                failureMsg.append(msg);
                continue;
            }

            if (ObjectUtils.isEmpty(trAttendanceVo.getAttendanceTime())) {
                failureNum++;
                String msg = "<br/>Failed to import the " + failureNum
                        + " message：The attendance date cannot be empty!";
                failureMsg.append(msg);
                continue;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String yearMonthStr = dateFormat.format(trAttendanceVo.getAttendanceTime());
            try {
                ArrayList<Character> status = new ArrayList<>();
                HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByPhone(trAttendanceVo.getJobnumber(), SecurityUtils.getUserEnterpriseId());
                // Determine whether the employee exists
                if (ObjectUtils.isEmpty(hrEmployees)) {
                    failureNum++;
                    String msg = "<br/>Failed to import the " + failureNum
                            + " message：There is no information about this user at this time!";
                    failureMsg.append(msg);
                    continue;
                }

                // The clock-in time of part-time staff is required
                if ("2".equals(hrEmployees.getEmploymentType())) {
                    if (ObjectUtils.isEmpty(trAttendanceVo.getCheckIn())) {
                        failureNum++;
                        String msg = "<br/>Failed to import the " + failureNum
                                + " message：The check in cannot be empty!";
                        failureMsg.append(msg);
                        continue;
                    }

                    if (ObjectUtils.isEmpty(trAttendanceVo.getCheckOut())) {
                        failureNum++;
                        String msg = "<br/>Failed to import the " + failureNum
                                + " message：The check out cannot be empty!";
                        failureMsg.append(msg);
                        continue;
                    }
                }

                HrAttendance hrAttendance = new HrAttendance();
                hrAttendance.setEnterpriseId(hrEmployees.getEnterpriseId());
                hrAttendance.setUserId(hrEmployees.getUserId());
                hrAttendance.setNickName(hrEmployees.getFullName());
                hrAttendance.setAttendanceTime(trAttendanceVo.getAttendanceTime());

                // Query employee scheduling
                HrEmpScheduling hrEmpScheduling = null;
                HrSchedulingEmp schedulingEmp = hrSchedulingEmpMapper.selectSchedulingEmpByUserIdAndDate(hrEmployees.getUserId(),
                        yearMonthStr);
                // company scheduling
                HrSettings hrSettings = hrSettingsMapper.selectHrSettingsByEid(hrEmployees.getEnterpriseId());
                if (!ObjectUtils.isEmpty(schedulingEmp)) {
                    // Employee scheduling
                    hrEmpScheduling = hrEmpSchedulingMapper
                            .selectHrEmpSchedulingBySchedulingId(schedulingEmp.getSchedulingId());
                } else {
                    if (!ObjectUtils.isEmpty(hrSettings)) {
                        hrEmpScheduling = hrEmpSchedulingMapper
                                .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
                    }
                }

                if (!"2".equals(hrEmployees.getEmploymentType()) && ObjectUtils.isEmpty(hrEmpScheduling)) {
                    failureNum++;
                    String msg = "<br/>Failed to import the" + failureNum + "message：The Employee Shifts is None!!";
                    failureMsg.append(msg);
                    continue;
                }

                // Calculate total working hours (supports cross-day)
                double hours = calculateHoursBetween(hrEmpScheduling.getDefaultStartTime(), hrEmpScheduling.getDefaultEndTime());

                // Calculating rest time
                double RestHours = calculateHoursBetween(hrEmpScheduling.getRestStartTime(), hrEmpScheduling.getRestEndTime());

                // Working hours
                hours = hours - RestHours;
                // Round to one decimal place
                double roundedHours = Math.round(hours * 10) / 10.0;

                LocalTime workInTime = hrEmpScheduling.getDefaultStartTime();
                LocalTime workOutTime = hrEmpScheduling.getDefaultEndTime();

                // Set working hours
                if ("2".equals(hrEmployees.getEmploymentType())) {
                    hrAttendance.setWorkTime(ObjectUtils.isEmpty(schedulingEmp) ? "0" : String.valueOf(roundedHours));
                } else {
                    hrAttendance.setWorkTime(String.valueOf(roundedHours));
                }

                hrAttendance.setAttendanceStatus("1");

                if (!ObjectUtils.isEmpty(trAttendanceVo.getCheckIn())) {
                    LocalTime checkIn = convert(trAttendanceVo.getCheckIn());
                    hrAttendance.setCheckIn(checkIn);
                    String lateTime = "00:00:00";
                    boolean isLate = workInTime.isBefore(checkIn);
                    if (isLate) {
                        lateTime = calculateTimeDifference(workInTime, checkIn);
                        boolean isSpecialType = "2".equals(hrEmployees.getEmploymentType());
                        boolean hasSchedule = !ObjectUtils.isEmpty(schedulingEmp);

                        if (!isSpecialType || (isSpecialType && hasSchedule)) {
                            status.add('1');
                        } else {
                            lateTime = "00:00:00";
                        }
                    }
                    hrAttendance.setLateTime(lateTime);
                }

                if (!ObjectUtils.isEmpty(trAttendanceVo.getCheckOut())) {
                    LocalTime checkIn = convert(trAttendanceVo.getCheckIn());
                    LocalTime checkOut = convert(trAttendanceVo.getCheckOut());
                    hrAttendance.setCheckOut(checkOut);

                    double workedHours = calculateHoursBetween(checkIn, checkOut);
                    boolean isEmploymentType2 = "2".equals(hrEmployees.getEmploymentType());
                    boolean hasScheduling = !ObjectUtils.isEmpty(schedulingEmp);

                    if (isEmploymentType2 && hasScheduling || !isEmploymentType2) {
                        workedHours -= RestHours;
                    }
                    double myRoundedHours = Math.round(workedHours * 10) / 10.0;
                    hrAttendance.setMyWorkTime(String.valueOf(myRoundedHours));

                    // Calculate overtime
                    if (checkOut.isAfter(workOutTime)) {
                        long overtimeMinutes = Duration.between(workOutTime, checkOut).toMinutes();

                        if (!ObjectUtils.isEmpty(hrSettings) && "2".equals(hrSettings.getEnableOvertime())) {
                            hrAttendance.setOverTime("00:00:00");
                        } else {
                            boolean addStatus = false;
                            int maxOvertime = ObjectUtils.isEmpty(hrSettings) || ObjectUtils.isEmpty(hrSettings.getMaximumOvertime())
                                    ? -1 : hrSettings.getMaximumOvertime();

                            if (maxOvertime < 0 || maxOvertime < overtimeMinutes) {
                                if (maxOvertime >= 0) overtimeMinutes -= maxOvertime;
                                hrAttendance.setOverTime(String.format("%02d:%02d:00", overtimeMinutes / 60, overtimeMinutes % 60));
                                addStatus = true;
                            } else {
                                hrAttendance.setOverTime("00:00:00");
                            }

                            if (addStatus) status.add('3');
                        }
                    } else {
                        hrAttendance.setOverTime("00:00:00");
                    }

                    // Calculate early leave
                    if (checkOut.isBefore(workOutTime)) {
                        boolean addEarlyStatus = false;
                        long earlyMinutes = Duration.between(checkOut, workOutTime).toMinutes();
                        int gracePeriod = ObjectUtils.isEmpty(hrSettings) || ObjectUtils.isEmpty(hrSettings.getGracePeriod())
                                ? 0 : hrSettings.getGracePeriod();

                        if (!isEmploymentType2 || hasScheduling) {
                            if (earlyMinutes > gracePeriod) {
                                earlyMinutes -= gracePeriod;
                                addEarlyStatus = true;
                            } else {
                                earlyMinutes = 0;
                            }
                            hrAttendance.setEarlyTime(String.format("%02d:%02d:00", earlyMinutes / 60, earlyMinutes % 60));
                        } else {
                            hrAttendance.setEarlyTime("00:00:00");
                        }

                        if (addEarlyStatus) status.add('2');
                    } else {
                        hrAttendance.setEarlyTime("00:00:00");
                    }
                }


                int i = 0;
                HrAttendance attendance = new HrAttendance();
                attendance.setUserId(hrAttendance.getUserId());
                attendance.setAttendanceTime(trAttendanceVo.getAttendanceTime());
                HrAttendance hrAttendance1 = hrAttendanceMapper.selectHrAttendance(attendance);
                hrAttendance.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
                if (!status.isEmpty()) {
                    hrAttendance.setPresentStatus(status.toString());
                }
                if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrAttendance1)) {
                    hrAttendance.setUpdateTime(DateUtils.getNowDate());
                    hrAttendance.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
                    hrAttendance.setAttendanceId(hrAttendance1.getAttendanceId());
                    i = hrAttendanceMapper.updateByIdNew(hrAttendance);
                } else {
                    hrAttendance.setCreateTime(DateUtils.getNowDate());
                    i = baseMapper.insertHrAttendance(hrAttendance);
                }
                if (i > 0) {
                    successNum++;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                failureNum++;
                String msg = "<br/>Failed to import the" + failureNum
                        + "message：Please check if the data type is correct!";
                failureMsg.append(msg);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, failureNum + " records failed to import: ");
            return failureMsg.toString();
        } else {
            successMsg.insert(0, successNum + " records imported successfully。");
            return successMsg.toString();
        }
    }

    public static String convertMinutesToTimeFormat(int minutes) {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        int seconds = 0; // Assume no seconds

        // Use String.format to format the time
        return String.format("%02d:%02d:%02d", hours, remainingMinutes, seconds);
    }

    @Override
    public Map getCountTime(String userEnterpriseId) {
        HrAttendance hrAttendance = new HrAttendance();
        hrAttendance.setEnterpriseId(userEnterpriseId);
        Map<String, Object> totalMap = hrAttendanceMapper.getCountTime(hrAttendance);
        HrEmployees hrEmployees = new HrEmployees();
        hrEmployees.setEnterpriseId(userEnterpriseId);
        Map<String, Object> EtotalMap = hrEmployeesMapper.getCount(hrEmployees);
        Long total = (Long) EtotalMap.get("total");
        totalMap.put("total", total);
        Long Present = (Long) totalMap.get("present");
        Long Late = (Long) totalMap.get("late");
        Long Advance = (Long) totalMap.get("inAdvance");
        // Get current date
        LocalDate today = LocalDate.now();
        // Last month data
        YearMonth lastYearMonth = YearMonth.from(today).minusMonths(1);
        LocalDate firstDayOfLastMonth = lastYearMonth.atDay(1);
        LocalDate lastDayOfLastMonth = lastYearMonth.atEndOfMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        HashMap<String, Object> timeMap = new HashMap<>();
        timeMap.put("beginCreateTime", firstDayOfLastMonth.format(formatter));
        timeMap.put("endCreateTime", lastDayOfLastMonth.format(formatter));
        hrAttendance.setParams(timeMap);
        Map<String, Object> LastMonthMap = baseMapper.getCountTime(hrAttendance);
        hrEmployees.setParams(timeMap);
        Map<String, Object> LastMonthEtotalMap = hrEmployeesMapper.getCount(hrEmployees);
        Long lastMonthTotal = (Long) LastMonthEtotalMap.get("total");
        Long lastMonthPresent = (Long) LastMonthMap.get("present");
        Long lastMonthLate = (Long) LastMonthMap.get("late");
        Long lastMonthInAdvance = (Long) LastMonthMap.get("inadvance");
        // Get this month's data
        YearMonth currentYearMonth = YearMonth.from(today);
        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth();
        timeMap.put("beginCreateTime", firstDayOfMonth.format(formatter));
        timeMap.put("endCreateTime", lastDayOfMonth.format(formatter));
        hrAttendance.setParams(timeMap);
        Map<String, Object> NowMonthMap = baseMapper.getCountTime(hrAttendance);
        hrEmployees.setParams(timeMap);
        Map<String, Object> NowMonthEtotalMap = hrEmployeesMapper.getCount(hrEmployees);
        Long nowMonthTotal = (Long) NowMonthEtotalMap.get("total");
        Long nowMonthPresent = (Long) NowMonthMap.get("present");
        Long nowMonthLate = (Long) NowMonthMap.get("late");
        Long nowMonthInAdvance = (Long) NowMonthMap.get("inadvance");
        totalMap.put("totalRate", calculateGrowthRateWithSign(lastMonthTotal, nowMonthTotal));
        totalMap.put("presentRate", calculateGrowthRateWithSign(lastMonthPresent, nowMonthPresent));
        totalMap.put("lateRate", calculateGrowthRateWithSign(lastMonthLate, nowMonthLate));
        totalMap.put("inAdvanceRate", calculateGrowthRateWithSign(lastMonthInAdvance, nowMonthInAdvance));
        return totalMap;
    }

    @Override
    public int updateByIdNew(HrAttendance hrAttendance) {
        ArrayList<Character> status = new ArrayList<>();
        // Query employee scheduling
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String yearMonthStr = dateFormat.format(hrAttendance.getAttendanceTime());
        HrEmpScheduling hrEmpScheduling = null;
        HrSchedulingEmp schedulingEmp = hrSchedulingEmpMapper.selectSchedulingEmpByUserIdAndDate(hrAttendance.getUserId(),
                yearMonthStr);
        // company scheduling
        HrSettings hrSettings = hrSettingsMapper.selectHrSettingsByEid(hrAttendance.getEnterpriseId());
        if (!ObjectUtils.isEmpty(schedulingEmp)) {
            // Employee scheduling
            hrEmpScheduling = hrEmpSchedulingMapper
                    .selectHrEmpSchedulingBySchedulingId(schedulingEmp.getSchedulingId());
        } else {
            if (!ObjectUtils.isEmpty(hrSettings)) {
                hrEmpScheduling = hrEmpSchedulingMapper
                        .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
            }
        }
        double RestHours = calculateHoursBetween(hrEmpScheduling.getRestStartTime(), hrEmpScheduling.getRestEndTime());
        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrAttendance.getCheckIn())) {
            LocalTime chenkIn = hrAttendance.getCheckIn();
            // 1 is before 2, 1: late
            if (hrEmpScheduling.getDefaultStartTime().isBefore(chenkIn)) {
                String late = calculateTimeDifference(hrEmpScheduling.getDefaultStartTime(), chenkIn);
                hrAttendance.setLateTime(late);
                status.add('1');
            }
            // Arrive early 5
            if (chenkIn.isBefore(hrEmpScheduling.getDefaultStartTime())) {
                String early = calculateTimeDifference(hrEmpScheduling.getDefaultStartTime(), chenkIn);
                hrAttendance.setInAdvanceTime(early);
            }

        }

        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrAttendance.getCheckOut())) {
            LocalTime chenkOut = hrAttendance.getCheckOut();
            // 1 is before 2, overtime 3:
            if (hrSettings.getDefaultEndTime().isBefore(chenkOut)) {
                String overTime = calculateTimeDifference(hrSettings.getDefaultEndTime(), chenkOut);
                hrAttendance.setOverTime(overTime);
                status.add('3');
            }
            // Leave early 4
            if (chenkOut.isBefore(hrSettings.getDefaultEndTime())) {
                String earlyTime = calculateTimeDifference(hrSettings.getDefaultEndTime(), chenkOut);
                hrAttendance.setEarlyTime(earlyTime);
                status.add('2');
            }
        }
        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrAttendance.getCheckOut())
                && org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrAttendance.getCheckIn())) {
            // Calculate the time difference (supports cross-day)
            double Myhours = calculateHoursBetween(hrAttendance.getCheckIn(), hrAttendance.getCheckOut());
            Myhours = Myhours - RestHours;
            // Round to one decimal place
            double MyroundedHours = Math.round(Myhours * 10) / 10.0;
            hrAttendance.setMyWorkTime(String.valueOf(MyroundedHours));
        }
        hrAttendance.setPresentStatus(status.toString());
        return hrAttendanceMapper.updateByIdNew(hrAttendance);
    }

    @Override
    public Map getCountByUserId(Long userId) {
        LocalDate today = LocalDate.now();
        YearMonth lastMonth = YearMonth.from(today).minusMonths(1);
        LocalDate firstDay = null;
        LocalDate lastDay = lastMonth.atEndOfMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM");
        HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByUserId(userId);
        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrEmployees) && org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrEmployees.getHireDate())) {
            Date hireDate = hrEmployees.getHireDate();
            // 把 java.util.Date 转换成 LocalDate
            LocalDate hireLocalDate = hireDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if (YearMonth.from(hireLocalDate).equals(lastMonth)) {
                firstDay = hireLocalDate;
            }else{
                firstDay = lastMonth.atDay(1);
            }
        } else {
            firstDay = lastMonth.atDay(1);
        }
        String yearMonthStr = firstDay.format(formatter2);
        String startDate = firstDay.format(formatter);
        String endDate = lastDay.format(formatter);

        // Present, Paid Leave, Absent, Leave
        HashMap<String, Object> map = new HashMap<>();
        //Present
        HrAttendance attendance = new HrAttendance();
        attendance.setUserId(userId);
        attendance.setStartDate(startDate);
        attendance.setEndDate(endDate);
        Map<String, Object> totalMap = hrAttendanceMapper.getCountByUserId(attendance);
        Long total = (Long) totalMap.get("total");


        //Leave
        HrLeave leave = new HrLeave();
        leave.setUserId(userId);
        Map<String, Object> leaveMap = hrLeaveMapper.leaveCountByUser(leave);
        Long leaveTotal = (Long) leaveMap.get("approved");
        Long holidayTotal = (Long) leaveMap.get("paid");

        // Number of presences
        map.put("total", total);
        // Number of paid holidays
        map.put("holidayTotal", holidayTotal);
        // Number of leaves
        map.put("LeaveTotal", leaveTotal);

        // Query employee scheduling
        HrEmpScheduling hrEmpScheduling = null;
        HrSchedulingEmp schedulingEmp = hrSchedulingEmpMapper.selectSchedulingEmpByUserIdAndDate(SecurityUtils.getUserId(),
                yearMonthStr);
        // company scheduling
        HrSettings hrSettings = hrSettingsMapper.selectHrSettingsByEid(SecurityUtils.getUserEnterpriseId());
        if (!ObjectUtils.isEmpty(schedulingEmp)) {
            // Employee scheduling
            hrEmpScheduling = hrEmpSchedulingMapper
                    .selectHrEmpSchedulingBySchedulingId(schedulingEmp.getSchedulingId());
        } else {
            if (!ObjectUtils.isEmpty(hrSettings)) {
                hrEmpScheduling = hrEmpSchedulingMapper
                        .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
            }
        }
        if (ObjectUtils.isEmpty(hrEmpScheduling)) {
            map.put("Absent", 0);
        } else {
            //Absent
            HrAttendance hrAttendance = new HrAttendance();
            hrAttendance.setUserId(userId);
            hrAttendance.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
            hrAttendance.setStartDate(startDate);
            hrAttendance.setEndDate(endDate);
            ArrayList<HrAttendance> hrAttendances = hrAttendanceMapper.selectHrAttendanceListByUser(hrAttendance);
            // Absenteeism
            if (hrAttendances.isEmpty()) {
                map.put("Absent", 0);
            } else {
                if (hrEmpScheduling.getSchedulingType().equals('2')) {
                    String weekDays = hrEmpScheduling.getWeekDays();
                    List<Integer> weekdays = Arrays.stream(weekDays.substring(1, weekDays.length() - 1).split(","))
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                    int i = 0;
                    for (HrAttendance hrAttendance1 : hrAttendances) {
                        String currentDate = hrAttendance1.getAbsentDate();
                        LocalDate localDate = LocalDate.parse(currentDate);
                        if (weekdays.contains(localDate.getDayOfWeek().getValue())) {
                            i++;
                        }
                    }
                    map.put("Absent", i);
                }
            }
        }

        return map;
    }

    @Override
    public List<HrEmployees> selectHrAttendanceListByHr(HrAttendance hrAttendance) {
        List<HrEmployees> hrEmployeesList = hrEmployeesMapper
                .selectHrEmployeesListByEid(hrAttendance.getEnterpriseId());
        if (ObjectUtils.isEmpty(hrAttendance.getSearchTime())) {
            // Get current year and month
            hrAttendance.setSearchTime(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
        }
        // Query the number of absences
        String firstDay = getFirstDayOfMonth(hrAttendance.getSearchTime());
        String lastDay = getLastDayOfMonth(hrAttendance.getSearchTime());
        HrAttendance attendance1 = new HrAttendance();
        attendance1.setStartDate(firstDay);
        attendance1.setEndDate(lastDay);
        for (HrEmployees hrEmployees : hrEmployeesList) {
            attendance1.setUserId(hrEmployees.getUserId());
            ArrayList<HrAttendance> hrAttendances = hrAttendanceMapper.selectHrAttendanceListByUser(attendance1);
            HrSchedulingEmp hrSchedulingEmp = new HrSchedulingEmp();
            hrSchedulingEmp.setSearchTime(firstDay.toString());
            hrSchedulingEmp.setUserId(hrEmployees.getUserId());
            List<HrSchedulingEmp> hrSchedulingEmps = hrSchedulingEmpMapper
                    .selectHrSchedulingEmpByUserId(hrSchedulingEmp);
            HrSchedulingEmp schedulingEmp = new HrSchedulingEmp();
            if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrSchedulingEmps)) {
                schedulingEmp = hrSchedulingEmps.get(0);
            } else {
                HrSettings hrSettings = hrSettingsMapper.selectHrSettingsByEid(SecurityUtils.getUserEnterpriseId());
                if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrSettings)) {
                    HrEmpScheduling hrEmpScheduling = hrEmpSchedulingMapper
                            .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
                    if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrEmpScheduling)) {
                        com.ys.common.core.utils.bean.BeanUtils.copyProperties(hrEmpScheduling, schedulingEmp);
                    }
                }
            }
            Character i = '1';
            if (!ObjectUtils.isEmpty(schedulingEmp)) {
                if (i.equals(schedulingEmp.getSchedulingType())) {
                    String weekDays = schedulingEmp.getWeekDays();
                    List<Integer> weekdays = Arrays.stream(weekDays.substring(1, weekDays.length() - 1).split(","))
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                    hrAttendances = (ArrayList<HrAttendance>) hrAttendances.stream()
                            .filter(a -> {
                                LocalDate date = LocalDate.parse(a.getAbsentDate());
                                Integer dayOfWeek = Integer.valueOf(date.getDayOfWeek().getValue());
                                return weekdays.contains(dayOfWeek);
                            })
                            .collect(Collectors.toList());
                    hrEmployees.setAbsentDays((long) hrAttendances.size());
                } else {
                    hrEmployees.setAbsentDays((long) hrAttendances.size());
                }
            }
        }
        for (HrEmployees hrEmployees : hrEmployeesList) {
            HrAttendance hrAttendance1 = new HrAttendance();
            hrAttendance1.setUserId(hrEmployees.getUserId());
            hrAttendance1.setStartDate(firstDay);
            hrAttendance1.setEndDate(lastDay);
            Map<String, Object> totalMap = hrAttendanceMapper.getCountByUserId(hrAttendance1);
            hrEmployees.setPresent((Long) totalMap.get("total"));
            hrEmployees.setLate((Long) totalMap.get("late"));
            Long presentDays = hrEmployees.getPresent(); // Number of presences
            Long absentDays = hrEmployees.getAbsentDays(); // Number of absences
            Long totalDays = presentDays + absentDays; // Total number of days expected to be present
            double attendanceRate = 0.0;
            if (totalDays > 0) {
                attendanceRate = (presentDays * 100.0) / totalDays; // Calculate the percentage
            }
            String attendanceRateStr = String.format("%.2f%%", attendanceRate);
            hrEmployees.setAttendanceRate(attendanceRateStr); // Set as a string type field
        }
        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrAttendance.getSearchValue())) {
            hrEmployeesList = hrEmployeesList.stream()
                    .filter(hrEmployees -> hrEmployees.getUsername().contains(hrAttendance.getSearchValue()))
                    .collect(Collectors.toList());
        }
        return hrEmployeesList;
    }

    // Get the first day of the month
    public static String getFirstDayOfMonth(String yearMonth) {
        YearMonth ym = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
        return ym.atDay(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    // Get the last day of the month
    public static String getLastDayOfMonth(String yearMonth) {
        YearMonth ym = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
        return ym.atEndOfMonth().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static LocalDate getFirstDayOfMonth2(String yearMonth) {
        return YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"))
                .atDay(1); // Directly return the LocalDate of the first day of the month
    }

    /**
     * Get the last day of the month corresponding to the specified year and month string
     */
    public static LocalDate getLastDayOfMonth2(String yearMonth) {
        return YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"))
                .atEndOfMonth(); // Directly return the LocalDate of the last day of the month
    }

    @Override
    public TableDataInfo selectHrAttendanceListByUser(HrAttendance hrAttendance) {
        String yearMonthStr = null;
        if (ObjectUtils.isEmpty(hrAttendance.getSearchTime())) {
            // Get current year and month
            hrAttendance.setSearchTime(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
            // Get current time
            LocalDate today = LocalDate.now();
            // Format as "year-month"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            yearMonthStr = today.format(formatter);
        } else {
            yearMonthStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }
        // Query the number of absences
        String firstDay = getFirstDayOfMonth(hrAttendance.getSearchTime());
        String lastDay = getLastDayOfMonth(hrAttendance.getSearchTime());


        // Query employee scheduling
        HrEmpScheduling hrEmpScheduling = null;
        HrSchedulingEmp schedulingEmp = hrSchedulingEmpMapper.selectSchedulingEmpByUserIdAndDate(hrAttendance.getUserId(),
                yearMonthStr);
        // company scheduling
        HrSettings hrSettings = hrSettingsMapper.selectHrSettingsByEid(hrAttendance.getEnterpriseId());
        if (!ObjectUtils.isEmpty(schedulingEmp)) {
            // Employee scheduling
            hrEmpScheduling = hrEmpSchedulingMapper
                    .selectHrEmpSchedulingBySchedulingId(schedulingEmp.getSchedulingId());
        } else {
            if (!ObjectUtils.isEmpty(hrSettings)) {
                hrEmpScheduling = hrEmpSchedulingMapper
                        .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
            }
        }


        HrEmpHoliday hrEmpHoliday = new HrEmpHoliday();
        hrEmpHoliday.setEnterpriseId(hrAttendance.getEnterpriseId());
        Map<String, Object> params1 = hrAttendance.getParams();
        params1.put("beginCreateTime", firstDay);
        params1.put("endCreateTime", lastDay);
        hrEmpHoliday.setParams(params1);
        // Company holiday situation
        List<HrEmpHoliday> hrEmpHolidays = hrEmpHolidayMapper.selectHrEmpHolidayList(hrEmpHoliday);
        // Personal leave situation
        HrLeave leave = new HrLeave();
        leave.setUserId(hrAttendance.getUserId());
        leave.setParams(params1);
        List<HrLeave> hrLeaves = hrLeaveMapper.selectHrLeaveList(leave);
        // Personal employment situation
        HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByUserId(hrAttendance.getUserId());
        // Personal attendance record situation
        HrAttendance attendance = new HrAttendance();
        attendance.setUserId(hrAttendance.getUserId());
        attendance.setParams(params1);
        List<HrAttendance> hrAttendanceList = hrAttendanceMapper.selectHrAttendanceList(attendance);
        // 1. Process company holiday dates
        Set<LocalDate> companyHolidays = new HashSet<>();
        for (HrEmpHoliday holiday : hrEmpHolidays) {
            LocalDate start = convertToLocalDate(holiday.getStateTime());
            LocalDate end = convertToLocalDate(holiday.getEndTime());
            LocalDate current = start;
            while (!current.isAfter(end)) {
                companyHolidays.add(current);
                current = current.plusDays(1);
            }
        }

        // 2. Process User leave dates (assuming HrLeave contains startDate and endDate)
        Set<LocalDate> leaveDates = new HashSet<>();
        for (HrLeave hrLeave : hrLeaves) {
            LocalDate start = convertToLocalDate(hrLeave.getStateTime());
            LocalDate end = convertToLocalDate(hrLeave.getEndTime());
            LocalDate current = start;
            while (!current.isAfter(end)) {
                leaveDates.add(current);
                current = current.plusDays(1);
            }
        }
// 3. Process Attendance Record dates
        Set<LocalDate> attendanceDates = hrAttendanceList.stream()
                .map(att -> convertToLocalDate(att.getAttendanceTime()))
                .collect(Collectors.toSet());

//absent datas
        HrAttendance hrAttendance2 = new HrAttendance();
        hrAttendance2.setUserId(SecurityUtils.getUserId());
        hrAttendance2.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        hrAttendance.setStartDate(firstDay);
        hrAttendance.setEndDate(lastDay);
        ArrayList<HrAttendance> hrAttendances = hrAttendanceMapper.selectHrAttendanceListByUser(hrAttendance2);

// 4. Get hire date and current date
// LocalDate hireDate = convertToLocalDate(hrEmployees.getHireDate());
// LocalDate today = LocalDate.now();
        LocalDate firstDay2 = getFirstDayOfMonth2(hrAttendance.getSearchTime());
        LocalDate lastDay2 = getLastDayOfMonth2(hrAttendance.getSearchTime());
// 5. Traverse each day to generate Attendance Record
        List<HrEmployees> records = new ArrayList<>();
        LocalDate currentDate = firstDay2;
        while (!currentDate.isAfter(lastDay2)) {
            HrEmployees employees = new HrEmployees();
            if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrEmployees)) {
                employees.setAvatarUrl(hrEmployees.getAvatarUrl());
                employees.setUsername(hrEmployees.getUsername());
                employees.setDeptName(hrEmployees.getDeptName());
                employees.setEmail(hrEmployees.getEmail());
                employees.setDataTime(currentDate);
            }
            boolean hasAttendance = attendanceDates.contains(currentDate);
            boolean isHoliday = companyHolidays.contains(currentDate);
            boolean isOnLeave = leaveDates.contains(currentDate);
            if (hasAttendance) {
                employees.setPresentStatus("1");
                employees.setStatusName("Present");
            } else if (isHoliday) {
                employees.setHolidayStatus("1");
                employees.setStatusName("OnHoliday");
            } else if (isOnLeave) {
                employees.setLeaveStatus("1");
                employees.setStatusName("OnLeave");
            } else {
                List<Integer> weekdays = new ArrayList<>();
                if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrEmpScheduling) && hrEmpScheduling.getSchedulingType().equals('2')) {
                    String weekDays = hrEmpScheduling.getWeekDays();
                    weekdays = Arrays.stream(weekDays.substring(1, weekDays.length() - 1).split(","))
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                    boolean isAbsent = false;
                    if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrAttendances)) {
                        for (HrAttendance hrAttendance1 : hrAttendances) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            String formattedDate = currentDate.format(formatter);
                            if (hrAttendance1.getAbsentDate().equals(formattedDate)) {
                                isAbsent = true;
                            }
                        }
                    }
                    if (!weekdays.contains(currentDate.getDayOfWeek().getValue())) {
                        employees.setHolidayStatus("1");
                        employees.setStatusName("OnHoliday");
                    } else {
                        if (isAbsent) {
                            employees.setPresentStatus("2");
                            employees.setStatusName("Absent");
                        }
                    }
                }
            }
            records.add(employees);
            currentDate = currentDate.plusDays(1);
        }
        Map<String, Object> params = hrAttendance.getParams();
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int pageNum = Integer.parseInt(params.get("pageNum").toString());
        int total = records.size();
        // 6. Calculate the pagination interception range
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<HrEmployees> pageData = records.subList(fromIndex, toIndex);
        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrAttendance.getAttendanceTime())) {
            Date attendanceTime = hrAttendance.getAttendanceTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(attendanceTime);
            pageData = pageData.stream()
                    .filter(HrEmployees -> HrEmployees.getDataTime().toString().equals(formattedDate))
                    .collect(Collectors.toList());
        }
        TableDataInfo rspData = new TableDataInfo();
        rspData.setRows(pageData);
        rspData.setMsg("Query successful");
        rspData.setTotal(total);
        return rspData;
    }

    // Weekly Summary
    @Override
    public List<Map<String, Object>> getCountByWeekly(HrAttendance hrAttendance) {
        LocalDate today = LocalDate.now();
        // Get current year and month
        YearMonth currentYearMonth = YearMonth.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Map<String, Object>> monthlyDataList = new ArrayList<>(12);

        // Use SHORT format to get month abbreviation and specify Locale as US to ensure
        // English
        DateTimeFormatter monthAbbrevFormatter = DateTimeFormatter.ofPattern("MMM", Locale.US);

        // Query data for the most recent 12 months in reverse order (including this
        // month)
        for (int i = 0; i < 12; i++) {
            YearMonth targetMonth = currentYearMonth.minusMonths(i);
            // Generate the first and last days of the current month
            LocalDate firstDay = targetMonth.atDay(1);
            LocalDate lastDay = targetMonth.atEndOfMonth();
            // Build query parameters
            Map<String, Object> params = new HashMap<>();
            params.put("beginCreateTime", firstDay.format(formatter));
            params.put("endCreateTime", lastDay.format(formatter));
            hrAttendance.setParams(params);
            // Execute the query
            Map<String, Object> monthlyData = hrEmployeesMapper.getCountByWeekly(hrAttendance);
            // Add month abbreviation (e.g., Jan, Feb)
            String monthAbbrev = targetMonth.format(monthAbbrevFormatter);
            monthlyData.put("month", monthAbbrev);
            monthlyDataList.add(monthlyData);
        }

        // Reverse the list to sort in natural order (oldest to newest)
        Collections.reverse(monthlyDataList);
        return monthlyDataList;
    }

    @Override
    public Map<String, Object> getCountByWeeklyRate(HrAttendance hrAttendance) {
        HrEmployees hrEmployees = new HrEmployees();
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        // 1. Get total number of employees and current month data
        Map<String, Object> count = hrEmployeesMapper.getCount(hrEmployees);
        Long total = count.get("total") != null ? (Long) count.get("total") : 0L;
        Map<String, Object> monthlyData = hrEmployeesMapper.getCountByWeekly(hrAttendance);
        Long fullTime = monthlyData.get("fulltime") != null ? (Long) monthlyData.get("fulltime") : 0L;
        Long partTime = monthlyData.get("parttime") != null ? (Long) monthlyData.get("parttime") : 0L;
        // 2. Calculate the percentage.
        BigDecimal fullTimePercent, partTimePercent;
        if (total != 0) {
            // Use BigDecimal to ensure precision
            fullTimePercent = BigDecimal.valueOf(fullTime)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);

            partTimePercent = BigDecimal.valueOf(partTime)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
        } else {
            fullTimePercent = BigDecimal.ZERO;
            partTimePercent = BigDecimal.ZERO;
        }
        monthlyData.put("fullRate", fullTimePercent + "%");
        monthlyData.put("partRate", partTimePercent + "%");
        return monthlyData;
    }

    @Override
    public ArrayList<HrEmpPayrollDetail> getPayrollsByEmp(HrAttendance hrAttendance) {
        ArrayList<HrEmpPayrollDetail> hrEmpPayrollDetails = new ArrayList<>();
        LocalDate today = LocalDate.now();
        // Current month working days. User queries the time summary for each month from
        // employment to now.
        // Query the user's employment date
        List<Map<String, String>> dateRanges = new ArrayList<>();
        HrEmployees hrEmployees = hrEmployeesMapper
                .selectHrEmployeesByUserId(hrAttendance.getUserId());
        if (ObjectUtils.isEmpty(hrEmployees)) {
            return hrEmpPayrollDetails;
        }
//        for (HrEmployees hrEmployees : hrEmployeesList) {
        Date hireDate = hrEmployees.getHireDate();
        LocalDate localHireDate = hireDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        YearMonth startMonth = YearMonth.from(localHireDate);
        YearMonth endMonth = YearMonth.from(today);
        // Loop from the employment month to the current month
        for (YearMonth currentMonth = startMonth; !currentMonth.isAfter(endMonth); currentMonth = currentMonth
                .plusMonths(1)) {

            LocalDate rangeStart, rangeEnd;

            if (currentMonth.equals(startMonth)) {
                // Employment month: start from the actual employment date
                rangeStart = localHireDate;
                // If it is the current month, the end date is today; otherwise, it is the last
                // day of the month
                rangeEnd = currentMonth.equals(endMonth) ? today : currentMonth.atEndOfMonth();
            } else if (currentMonth.equals(endMonth)) {
                // Current month: end today
                rangeStart = currentMonth.atDay(1);
                rangeEnd = today;
            } else {
                // Complete month: from the 1st to the last day
                rangeStart = currentMonth.atDay(1);
                rangeEnd = currentMonth.atEndOfMonth();
            }
            // Query user scheduling and enterprise scheduling. First check if there is a
            // schedule for the current month.
            // If not, use the enterprise schedule; otherwise, skip directly.
            HrSchedulingEmp hrSchedulingEmp = new HrSchedulingEmp();
            hrSchedulingEmp.setUserId(hrEmployees.getUserId());
            hrSchedulingEmp.setSearchTime(rangeStart.withDayOfMonth(1).toString());
            HrSchedulingEmp schedulingEmp2 = hrSchedulingEmpMapper
                    .selectHrSchedulingEmpByDataAndUserId(hrSchedulingEmp);
            HrSettings hrSettings = hrSettingsMapper.selectHrSettingsByEid(SecurityUtils.getUserEnterpriseId());

            // Scheduling
            HrSchedulingEmp schedulingEmp = new HrSchedulingEmp();
            if (schedulingEmp2 != null) {
                schedulingEmp = schedulingEmp2;
            } else if (hrSettings != null) {
                HrEmpScheduling hrEmpScheduling = hrEmpSchedulingMapper
                        .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
                if (ObjectUtils.isEmpty(hrEmpScheduling)) {
                    continue;
                }
                com.ys.common.core.utils.bean.BeanUtils.copyProperties(hrEmpScheduling, schedulingEmp);
            } else {
                continue;
            }
            // Expected working hours for the day
            double hours = calculateHoursBetween(schedulingEmp.getDefaultStartTime(),
                    schedulingEmp.getDefaultEndTime());
            double RestHours = calculateHoursBetween(schedulingEmp.getRestStartTime(),
                    schedulingEmp.getRestEndTime());
            hours = hours - RestHours;
            // Round to one decimal place
            double workerTime = Math.round(hours * 10) / 10.0;
            // Expected working days
            long matchDay = 0;
            // Set whether the user scheduling is monthly or weekly. 1: Monthly 2: Weekly
            if (schedulingEmp.getSchedulingType().equals('1')) {
                String monthDaysOff = schedulingEmp.getMonthDaysOff();
                matchDay = daysBetweenInclusive(rangeStart.withDayOfMonth(1),
                        rangeStart.withDayOfMonth(rangeStart.lengthOfMonth())) - Integer.parseInt(monthDaysOff);
            } else {
                String weekDays = schedulingEmp.getWeekDays();
                int[] weekdays = Arrays.stream(weekDays.substring(1, weekDays.length() - 1).split(","))
                        .map(String::trim)
                        .mapToInt(Integer::parseInt)
                        .toArray();
                // matchDay = calculateWorkdaysSimple(weekdays, rangeStart, rangeEnd);
                matchDay = calculateWorkdaysSimple(weekdays, rangeStart.withDayOfMonth(1),
                        rangeStart.withDayOfMonth(rangeStart.lengthOfMonth()));
            }
            // matchDay = matchDay - schedulingEmp.getPaidVacationDays();
            HrEmpPayrollDetail hrEmpPayrollDetail = new HrEmpPayrollDetail();
            HrEmpTime hrEmpTime = new HrEmpTime();
            hrEmpTime.setUserId(hrEmployees.getUserId());
            hrEmpTime.setSearchTime(rangeStart.withDayOfMonth(1).toString());
            HrEmpTime timeByUserId = hrEmpTimeMapper.getTimeByUserId(hrEmpTime);
            if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(timeByUserId)) {
                BeanUtils.copyProperties(timeByUserId, hrEmpPayrollDetail);
                hrEmpPayrollDetail.setMouthDays(Long.valueOf(timeByUserId.getMouthDays()));
                Date payrollData = timeByUserId.getPayrollData();
                hrEmpPayrollDetail.setPayrollData(new SimpleDateFormat("yyyy-MM").format(payrollData));
                HrPayroll hrPayroll = new HrPayroll();
                hrPayroll.setUserId(hrEmployees.getUserId());
                hrPayroll.setSearchTime(rangeStart.withDayOfMonth(1).toString());
                HrPayroll payRollByUserId = hrPayrollMapper.getPayRollByUserId(hrPayroll);
                if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(payRollByUserId)) {
                    hrEmpPayrollDetail.setPayrollStatus(payRollByUserId.getPayrollStatus());
                } else {
                    hrEmpPayrollDetail.setPayrollStatus('0');
                }
                hrEmpPayrollDetails.add(hrEmpPayrollDetail);
                continue;
            }
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM");
            String format = rangeStart.format(formatter1);
            hrEmpPayrollDetail.setPayrollData(format);
            // Format as a string
            Map<String, String> range = new HashMap<>();
            String start = rangeStart.format(DATE_FORMATTER);
            String end = rangeEnd.format(DATE_FORMATTER);
            dateRanges.add(range);
            // Holiday days
            HrEmpHoliday hrEmpHoliday = new HrEmpHoliday();
            Map<String, Object> params = new HashMap<>();
            params.put("beginCreateTime", start);
            params.put("endCreateTime", end);
            hrEmpHoliday.setParams(params);
            hrEmpHoliday.setEnterpriseId(hrAttendance.getEnterpriseId());
            List<HrEmpHoliday> hrEmpHolidays = hrEmpHolidayMapper.selectHrEmpHolidayList(hrEmpHoliday);
            long holidays = (long) hrEmpHolidays.size();
            // Company holiday hours for the current month
            hrEmpPayrollDetail.setHolidaysHours(holidays * workerTime);
            // Attendance time, paid leave time, absence time, late time, early leave time.
            // Expected working days for the current month
            hrEmpPayrollDetail.setNickName(hrEmployees.getFullName());
            hrEmpPayrollDetail.setUserId(hrEmployees.getUserId());
            hrEmpPayrollDetail.setAvatar(hrEmployees.getAvatarUrl());
            // Expected working days for the current month
            hrEmpPayrollDetail.setMouthDays(matchDay - holidays);
            // Expected working hours for the current month
            hrEmpPayrollDetail.setWorkHours((matchDay - holidays) * workerTime);
            // Employee base salary
            hrEmpPayrollDetail.setBaseSalary(hrEmployees.getBaseSalary());
            // Attendance time
            HrAttendance attendance = new HrAttendance();
            attendance.setUserId(hrEmployees.getUserId());
            attendance.setParams(params);
            List<HrAttendance> hrAttendances = hrAttendanceMapper.selectHrAttendanceList(attendance);
            // Attendance time
            double presentHours = 0.0;
            for (HrAttendance hrAttendance1 : hrAttendances) {
                double myWorkTime = Double.parseDouble(hrAttendance1.getMyWorkTime());
                presentHours += myWorkTime;
            }
            // All attendance time statistics
            BigDecimal roundedHours = BigDecimal.valueOf(presentHours)
                    .setScale(2, RoundingMode.HALF_UP);
            // Overtime
            List<String> OverTime = hrAttendances.stream()
                    .map(HrAttendance::getOverTime)
                    .collect(Collectors.toList());
            double OverHours = calculateTotalLateHours(OverTime);
            hrEmpPayrollDetail.setOverHours(OverHours);

            // Late and early leave time
            List<String> collect1 = hrAttendances.stream()
                    .map(HrAttendance::getLateTime)
                    .collect(Collectors.toList());
            List<String> collect2 = hrAttendances.stream()
                    .map(HrAttendance::getEarlyTime)
                    .collect(Collectors.toList());
            double totalLateHours = calculateTotalLateHours(collect1);
            double totalLateHours2 = calculateTotalLateHours(collect2);
            // Late time
            hrEmpPayrollDetail.setLateHours(totalLateHours);
            // Early leave time
            hrEmpPayrollDetail.setEarlyHours(totalLateHours2);

            // Normal attendance time (including overtime, late, and early leave)
            hrEmpPayrollDetail.setPresentHours(roundedHours.doubleValue());

            // Leave time
            HrLeave leave = new HrLeave();
            leave.setParams(params);
            leave.setUserId(hrEmployees.getUserId());
            leave.setLeaveStatus("1");
            List<HrLeave> hrLeaves = hrLeaveMapper.selectHrLeaveList(leave);
            if (!hrLeaves.isEmpty()) {
                // Paid leave
                List<HrLeave> paidLeave = hrLeaves.stream().filter(x -> "5".equals(x.getLeaveType()))
                        .collect(Collectors.toList());
                // Unpaid leave
                hrLeaves = hrLeaves.stream().filter(x -> !("5".equals(x.getLeaveType())))
                        .collect(Collectors.toList());
                int totalDays = 0;
                int totalHours = 0;
                for (HrLeave leaves : hrLeaves) {
                    LocalDate leaveStart = leaves.getStateTime().toInstant()
                            .atZone(ZoneId.systemDefault()) // Use SystemDefault time zone
                            .toLocalDate();
                    LocalDate leaveEnd = leaves.getEndTime().toInstant()
                            .atZone(ZoneId.systemDefault()) // Use SystemDefault time zone
                            .toLocalDate();
                    if (leaveStart.equals(leaveEnd)) {
                        // Check if it is within the target month
                        if (!leaveStart.isBefore(rangeStart) && !leaveStart.isAfter(rangeEnd)) {
                            // Record single-day leave hours
                            totalHours += leaves.getLeaveDuration();
                        }
                        continue; // Skip subsequent multi-day processing
                    }
                    // 1. Calculate the intersection of the holiday and the target month
                    LocalDate effectiveStart = leaveStart.isBefore(rangeStart) ? rangeStart : leaveStart;
                    LocalDate effectiveEnd = leaveEnd.isAfter(rangeEnd) ? rangeEnd : leaveEnd;
                    // 2. Skip invalid intersections (no overlap)
                    if (effectiveStart.isAfter(effectiveEnd)) {
                        continue;
                    }
                    // 3. Calculate the actual holiday days within the month
                    long daysInMonth = ChronoUnit.DAYS.between(effectiveStart, effectiveEnd) + 1;
                    totalDays += daysInMonth;
                }

                hrEmpPayrollDetail.setLeaveHours(totalDays * workerTime + totalHours);
            } else {
                hrEmpPayrollDetail.setLeaveHours(0.0);
            }
            // Absence time
            HrAttendance attendance1 = new HrAttendance();
            attendance1.setUserId(hrEmployees.getUserId());
            attendance1.setStartDate(rangeStart.toString());
            attendance1.setEndDate(rangeEnd.toString());
            ArrayList<HrAttendance> hrAttendances1 = hrAttendanceMapper.selectHrAttendanceListByUser(attendance1);
            if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrAttendances1)) {
                if (schedulingEmp.getSchedulingType().equals('2')) {
                    String weekDays = schedulingEmp.getWeekDays();
                    List<Integer> weekdays = Arrays.stream(weekDays.substring(1, weekDays.length() - 1).split(","))
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                    hrAttendances1 = (ArrayList<HrAttendance>) hrAttendances1.stream()
                            .filter(a -> {
                                LocalDate date = LocalDate.parse(a.getAbsentDate());
                                Integer dayOfWeek = Integer.valueOf(date.getDayOfWeek().getValue());
                                return weekdays.contains(dayOfWeek);
                            })
                            .collect(Collectors.toList());
                }
                hrEmpPayrollDetail.setAbsentHours(hrAttendances1.size() * workerTime);
            } else {
                hrEmpPayrollDetail.setAbsentHours(0.0);
            }
            hrEmpPayrollDetails.add(hrEmpPayrollDetail);
        }
//        }
        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrAttendance.getSearchId())) {
            hrEmpPayrollDetails = hrEmpPayrollDetails.stream()
                    .filter(x -> x.getUserId().equals(hrAttendance.getSearchId()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        return hrEmpPayrollDetails;
    }

    @Override
    public ArrayList<HrEmpPayrollDetail> getPayrollsByHr(HrAttendance hrAttendance) {
        ArrayList<HrEmpPayrollDetail> hrEmpPayrollDetails = new ArrayList<>();
        LocalDate today = LocalDate.now();
        // Current month working days. User queries the time summary for each month from
        // employment to now.
        // Query the user's employment date
        List<Map<String, String>> dateRanges = new ArrayList<>();
        List<HrEmployees> hrEmployeesList = hrEmployeesMapper.selectHrEmployeesListByEid(hrAttendance.getEnterpriseId());
        for (HrEmployees hrEmployees : hrEmployeesList) {
            Date hireDate = hrEmployees.getHireDate();
            LocalDate localHireDate = hireDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            YearMonth startMonth = YearMonth.from(localHireDate);
            YearMonth endMonth = YearMonth.from(today);
            // Loop from the employment month to the current month
            for (YearMonth currentMonth = startMonth; !currentMonth.isAfter(endMonth); currentMonth = currentMonth
                    .plusMonths(1)) {

                LocalDate rangeStart, rangeEnd;

                if (currentMonth.equals(startMonth)) {
                    // Employment month: start from the actual employment date
                    rangeStart = localHireDate;
                    // If it is the current month, the end date is today; otherwise, it is the last
                    // day of the month
                    rangeEnd = currentMonth.equals(endMonth) ? today : currentMonth.atEndOfMonth();
                } else if (currentMonth.equals(endMonth)) {
                    // Current month: end today
                    rangeStart = currentMonth.atDay(1);
                    rangeEnd = today;
                } else {
                    // Complete month: from the 1st to the last day
                    rangeStart = currentMonth.atDay(1);
                    rangeEnd = currentMonth.atEndOfMonth();
                }
                // Query user scheduling and enterprise scheduling. First check if there is a
                // schedule for the current month.
                // If not, use the enterprise schedule; otherwise, skip directly.
                HrSchedulingEmp hrSchedulingEmp = new HrSchedulingEmp();
                hrSchedulingEmp.setUserId(hrEmployees.getUserId());
                hrSchedulingEmp.setSearchTime(rangeStart.withDayOfMonth(1).toString());
                HrSchedulingEmp schedulingEmp2 = hrSchedulingEmpMapper
                        .selectHrSchedulingEmpByDataAndUserId(hrSchedulingEmp);
                HrSettings hrSettings = hrSettingsMapper.selectHrSettingsByEid(SecurityUtils.getUserEnterpriseId());

                // Scheduling
                HrSchedulingEmp schedulingEmp = new HrSchedulingEmp();
                if (schedulingEmp2 != null) {
                    schedulingEmp = schedulingEmp2;
                } else if (hrSettings != null) {
                    HrEmpScheduling hrEmpScheduling = hrEmpSchedulingMapper
                            .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
                    if (ObjectUtils.isEmpty(hrEmpScheduling)) {
                        continue;
                    }
                    com.ys.common.core.utils.bean.BeanUtils.copyProperties(hrEmpScheduling, schedulingEmp);
                } else {
                    continue;
                }
                // Expected working hours for the day
                double hours = calculateHoursBetween(schedulingEmp.getDefaultStartTime(),
                        schedulingEmp.getDefaultEndTime());
                double RestHours = calculateHoursBetween(schedulingEmp.getRestStartTime(),
                        schedulingEmp.getRestEndTime());
                hours = hours - RestHours;
                // Round to one decimal place
                double workerTime = Math.round(hours * 10) / 10.0;
                // Expected working days
                long matchDay = 0;
                // Set whether the user scheduling is monthly or weekly. 1: Monthly 2: Weekly
                if (schedulingEmp.getSchedulingType().equals('1')) {
                    String monthDaysOff = schedulingEmp.getMonthDaysOff();
                    matchDay = daysBetweenInclusive(rangeStart.withDayOfMonth(1),
                            rangeStart.withDayOfMonth(rangeStart.lengthOfMonth())) - Integer.parseInt(monthDaysOff);
                } else {
                    String weekDays = schedulingEmp.getWeekDays();
                    int[] weekdays = Arrays.stream(weekDays.substring(1, weekDays.length() - 1).split(","))
                            .map(String::trim)
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    // matchDay = calculateWorkdaysSimple(weekdays, rangeStart, rangeEnd);
                    matchDay = calculateWorkdaysSimple(weekdays, rangeStart.withDayOfMonth(1),
                            rangeStart.withDayOfMonth(rangeStart.lengthOfMonth()));
                }
                // matchDay = matchDay - schedulingEmp.getPaidVacationDays();
                HrEmpPayrollDetail hrEmpPayrollDetail = new HrEmpPayrollDetail();
                HrEmpTime hrEmpTime = new HrEmpTime();
                hrEmpTime.setUserId(hrEmployees.getUserId());
                hrEmpTime.setSearchTime(rangeStart.withDayOfMonth(1).toString());
                HrEmpTime timeByUserId = hrEmpTimeMapper.getTimeByUserId(hrEmpTime);
                if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(timeByUserId)) {
                    BeanUtils.copyProperties(timeByUserId, hrEmpPayrollDetail);
                    hrEmpPayrollDetail.setMouthDays(Long.valueOf(timeByUserId.getMouthDays()));
                    Date payrollData = timeByUserId.getPayrollData();
                    hrEmpPayrollDetail.setPayrollData(new SimpleDateFormat("yyyy-MM").format(payrollData));
                    HrPayroll hrPayroll = new HrPayroll();
                    hrPayroll.setUserId(hrEmployees.getUserId());
                    hrPayroll.setSearchTime(rangeStart.withDayOfMonth(1).toString());
                    HrPayroll payRollByUserId = hrPayrollMapper.getPayRollByUserId(hrPayroll);
                    if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(payRollByUserId)) {
                        hrEmpPayrollDetail.setPayrollStatus(payRollByUserId.getPayrollStatus());
                    } else {
                        hrEmpPayrollDetail.setPayrollStatus('0');
                    }
                    hrEmpPayrollDetails.add(hrEmpPayrollDetail);
                    continue;
                }
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM");
                String format = rangeStart.format(formatter1);
                hrEmpPayrollDetail.setPayrollData(format);
                // Format as a string
                Map<String, String> range = new HashMap<>();
                String start = rangeStart.format(DATE_FORMATTER);
                String end = rangeEnd.format(DATE_FORMATTER);
                dateRanges.add(range);
                // Holiday days
                HrEmpHoliday hrEmpHoliday = new HrEmpHoliday();
                Map<String, Object> params = new HashMap<>();
                params.put("beginCreateTime", start);
                params.put("endCreateTime", end);
                hrEmpHoliday.setParams(params);
                hrEmpHoliday.setEnterpriseId(hrAttendance.getEnterpriseId());
                List<HrEmpHoliday> hrEmpHolidays = hrEmpHolidayMapper.selectHrEmpHolidayList(hrEmpHoliday);
                long holidays = (long) hrEmpHolidays.size();
                // Company holiday hours for the current month
                hrEmpPayrollDetail.setHolidaysHours(holidays * workerTime);
                // Attendance time, paid leave time, absence time, late time, early leave time.
                // Expected working days for the current month
                hrEmpPayrollDetail.setNickName(hrEmployees.getFullName());
                hrEmpPayrollDetail.setUserId(hrEmployees.getUserId());
                hrEmpPayrollDetail.setAvatar(hrEmployees.getAvatarUrl());
                // Expected working days for the current month
                hrEmpPayrollDetail.setMouthDays(matchDay - holidays);
                // Expected working hours for the current month
                hrEmpPayrollDetail.setWorkHours((matchDay - holidays) * workerTime);
                // Employee base salary
                hrEmpPayrollDetail.setBaseSalary(hrEmployees.getBaseSalary());
                // Attendance time
                HrAttendance attendance = new HrAttendance();
                attendance.setUserId(hrEmployees.getUserId());
                attendance.setParams(params);
                List<HrAttendance> hrAttendances = hrAttendanceMapper.selectHrAttendanceList(attendance);
                // Attendance time
                double presentHours = 0.0;
                for (HrAttendance hrAttendance1 : hrAttendances) {
                    double myWorkTime = Double.parseDouble(hrAttendance1.getMyWorkTime());
                    presentHours += myWorkTime;
                }
                // All attendance time statistics
                BigDecimal roundedHours = BigDecimal.valueOf(presentHours)
                        .setScale(2, RoundingMode.HALF_UP);
                // Overtime
                List<String> OverTime = hrAttendances.stream()
                        .map(HrAttendance::getOverTime)
                        .collect(Collectors.toList());
                double OverHours = calculateTotalLateHours(OverTime);
                hrEmpPayrollDetail.setOverHours(OverHours);

                // Late and early leave time
                List<String> collect1 = hrAttendances.stream()
                        .map(HrAttendance::getLateTime)
                        .collect(Collectors.toList());
                List<String> collect2 = hrAttendances.stream()
                        .map(HrAttendance::getEarlyTime)
                        .collect(Collectors.toList());
                double totalLateHours = calculateTotalLateHours(collect1);
                double totalLateHours2 = calculateTotalLateHours(collect2);
                // Late time
                hrEmpPayrollDetail.setLateHours(totalLateHours);
                // Early leave time
                hrEmpPayrollDetail.setEarlyHours(totalLateHours2);

                // Normal attendance time (including overtime, late, and early leave)
                hrEmpPayrollDetail.setPresentHours(roundedHours.doubleValue());

                // Leave time
                HrLeave leave = new HrLeave();
                leave.setParams(params);
                leave.setUserId(hrEmployees.getUserId());
                leave.setLeaveStatus("1");
                List<HrLeave> hrLeaves = hrLeaveMapper.selectHrLeaveList(leave);
                if (!hrLeaves.isEmpty()) {
                    // Paid leave
                    List<HrLeave> paidLeave = hrLeaves.stream().filter(x -> "5".equals(x.getLeaveType()))
                            .collect(Collectors.toList());
                    // Unpaid leave
                    hrLeaves = hrLeaves.stream().filter(x -> !("5".equals(x.getLeaveType())))
                            .collect(Collectors.toList());
                    int totalDays = 0;
                    int totalHours = 0;
                    for (HrLeave leaves : hrLeaves) {
                        LocalDate leaveStart = leaves.getStateTime().toInstant()
                                .atZone(ZoneId.systemDefault()) // Use SystemDefault time zone
                                .toLocalDate();
                        LocalDate leaveEnd = leaves.getEndTime().toInstant()
                                .atZone(ZoneId.systemDefault()) // Use SystemDefault time zone
                                .toLocalDate();
                        if (leaveStart.equals(leaveEnd)) {
                            // Check if it is within the target month
                            if (!leaveStart.isBefore(rangeStart) && !leaveStart.isAfter(rangeEnd)) {
                                // Record single-day leave hours
                                totalHours += leaves.getLeaveDuration();
                            }
                            continue; // Skip subsequent multi-day processing
                        }
                        // 1. Calculate the intersection of the holiday and the target month
                        LocalDate effectiveStart = leaveStart.isBefore(rangeStart) ? rangeStart : leaveStart;
                        LocalDate effectiveEnd = leaveEnd.isAfter(rangeEnd) ? rangeEnd : leaveEnd;
                        // 2. Skip invalid intersections (no overlap)
                        if (effectiveStart.isAfter(effectiveEnd)) {
                            continue;
                        }
                        // 3. Calculate the actual holiday days within the month
                        long daysInMonth = ChronoUnit.DAYS.between(effectiveStart, effectiveEnd) + 1;
                        totalDays += daysInMonth;
                    }

                    hrEmpPayrollDetail.setLeaveHours(totalDays * workerTime + totalHours);
                } else {
                    hrEmpPayrollDetail.setLeaveHours(0.0);
                }
                // Absence time
                HrAttendance attendance1 = new HrAttendance();
                attendance1.setUserId(hrEmployees.getUserId());
                attendance1.setStartDate(rangeStart.toString());
                attendance1.setEndDate(rangeEnd.toString());
                ArrayList<HrAttendance> hrAttendances1 = hrAttendanceMapper.selectHrAttendanceListByUser(attendance1);
                if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrAttendances1)) {
                    if (schedulingEmp.getSchedulingType().equals('2')) {
                        String weekDays = schedulingEmp.getWeekDays();
                        List<Integer> weekdays = Arrays.stream(weekDays.substring(1, weekDays.length() - 1).split(","))
                                .map(String::trim)
                                .map(Integer::parseInt)
                                .collect(Collectors.toList());
                        hrAttendances1 = (ArrayList<HrAttendance>) hrAttendances1.stream()
                                .filter(a -> {
                                    LocalDate date = LocalDate.parse(a.getAbsentDate());
                                    Integer dayOfWeek = Integer.valueOf(date.getDayOfWeek().getValue());
                                    return weekdays.contains(dayOfWeek);
                                })
                                .collect(Collectors.toList());
                    }
                    hrEmpPayrollDetail.setAbsentHours(hrAttendances1.size() * workerTime);
                } else {
                    hrEmpPayrollDetail.setAbsentHours(0.0);
                }
                hrEmpPayrollDetails.add(hrEmpPayrollDetail);
            }
        }
        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrAttendance.getSearchId())) {
            hrEmpPayrollDetails = hrEmpPayrollDetails.stream()
                    .filter(x -> x.getUserId().equals(hrAttendance.getSearchId()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        return hrEmpPayrollDetails;
    }

    @Override
    public List<Map> getPayrollsCount(HrAttendance hrAttendance) {
        List<Map> payrollsCount = hrAttendanceMapper.getPayrollsCount(hrAttendance);
//        COUNT(*) FILTER (WHERE TRUE) AS allRecordCount
        return payrollsCount;
    }

    /**
     * Query User's Weekly Attendance
     *
     * @param userId
     * @return
     */
    @Override
    public List<HrAttendance> getUserWeekAttendance(Long userId) {
        List<HrAttendance> list = hrAttendanceMapper.selectUserWeekAttendance(userId);
        HrAttendance hrAttendance = new HrAttendance();
        // Get current year and month
        hrAttendance.setSearchTime(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
        // Query absence times
        String firstDay = getFirstDayOfMonth(hrAttendance.getSearchTime());
        HrSchedulingEmp hrSchedulingEmp = new HrSchedulingEmp();
        hrSchedulingEmp.setSearchTime(firstDay.toString());
        hrSchedulingEmp.setUserId(hrAttendance.getUserId());
        List<HrSchedulingEmp> hrSchedulingEmps = hrSchedulingEmpMapper.selectHrSchedulingEmpByUserId(hrSchedulingEmp);

        // Employee scheduling information
        HrSchedulingEmp schedulingEmp = null;
        long NANOS_PER_DAY = 24 * 60 * 60 * 1_000_000_000L;
        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrSchedulingEmps)) {
            schedulingEmp = hrSchedulingEmps.get(0);
        } else {
            // Company scheduling settings
            HrSettings hrSettings = hrSettingsMapper.selectHrSettingsByEid(SecurityUtils.getUserEnterpriseId());
            HrEmpScheduling hrEmpScheduling = hrEmpSchedulingMapper
                    .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
            schedulingEmp = new HrSchedulingEmp();
            // Copy data to schedulingEmp object
            com.ys.common.core.utils.bean.BeanUtils.copyProperties(hrEmpScheduling, schedulingEmp);
        }
        long startNano = schedulingEmp.getRestStartTime().toNanoOfDay();
        long endNano = schedulingEmp.getRestEndTime().toNanoOfDay();

        // Handle cross-day situation
        if (schedulingEmp.getRestEndTime().isBefore(schedulingEmp.getRestStartTime())) {
            endNano += NANOS_PER_DAY; // Add one day's nanoseconds
        }

        long nanoDiff = Math.abs(endNano - startNano);
        double hours = nanoDiff / 3_600_000_000_000.0; // Convert nanoseconds to hours

        // Round to two decimal places
        BigDecimal bd = BigDecimal.valueOf(hours);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        BigDecimal finalBd = bd;
        // Set break time
        list.forEach(item -> item.setRestTime(finalBd.doubleValue()));
        Character c = '1';
        if (c.equals(schedulingEmp.getSchedulingType())) {
            // Monthly
            list.forEach(item -> {
                if ("3".equals(item.getIsBefore())) {
                    // Waiting for work
                    item.setAttendanceStatus("Wait Work");
                    item.setShowType("2");
                } else {
                    // Check if clocked in
                    if (!ObjectUtils.isEmpty(item.getCheckIn()) || !ObjectUtils.isEmpty(item.getCheckOut())) {
                        item.setShowType("1");
                    } else {
                        item.setShowType("2");
                        HrLeave leave = hrLeaveMapper.selectLeaveByUserId(userId, item.getAttendanceTime());
                        if (!ObjectUtils.isEmpty(leave)) {
                            item.setAttendanceStatus("Leave");
                        } else {
                            // Check for holidays if not on leave
                            HrEmpHoliday hrEmpHoliday = hrEmpHolidayMapper.selectHolidayByUserId(userId,
                                    item.getAttendanceTime());
                            if (!ObjectUtils.isEmpty(hrEmpHoliday)) {
                                item.setAttendanceStatus(StringUtils.isEmpty(hrEmpHoliday.getHolidayName()) ? "Holiday"
                                        : hrEmpHoliday.getHolidayName());
                            } else {
                                item.setAttendanceStatus("Absence");
                            }
                        }
                    }
                }
            });
        } else {
            // Weekly
            // Get working days
            String weekDays = schedulingEmp.getWeekDays();
            List<String> weeks = StringUtils.convertNumbersToWeek(weekDays);
            list.forEach(item -> {
                // Determine if it's today, before, or after
                if ("3".equals(item.getIsBefore())) {
                    if (weeks.contains(item.getWeekDay())) {
                        // Waiting for work
                        item.setAttendanceStatus("Wait Work");
                        item.setShowType("2");
                    } else {
                        // Not a working day
                        item.setAttendanceStatus("Not Working");
                        item.setShowType("2");
                    }
                } else {
                    // Before today
                    if (weeks.contains(item.getWeekDay())) {
                        // Check if clocked in
                        if (!ObjectUtils.isEmpty(item.getCheckIn()) || !ObjectUtils.isEmpty(item.getCheckOut())) {
                            item.setShowType("1");
                        } else {
                            item.setShowType("2");
                            HrLeave leave = hrLeaveMapper.selectLeaveByUserId(userId, item.getAttendanceTime());
                            if (!ObjectUtils.isEmpty(leave)) {
                                item.setAttendanceStatus("Leave");
                            } else {
                                // Check for holidays if not on leave
                                HrEmpHoliday hrEmpHoliday = hrEmpHolidayMapper.selectHolidayByUserId(userId,
                                        item.getAttendanceTime());
                                if (!ObjectUtils.isEmpty(hrEmpHoliday)) {
                                    item.setAttendanceStatus(
                                            StringUtils.isEmpty(hrEmpHoliday.getHolidayName()) ? "Holiday"
                                                    : hrEmpHoliday.getHolidayName());
                                } else {
                                    item.setAttendanceStatus("Absence");
                                }
                            }
                        }
                    } else {
                        // Not a working day
                        item.setAttendanceStatus("Not Working");
                        item.setShowType("2");
                    }
                }
            });
        }

        return list;
    }

    /**
     * Total Present
     *
     * @param userEnterpriseId
     * @return
     */
    @Override
    public Map<String, Object> selectPresentTotal(String userEnterpriseId) {
        EmployeePresenCountVo vo = baseMapper.selectPresentTotal(userEnterpriseId);
        Map<String, Object> map = new HashMap<>();
        map.put("thisDays", vo.getThisDays());
        if (vo.getThisDays() > vo.getLastDays()) {
            // Rising
            map.put("ratioType", 1);
        } else if (vo.getThisDays() < vo.getLastDays()) {
            // Falling
            map.put("ratioType", 2);
        } else {
            // Flat
            map.put("ratioType", 1);
        }
        BigDecimal thisMonth = BigDecimal.valueOf(vo.getThisDays());
        BigDecimal lastMonth = BigDecimal.valueOf(vo.getLastDays());

        if (lastMonth.compareTo(BigDecimal.ZERO) == 0) {
            map.put("ratio", 100);
        } else {
            // (this - last) / last * 100
            BigDecimal diff = thisMonth.subtract(lastMonth);
            BigDecimal ratio = diff
                    .divide(lastMonth, 2, RoundingMode.HALF_UP) // Calculate ratio first, retain 2 decimal places
                    .multiply(BigDecimal.valueOf(100)); // Multiply by 100 to get percentage
            map.put("ratio", ratio);
        }
        return map;
    }

    /**
     * Total Absent
     *
     * @param userEnterpriseId
     * @return
     */
    @Override
    public Map<String, Object> selectAbsentTotal(String userEnterpriseId) {
        Character type = '1';
        Map<String, Object> map = new HashMap<>();
        // Query all employees
        List<HrEmployees> employeesList = hrEmployeesMapper.selectHrEmployeesListByBeforeHireDate(userEnterpriseId);

        // Today's absence days
        AtomicInteger thisTotalCount = new AtomicInteger();

        // Yesterday's absence days
        AtomicInteger lastTotalCount = new AtomicInteger();

        // Query if yesterday was a holiday
        Integer lastHolidayCount = hrEmpHolidayMapper.selectHolidayByLastDay(userEnterpriseId);
        // Query if today is a holiday
        Integer thisHolidayCount = hrEmpHolidayMapper.selectHolidayByThisDay(userEnterpriseId);

        // Get current time
        LocalDate today = LocalDate.now();
        // Format as "year-month"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonthStr = today.format(formatter);

        // Employee scheduling information
        employeesList.stream().forEach(emp -> {
            HrEmpScheduling hrEmpScheduling = null;
            // Query employee scheduling
            HrSchedulingEmp schedulingEmp = hrSchedulingEmpMapper.selectSchedulingEmpByUserIdAndDate(emp.getUserId(),
                    yearMonthStr);
            if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(schedulingEmp)) {
                // Employee scheduling
                hrEmpScheduling = hrEmpSchedulingMapper
                        .selectHrEmpSchedulingBySchedulingId(schedulingEmp.getSchedulingId());
            } else {
                HrSettings hrSettings = hrSettingsMapper.selectHrSettingsByEid(userEnterpriseId);
                if (!ObjectUtils.isEmpty(hrSettings)) {
                    hrEmpScheduling = hrEmpSchedulingMapper
                            .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
                }
            }
            if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrEmpScheduling)) {
                if (type.equals(hrEmpScheduling.getSchedulingType())) {
                    // Monthly scheduling
                    if (Integer.valueOf(0).equals(lastHolidayCount)) {
                        // Not a holiday
                        // Query yesterday's clock-in count
                        Integer lastDayCount = hrAttendanceMapper.selectAttendanceByLastDay(emp.getUserId());
                        // Query yesterday's leave days
                        Integer lastLeaveCount = hrLeaveMapper.selectLeaveByLastDay(emp.getUserId(),
                                hrEmpScheduling.getDefaultStartTime(), hrEmpScheduling.getDefaultEndTime());
                        if (Integer.valueOf(0).equals(lastDayCount) && Integer.valueOf(0).equals(lastLeaveCount)) {
                            lastTotalCount.addAndGet(1);
                        }
                    }
                    if (Integer.valueOf(0).equals(thisHolidayCount)) {
                        // Today's clock-in count
                        Integer thisDayCount = hrAttendanceMapper.selectAttendanceByThisDay(emp.getUserId());
                        // Query today's leave days
                        Integer thisLeaveCount = hrLeaveMapper.selectLeaveByThisDay(emp.getUserId(),
                                hrEmpScheduling.getDefaultStartTime(), hrEmpScheduling.getDefaultEndTime());
                        if (Integer.valueOf(0).equals(thisDayCount) && Integer.valueOf(0).equals(thisLeaveCount)) {
                            thisTotalCount.addAndGet(1);
                        }
                    }

                } else {
                    // Weekly scheduling
                    // Get working days
                    if (!ObjectUtils.isEmpty(schedulingEmp) && StringUtils.isNotEmpty(schedulingEmp.getWeekDays())) {
                        String weekDays = schedulingEmp.getWeekDays();
                        List<String> weeks = StringUtils.convertNumbersToWeek(weekDays);
                        // Yesterday
                        LocalDate yesterday = today.minusDays(1);
                        String todayToWeek = timeToWeek(today.getDayOfWeek());
                        String yesterdayToWeek = timeToWeek(yesterday.getDayOfWeek());
                        if (weeks.contains(todayToWeek)) {
                            // Today's clock-in count
                            Integer thisDayCount = hrAttendanceMapper.selectAttendanceByThisDay(emp.getUserId());
                            // Query today's leave days
                            Integer thisLeaveCount = hrLeaveMapper.selectLeaveByThisDay(emp.getUserId(),
                                    hrEmpScheduling.getDefaultStartTime(), hrEmpScheduling.getDefaultEndTime());
                            if (Integer.valueOf(0).equals(thisDayCount) && Integer.valueOf(0).equals(thisLeaveCount)) {
                                lastTotalCount.addAndGet(1);
                            }
                        }
                        if (weeks.contains(yesterdayToWeek)) {
                            // Not a holiday
                            // Query yesterday's clock-in count
                            Integer lastDayCount = hrAttendanceMapper.selectAttendanceByLastDay(emp.getUserId());
                            // Query yesterday's leave days
                            Integer lastLeaveCount = hrLeaveMapper.selectLeaveByLastDay(emp.getUserId(),
                                    hrEmpScheduling.getDefaultStartTime(), hrEmpScheduling.getDefaultEndTime());
                            if (Integer.valueOf(0).equals(lastDayCount) && Integer.valueOf(0).equals(lastLeaveCount)) {
                                thisTotalCount.addAndGet(1);
                            }
                        }
                    }
                }
            }
        });

        BigDecimal thisAbsenceDays = new BigDecimal(thisTotalCount.get());
        BigDecimal lastAbsenceDays = new BigDecimal(lastTotalCount.get());
        map.put("totalCount", thisTotalCount.get());
        map.put("ratioType", thisAbsenceDays.compareTo(lastAbsenceDays) > 0 ? "1" : "2");

        // Absence comparison rate, prevent division by zero
        BigDecimal absenceRateChange = BigDecimal.ZERO;
        if (lastAbsenceDays.compareTo(BigDecimal.ZERO) != 0) {
            absenceRateChange = thisAbsenceDays.subtract(lastAbsenceDays)
                    .divide(lastAbsenceDays, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        } else {
            map.put("ratio", 100);
        }
        map.put("ratio", absenceRateChange);
        return map;
    }

    /**
     * Statistics of Attendance Rate
     *
     * @param userEnterpriseId
     * @return
     */
    @Override
    public List<AttendanceRateVo> selectAttendanceRate(String userEnterpriseId, Integer year) {
        Character type = '1';
        // Statistics of clocked-in records
        List<AttendanceRateVo> voList = hrAttendanceMapper.selectAttendanceRate(userEnterpriseId, year);
        // Query employee list
        HrEmployees hrEmployees = new HrEmployees();
        hrEmployees.setEnterpriseId(userEnterpriseId);
        List<HrEmployees> employeesList = hrEmployeesMapper.selectHrEmployeesList(hrEmployees);
        // Employee scheduling information
        AtomicReference<HrSchedulingEmp> schedulingEmpRef = new AtomicReference<>();
        voList.stream().forEach(vo -> {
            // Parse year and month
            YearMonth yearMonth = YearMonth.parse(vo.getDate());

            // Get the number of days in the current month
            int daysInMonth = yearMonth.lengthOfMonth();
            if (vo.getAttendanceCount() <= 0) {
                vo.setAttendanceRate(BigDecimal.ZERO);
            } else {
                AtomicInteger totalCount = new AtomicInteger();
                // Query absence times
                String firstDay = getFirstDayOfMonth(vo.getDate());
                // Query the number of schedules under the specified year and month
                employeesList.stream().forEach(employee -> {
                    HrSchedulingEmp hrSchedulingEmp = new HrSchedulingEmp();
                    hrSchedulingEmp.setSearchTime(firstDay.toString());
                    hrSchedulingEmp.setUserId(employee.getUserId());
                    List<HrSchedulingEmp> hrSchedulingEmps = hrSchedulingEmpMapper
                            .selectHrSchedulingEmpByUserId(hrSchedulingEmp);
                    if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrSchedulingEmps)) {
                        schedulingEmpRef.set(hrSchedulingEmps.get(0));
                    } else {
                        // Company scheduling settings
                        HrSettings hrSettings = hrSettingsMapper
                                .selectHrSettingsByEid(SecurityUtils.getUserEnterpriseId());
                        HrEmpScheduling hrEmpScheduling = hrEmpSchedulingMapper
                                .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
                        HrSchedulingEmp schedulingEmp = new HrSchedulingEmp();
                        // Copy data to schedulingEmp object
                        com.ys.common.core.utils.bean.BeanUtils.copyProperties(hrEmpScheduling, schedulingEmp);
                        schedulingEmpRef.set(schedulingEmp);
                    }

                    if (type.equals(schedulingEmpRef.get().getSchedulingType())) {
                        // Monthly scheduling
                        if (ObjectUtils.isEmpty(schedulingEmpRef.get().getMonthDaysOff())) {
                            totalCount.addAndGet(daysInMonth);
                        } else {
                            totalCount
                                    .addAndGet(daysInMonth - Integer.valueOf(schedulingEmpRef.get().getMonthDaysOff()));
                        }
                    } else {
                        // Weekly scheduling
                        List<Integer> weekdays = Arrays
                                .stream(schedulingEmpRef.get().getWeekDays()
                                        .substring(1, schedulingEmpRef.get().getWeekDays().length() - 1).split(","))
                                .map(String::trim)
                                .map(Integer::valueOf)
                                .collect(Collectors.toList());
                        // Calculate working days in the current month
                        int i = calculateWorkdays(vo.getDate(), weekdays);
                        totalCount.addAndGet(daysInMonth - i);
                    }
                });
                BigDecimal readyCount = BigDecimal.valueOf(vo.getAttendanceCount());
                BigDecimal shouldCount = BigDecimal.valueOf(totalCount.get());

                if (shouldCount.compareTo(BigDecimal.ZERO) == 0 || readyCount.compareTo(shouldCount) > 0) {
                    vo.setAttendanceRate(new BigDecimal(100));
                } else {
                    // (this / last) * 100
                    BigDecimal ratio = readyCount
                            .divide(shouldCount, 2, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100)); // Multiply by 100 to get percentage
                    vo.setAttendanceRate(ratio);
                }
            }
        });
        return voList;
    }

    /**
     * Statistics of employees' working hours for the month
     *
     * @param hrAttendance
     * @return
     */
    @Override
    public List<HrEmpPayrollDetail> selectPayrollsByHr(HrAttendance hrAttendance,List<HrEmployees> hrEmployeesList) {
        List<HrEmpPayrollDetail> details = new ArrayList<>();
        // Format as "year-month"
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String yearMonthStr = format.format(hrAttendance.getSearchData());
        for (HrEmployees hrEmployees : hrEmployeesList) {
            HrEmpPayrollDetail detail = new HrEmpPayrollDetail();
            detail.setUserId(hrEmployees.getUserId());
            detail.setNickName(hrEmployees.getFullName());
            detail.setEnterpriseId(hrEmployees.getEnterpriseId());
            detail.setPayrollData(yearMonthStr);
            detail.setMouthDays(0L);
            detail.setPresentHours(0.0);
            detail.setHolidaysHours(0.0);
            detail.setLeaveHours(0.0);
            detail.setLateHours(0.0);
            detail.setEarlyHours(0.0);
            detail.setOverHours(0.0);
            detail.setAbsentHours(0.0);
            HrPayroll hrPayroll = new HrPayroll();
            hrPayroll.setUserId(hrEmployees.getUserId());
            hrPayroll.setSearchTime(yearMonthStr);
            detail.setPayrollStatus('0');
            HrPayroll payRollByUserId = hrPayrollMapper.getPayRollByUserId(hrPayroll);
            if (!ObjectUtils.isEmpty(payRollByUserId)) {
                continue;
            }
            // Query employee scheduling
            HrEmpScheduling hrEmpScheduling = null;
            HrSchedulingEmp schedulingEmp = hrSchedulingEmpMapper.selectSchedulingEmpByUserIdAndDate(hrEmployees.getUserId(),
                    yearMonthStr);
            // company scheduling
            HrSettings hrSettings = hrSettingsMapper.selectHrSettingsByEid(hrEmployees.getEnterpriseId());
            if (!ObjectUtils.isEmpty(schedulingEmp)) {
                // Employee scheduling
                hrEmpScheduling = hrEmpSchedulingMapper
                        .selectHrEmpSchedulingBySchedulingId(schedulingEmp.getSchedulingId());
            } else {
                if (!ObjectUtils.isEmpty(hrSettings)) {
                    hrEmpScheduling = hrEmpSchedulingMapper
                            .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
                }
            }
            double roundedHours = 0.0;
            if (!ObjectUtils.isEmpty(hrEmpScheduling)) {
                // Calculate total working hours (supports cross-day)
                double hours = calculateHoursBetween(hrEmpScheduling.getDefaultStartTime(), hrEmpScheduling.getDefaultEndTime());

                // Calculating rest time
                double RestHours = calculateHoursBetween(hrEmpScheduling.getRestStartTime(), hrEmpScheduling.getRestEndTime());

                // Working hours
                hours = hours - RestHours;
                // Round to one decimal place
                roundedHours = Math.round(hours * 10) / 10.0;
                if (hrEmpScheduling.getSchedulingType().equals('1')) {
                    int daysInMonth = getDaysInMonth(hrAttendance.getSearchData());
                    daysInMonth = daysInMonth - Integer.valueOf(hrEmpScheduling.getMonthDaysOff());
                    detail.setMouthDays(Long.valueOf(daysInMonth));
                    detail.setWorkHours(daysInMonth * roundedHours);
                } else {
                    if (StringUtils.isNotEmpty(hrEmpScheduling.getWeekDays())) {
                        String weekDays = hrEmpScheduling.getWeekDays();
                        List<Integer> weekdays = Arrays.stream(weekDays.substring(1, weekDays.length() - 1).split(","))
                                .map(String::trim)
                                .map(Integer::parseInt)
                                .collect(Collectors.toList());
                        int i = countWorkingDays(hrAttendance.getSearchData(), weekdays);
                        detail.setMouthDays(Long.valueOf(i));
                        detail.setWorkHours(i * roundedHours);
                    }
                }
            }

            //Check leave status And Paid  leave
            List<HrLeave> leaves = hrLeaveMapper.selectLeaveByUserIdAndDate(hrEmployees.getUserId(), hrAttendance.getSearchData());
            if (!leaves.isEmpty()) {
                double count = 0.0;
                double paid = 0.0;
                for (HrLeave leave : leaves) {
                    count += calculateLeaveHours(leave.getStateTime(),
                            leave.getEndTime(), hrAttendance.getSearchData(), roundedHours);
                    if(!leave.getPaidLeave().isEmpty() &&  "1".equals(leave.getPaidLeave())){
                    paid += calculateLeaveHours(leave.getStateTime(),
                                leave.getEndTime(), hrAttendance.getSearchData(), roundedHours);
                    }
                }
                detail.setPaidHours(paid);
                detail.setLeaveHours(Double.valueOf(count));
            }

            // Check vacation status
            List<HrEmpHoliday> hrEmpHolidays = hrEmpHolidayMapper.selectByUserIdAndDate(hrEmployees.getEnterpriseId(), hrAttendance.getSearchData());
            if (!hrEmpHolidays.isEmpty()) {
                double count = 0.0;
                for (HrEmpHoliday hrEmpHoliday : hrEmpHolidays) {
                    count += calculateLeaveHours(hrEmpHoliday.getStateTime(),
                            hrEmpHoliday.getEndTime(), hrAttendance.getSearchData(), roundedHours);
                }
                detail.setHolidaysHours(count);
            }


            // Statistics of working hours this month
            List<HrAttendance> attendances = hrAttendanceMapper.selectPresentHours(hrEmployees.getUserId(), hrAttendance.getSearchData());
            if (!attendances.isEmpty()) {
                double totalWorkTime = attendances.stream()
                        .mapToDouble(att -> {
                            try {
                                return Double.parseDouble(att.getMyWorkTime());
                            } catch (NumberFormatException e) {
                                return 0.0;
                            }
                        })
                        .sum();
                Double paidHours = detail.getPaidHours();
                detail.setPresentHours(totalWorkTime+paidHours);
                List<String> overTimeList = attendances.stream()
                        .map(HrAttendance::getOverTime)
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                detail.setOverHours(sumTimeStringsAsHours(overTimeList));

                List<String> lateTimeList = attendances.stream()
                        .map(HrAttendance::getLateTime)
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                detail.setLateHours(sumTimeStringsAsHours(lateTimeList));

                List<String> earlyTimeList = attendances.stream()
                        .map(HrAttendance::getEarlyTime)
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                detail.setEarlyHours(sumTimeStringsAsHours(earlyTimeList));
            }

            //Violations
            //Absent
            HrAttendance hrAttendanceTemp = new HrAttendance();
            hrAttendanceTemp.setUserId(hrEmployees.getUserId());
            hrAttendanceTemp.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
            Date hireDate = hrEmployees.getHireDate();
            String hireDateStr = format.format(hireDate);
            YearMonth yearMonth = YearMonth.parse(yearMonthStr, DateTimeFormatter.ofPattern("yyyy-MM"));
            if(yearMonthStr.equals(hireDateStr)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String s = sdf.format(hireDate);
                hrAttendanceTemp.setStartDate(s);
            }else{
                LocalDate start = yearMonth.atDay(1);
                String s = start.format(DATE_FORMATTER);
                hrAttendanceTemp.setStartDate(s);
            }
            LocalDate endOfMonth = yearMonth.atEndOfMonth();
            hrAttendanceTemp.setEndDate(endOfMonth.format(DATE_FORMATTER));
            ArrayList<HrAttendance> hrAttendances = hrAttendanceMapper.selectHrAttendanceListByUser(hrAttendanceTemp);
            if (hrAttendances.isEmpty()) {
                detail.setAbsentHours(0.0);
            } else {
                if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(hrEmpScheduling) && hrEmpScheduling.getSchedulingType().equals('2')) {
                    String weekDays = hrEmpScheduling.getWeekDays();
                    List<Integer> weekdays = Arrays.stream(weekDays.substring(1, weekDays.length() - 1).split(","))
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                    int i = 0;
                    for (HrAttendance hrAttendance1 : hrAttendances) {
                        String currentDate = hrAttendance1.getAbsentDate();
                        LocalDate localDate = LocalDate.parse(currentDate);
                        if (weekdays.contains(localDate.getDayOfWeek().getValue())) {
                            i++;
                        }
                    }
                    detail.setAbsentHours(i*roundedHours);
                }
            }
            details.add(detail);
        }
        return details;
    }

    private int calculateWorkdaysSimple(int[] weekdays, LocalDate startDate, LocalDate endDate) {
        // 1. Convert the workday array to a Set
        Set<DayOfWeek> workdaysSet = new HashSet<>();
        for (int day : weekdays) {
            workdaysSet.add(numberToDayOfWeek(day));
        }

        // 2. Iterate through each day in the date range
        int workdayCount = 0;
        LocalDate current = startDate;

        // Ensure the start date is before or equal to the end date
        while (!current.isAfter(endDate)) {
            if (workdaysSet.contains(current.getDayOfWeek())) {
                workdayCount++;
            }
            current = current.plusDays(1);
        }

        return workdayCount;
    }

    private DayOfWeek numberToDayOfWeek(int number) {
        // ISO standard: DayOfWeek.MONDAY = 1, SUNDAY = 7
        if (number < 1 || number > 7) {
            throw new IllegalArgumentException("Working day number must be between 1-7");
        }
        return DayOfWeek.of(number);
    }

    public HrEmpSchedulingDTO convertToDTO(HrEmpScheduling entity) {
        HrEmpSchedulingDTO dto = new HrEmpSchedulingDTO();
        BeanUtils.copyProperties(entity, dto);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convert string to List<Integer>
            if (entity.getWeekDays() != null) {
                dto.setWeekDayList(objectMapper.readValue(entity.getWeekDays(), new TypeReference<List<Integer>>() {
                }));
            }

            // Convert string to List<String>
            if (entity.getMonthDaysOff() != null) {
                dto.setMonthDayOffList(
                        objectMapper.readValue(entity.getMonthDaysOff(), new TypeReference<List<String>>() {
                        }));
            }

            // Convert string to Map<String, DailyShift>
            if (entity.getDailySchedule() != null) {
                dto.setDailyScheduleMap(
                        objectMapper.readValue(entity.getDailySchedule(), new TypeReference<Map<String, DailyShift>>() {
                        }));
            }

        } catch (Exception e) {
            e.printStackTrace(); // It can be changed to log record.
            // You can also throw a business exception to prompt data abnormality.
        }

        return dto;
    }

    // The total sum of lateness time
    public static double calculateTotalLateHours(List<String> hrAttendances) {
        // 1. Process the lateness time using Stream.
        long totalSeconds = hrAttendances.stream()
                // Filter out null values or invalid character strings.
                .filter(time -> time != null && !time.trim().isEmpty())
                // Convert the time character string into seconds.
                .mapToLong(timeStr -> {
                    String[] parts = timeStr.split(":");
                    int hours = Integer.parseInt(parts[0]);
                    int minutes = Integer.parseInt(parts[1]);
                    int seconds = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
                    return hours * 3600L + minutes * 60L + seconds;
                })
                // Accumulate all the seconds.
                .sum();

        // 2. Convert the total number of seconds into hours, rounded to two decimal
        // places.
        double totalHours = totalSeconds / 3600.0;
        return Math.round(totalHours * 100.0) / 100.0; // Round to two decimal places.
    }

    // Calculate the number of days difference within the same month.
    public static long daysBetweenInclusive(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return 0;
        }
        // Note: Period.getDays() returns the number of days difference within a month,
        // without crossing months.
        return start.until(end).getDays() + 1L; // Only applicable within the same month.
    }

    // Auxiliary method: Convert Date to LocalDate.
    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // Calculate the time difference.
    public static String calculateTimeDifference(LocalTime start, LocalTime end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        try {
            // Parse the character string into a LocalTime object
            // LocalTime start = LocalTime.parse(startTime, formatter);

            // Calculate the time difference (Duration will automatically handle cross-day
            // scenarios, but here we process it as the absolute value of the difference)
            Duration duration = Duration.between(start, end);
            if (duration.isNegative()) {
                duration = duration.negated();
            }

            // Extract total hours, minutes and seconds
            long totalHours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            long seconds = duration.getSeconds() % 60;

            // Format as HH:mm:ss
            return String.format("%02d:%02d:%02d", totalHours, minutes, seconds);

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Time format error, should be HH:mm:ss.");
        }
    }

    // Calculate the percentage.
    public static String calculateGrowthRateWithSign(Long lastMonth, Long thisMonth) {
        if (lastMonth == null) {
            lastMonth = 0L;
        }
        if (thisMonth == null) {
            thisMonth = 0L;
        }
        // Handle the case where the denominator is zero.
        if (lastMonth == 0) {
            return "0.00%";
        }
        // Calculate the amount of increase.
        Long difference = thisMonth - lastMonth;
        try {
            BigDecimal growthRate = new BigDecimal(difference)
                    .divide(new BigDecimal(lastMonth), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(2, RoundingMode.HALF_UP);
            String sign = growthRate.signum() >= 0 ? "+" : "";
            return sign + growthRate + "%";

        } catch (ArithmeticException e) {
            return "0.00%";
        }
    }

    // Time Conversion
    public static LocalTime dateToLocalTime(Date date) {
        if (date == null)
            return null;

        // 1. Force to specify the Chinese time zone (Asia/Shanghai)
        ZoneId zone = ZoneId.of("Asia/Shanghai");
        ZonedDateTime zonedDateTime = date.toInstant().atZone(zone);

        // 2. Check if it is the benchmark date associated with Excel Default
        // (1899-12-31).
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        if (localDateTime.toLocalDate().equals(LocalDate.of(1899, 12, 31))) {
            // 3. Correct historical time zone offset (UTC+8:05 → UTC+8)
            localDateTime = localDateTime.minusMinutes(5);
        }

        return localDateTime.toLocalTime();
    }

    // Calculate the time difference.
    public static double calculateHoursBetween(LocalTime start, LocalTime end) {
        // Check if it crosses midnight (end time is earlier than start time).
        if (end.isBefore(start)) {
            // Calculate the duration from the start time of the day to midnight.
            Duration firstHalf = Duration.between(start, LocalTime.MAX).plusSeconds(1);
            // Calculate the duration from midnight to the end time of the next day.
            Duration secondHalf = Duration.between(LocalTime.MIN, end);
            // Merge two time periods.
            return firstHalf.plus(secondHalf).toMinutes() / 60.0;
        } else {
            // For cases that do not cross midnight, calculate directly.
            return Duration.between(start, end).toMinutes() / 60.0;
        }
    }

    /**
     * Calculate the number of working days in the middle of the month.
     *
     * @param yearMonthStr
     * @param workdays
     * @return
     */
    public int calculateWorkdays(String yearMonthStr, List<Integer> workdays) {
        YearMonth yearMonth = YearMonth.parse(yearMonthStr);
        int daysInMonth = yearMonth.lengthOfMonth();
        int workdayCount = 0;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = yearMonth.atDay(day);
            int dayOfWeek = date.getDayOfWeek().getValue(); // Monday = 1, Sunday = 7

            if (workdays.contains(dayOfWeek)) {
                workdayCount++;
            }
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
        // The value range of DayOfWeek is 1-7, where MONDAY = 1 and SUNDAY = 7.
        int index = dayOfWeek.getValue() - 1;
        return dayNames[index];
    }

    public LocalTime convert(Date date) {
        Time sqlTime = new Time(date.getTime());
        LocalTime time = sqlTime.toLocalTime();
        return time;
    }

    /**
     * Calculates the number of leave hours taken by the specified employee in searchData (month).
     */
    private double calculateLeaveHours(Date stateTime,
                                       Date endTime,
                                       Date searchData,
                                       double roundedHours) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime start = Instant.ofEpochMilli(stateTime.getTime())
                .atZone(zone)
                .toLocalDateTime();
        LocalDateTime end = Instant.ofEpochMilli(endTime.getTime())
                .atZone(zone)
                .toLocalDateTime();

        LocalDate yearMonth = Instant.ofEpochMilli(searchData.getTime())
                .atZone(zone)
                .toLocalDate()
                .withDayOfMonth(1);
        LocalDateTime monthStart = yearMonth.atStartOfDay();
        LocalDateTime monthEnd = yearMonth
                .with(TemporalAdjusters.lastDayOfMonth())
                .atTime(23, 59, 59);

        LocalDateTime clippedStart = start.isBefore(monthStart) ? monthStart : start;
        LocalDateTime clippedEnd = end.isAfter(monthEnd) ? monthEnd : end;

        if (clippedEnd.isBefore(clippedStart)) {
            return 0.0;
        }

        if (!clippedStart.equals(start) || !clippedEnd.equals(end)) {
            return roundedHours;
        }

        if (clippedStart.toLocalDate().equals(clippedEnd.toLocalDate())) {
            double hours = Duration.between(clippedStart, clippedEnd)
                    .toMinutes() / 60.0;
            return Math.min(hours, roundedHours);
        }

        return roundedHours;
    }

    /**
     * Get the day of the month in which searchData is located
     */
    private int getDaysInMonth(Date searchData) {
        // 1. 转 LocalDate
        ZoneId zone = ZoneId.systemDefault();
        LocalDate local = Instant.ofEpochMilli(searchData.getTime())
                .atZone(zone)
                .toLocalDate();
        // 2. 用 YearMonth 取得当月天数
        YearMonth ym = YearMonth.from(local);
        return ym.lengthOfMonth();
    }

    private int countWorkingDays(Date searchData, List<Integer> workWeekDays) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate local = Instant.ofEpochMilli(searchData.getTime())
                .atZone(zone)
                .toLocalDate();

        YearMonth ym = YearMonth.from(local);
        int daysInMonth = ym.lengthOfMonth();

        int count = 0;
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate d = ym.atDay(day);
            int dow = d.getDayOfWeek().getValue();  // 1=Monday ... 7=Sunday
            if (workWeekDays.contains(dow)) {
                count++;
            }
        }
        return count;
    }

    private double sumTimeStringsAsHours(List<String> timeStrings) {
        Duration total = timeStrings.stream()
                .map(LocalTime::parse)
                .map(t -> Duration.between(LocalTime.MIDNIGHT, t))
                .reduce(Duration.ZERO, Duration::plus);

        // 拆分小时和分钟
        long hours = total.toHours();
        long minutes = total.minusHours(hours).toMinutes();

        // 转为 double，再保留两位小数
        double raw = hours + minutes / 60.0;
        return BigDecimal.valueOf(raw)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
