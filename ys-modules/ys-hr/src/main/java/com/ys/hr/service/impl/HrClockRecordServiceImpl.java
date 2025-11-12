package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.bean.BeanUtils;
import com.ys.hr.domain.*;
import com.ys.hr.mapper.*;
import com.ys.hr.service.IHrClockRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clock in RecordServiceBusiness layer processing
 *
 * @author ys
 * @date 2024-11-29
 */
@Slf4j
@Service
public class HrClockRecordServiceImpl extends ServiceImpl<HrClockRecordMapper, HrClockRecord>
        implements IHrClockRecordService {

    @Resource
    private HrAttendanceMapper hrAttendanceMapper;

    @Resource
    private HrEmployeesMapper hrEmployeesMapper;

    @Resource
    private HrEmpSchedulingMapper hrEmpSchedulingMapper;

    @Resource
    private HrSchedulingEmpMapper hrSchedulingEmpMapper;

    @Resource
    private HrSettingsMapper hrSettingsMapper;

    @Override
    public List<HrClockRecord> selectHrClockRecordList(HrClockRecord hrClockRecord) {
        return baseMapper.selectOaClockRecordList(hrClockRecord);
    }

    @Override
    public void replaceBatch(List<HrClockRecord> hrClockRecords) {
//        baseMapper.replaceBatch(hrClockRecords);
    }

    @Override
    public void addRecord(HrClockRecord clockRecord) {
        HrEmployees employees = hrEmployeesMapper.selectEmployeeByJobnumber(clockRecord.getJobnumber());
        if (ObjectUtils.isEmpty(employees)) {
            log.debug("Employee: {}, Work No:{},Punch time:{}, Employee does not exist!", clockRecord.getName(),
                    clockRecord.getJobnumber(), clockRecord.getTime());
        } else {
            clockRecord.setEnterpriseId(employees.getEnterpriseId());
            // 1. Generate Instant from second-level timestamp
            Instant instant = Instant.ofEpochSecond(clockRecord.getTime());
            // 2. Convert to ZonedDateTime according to the system time zone, and then
            // extract LocalTime
            ZoneId zone = ZoneId.systemDefault();
            // Current check-in time
            LocalTime time = instant.atZone(zone).toLocalTime();
            // 1. Convert to LocalDateTime
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, zone);
            // 3. Formatting output
            String formatted = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String mouth = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            // Query user shift schedule
            HrSchedulingEmp hrSchedulingEmp = new HrSchedulingEmp();
            hrSchedulingEmp.setUserId(employees.getUserId());
            hrSchedulingEmp.setSearchTime(mouth);
            HrSchedulingEmp schedulingEmp = new HrSchedulingEmp();
            HrSchedulingEmp schedulingEmp2 = hrSchedulingEmpMapper
                    .selectHrSchedulingEmpByDataAndUserId(hrSchedulingEmp);
            HrSettings hrSettings = hrSettingsMapper.selectHrSettingsByEid(employees.getEnterpriseId());
            if (schedulingEmp2 != null) {
                schedulingEmp = schedulingEmp2;
            } else {
                if (hrSettings != null) {
                    HrEmpScheduling hrEmpScheduling = hrEmpSchedulingMapper
                            .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
                    if (ObjectUtils.isNotEmpty(hrEmpScheduling)) {
                        BeanUtils.copyProperties(hrEmpScheduling, schedulingEmp);
                    }
                }
            }
            if (ObjectUtils.isEmpty(schedulingEmp)) {
                log.debug(
                        "Employee: {}, Work No:{},Punch time:{}, There is no shift information for the current employee",
                        clockRecord.getName(), clockRecord.getJobnumber(), clockRecord.getTime());
            } else {
                // Default work check-in time
                LocalTime defaultStartTime = schedulingEmp.getDefaultStartTime();
                // Default off-duty check-in time
                LocalTime defaultEndTime = schedulingEmp.getDefaultEndTime();
                HrAttendance hrAttendance = hrAttendanceMapper.selectAttendanceByUserIdAndDate(employees.getUserId(),
                        formatted);
                if (ObjectUtils.isEmpty(hrAttendance)) {
                    hrAttendance = new HrAttendance();
                    hrAttendance.setAttendanceId(null);
                    hrAttendance.setUserId(employees.getUserId());
                    hrAttendance.setNickName(employees.getFullName());
                    hrAttendance.setAttendanceStatus("1");
                    hrAttendance.setEarlyTime("00:00:00");
                    hrAttendance.setOverTime("00:00:00");
                    // Only take year, month and day (converted to LocalDate)
                    LocalDate localDate = ldt.toLocalDate();
                    hrAttendance
                            .setAttendanceTime(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    hrAttendance.setCreateTime(DateUtils.getNowDate());
                    // Determine if late
                    if (time.isAfter(defaultStartTime)) {
                        Duration diff = Duration.between(defaultStartTime, time);
                        hrAttendance.setLateTime(formatDuration(diff.getSeconds()));
                        hrAttendance.setPresentStatus("[1]");
                    }
                    hrAttendance.setCheckIn(time);
                    hrAttendance.setWorkTime(String.valueOf(calculateWorkHours(defaultStartTime, defaultEndTime)));
                    hrAttendance.setMyWorkTime("0.0");
                    hrAttendance.setEnterpriseId(employees.getEnterpriseId());
                    hrAttendanceMapper.insert(hrAttendance);
                } else {
                    // First check-in time
                    LocalTime checkIn = hrAttendance.getCheckIn();
                    hrAttendance.setCheckOut(time);
                    ArrayList<Character> status = new ArrayList<>();
                    // Determine if late
                    if (checkIn.isAfter(defaultStartTime)) {
                        Duration diff = Duration.between(defaultStartTime, checkIn);
                        hrAttendance.setLateTime(formatDuration(diff.getSeconds()));
                        status.add('1');
                    }
                    // Determine if leaving early
                    Duration diff = Duration.between(defaultEndTime, time);
                    long seconds = diff.getSeconds();
                    // Early leave: diff is negative
                    if (seconds < 0) {
                        long earlySeconds = -seconds;
                        status.add('2');
                        hrAttendance.setEarlyTime(formatDuration(earlySeconds));
                    }
                    // Overtime: diff is positive
                    if (seconds > 0) {
                        status.add('3');
                        hrAttendance.setEarlyTime(formatDuration(seconds));
                    }
                    hrAttendance.setPresentStatus(status.toString());
                    hrAttendance.setUpdateTime(DateUtils.getNowDate());
                    hrAttendance.setMyWorkTime(String.valueOf(calculateWorkHours(checkIn, time)));
                    hrAttendanceMapper.updateById(hrAttendance);
                }
                baseMapper.saveData(clockRecord);
                log.debug("Employee: {}, Work No:{},Punch time:{}, add record success", clockRecord.getName(),
                        clockRecord.getJobnumber(), clockRecord.getTime());
            }

        }
    }

    /**
     * Format seconds into "HH:mm:ss"
     */
    private String formatDuration(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Calculate work hours, return hours with 1 decimal place
     */
    private double calculateWorkHours(LocalTime startTime, LocalTime endTime) {
        Duration duration = Duration.between(startTime, endTime);

        // Get total seconds and convert to hours
        double hours = duration.getSeconds() / 3600.0;

        // Keep 1 decimal place (rounding)
        BigDecimal bd = new BigDecimal(hours).setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
