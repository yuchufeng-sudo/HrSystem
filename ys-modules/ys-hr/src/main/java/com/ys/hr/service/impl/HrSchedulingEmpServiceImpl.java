package com.ys.hr.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.utils.bean.BeanUtils;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmpScheduling;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrSettings;
import com.ys.hr.mapper.HrEmpSchedulingMapper;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.hr.mapper.HrSettingsMapper;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import org.apache.catalina.security.SecurityUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrSchedulingEmpMapper;
import com.ys.hr.domain.HrSchedulingEmp;
import com.ys.hr.service.IHrSchedulingEmpService;
import com.ys.common.core.utils.DateUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Employee scheduling Service Implementation
 *
 * @author ys
 * @date 2025-06-08
 */
@Service
public class HrSchedulingEmpServiceImpl extends ServiceImpl<HrSchedulingEmpMapper, HrSchedulingEmp> implements IHrSchedulingEmpService {

    @Autowired
    private HrSettingsMapper hrSettingsMapper;

    @Autowired
    private HrEmpSchedulingMapper hrEmpSchedulingMapper;

    @Resource
    private RemoteMessageService remoteMessageService;

    @Resource
    private HrEmployeesMapper hrEmployeesMapper;

    /**
     * Query Employee scheduling
     *
     * @param schedulingEmpId Employee scheduling primary key
     * @return Employee scheduling
     */
    @Override
    public HrSchedulingEmp selectHrSchedulingEmpBySchedulingEmpId(String schedulingEmpId) {
        return baseMapper.selectHrSchedulingEmpBySchedulingEmpId(schedulingEmpId);
    }

    /**
     * Query Employee scheduling list
     *
     * @param hrSchedulingEmp Employee scheduling
     * @return Employee scheduling
     */
    @Override
    public List<HrSchedulingEmp> selectHrSchedulingEmpList(HrSchedulingEmp hrSchedulingEmp) {
        return baseMapper.selectHrSchedulingEmpList(hrSchedulingEmp);
    }

    /**
     * Add Employee scheduling
     *
     * @param hrSchedulingEmp Employee scheduling
     * @return Result
     */
    @Override
    @Transactional
    public int insertHrSchedulingEmp(HrSchedulingEmp hrSchedulingEmp) {
        HrSchedulingEmp schedulingEmp = baseMapper.selectHrSchedulingEmpInfo(hrSchedulingEmp);
        if (!ObjectUtils.isEmpty(schedulingEmp)) {
            throw new RuntimeException("The shift already exists, please select again");
        }
        hrSchedulingEmp.setCreateTime(DateUtils.getNowDate());
        int i = baseMapper.insert(hrSchedulingEmp);
        if (i > 0) {
            AjaxResult info = remoteMessageService.getInfo(hrSchedulingEmp.getUserId(), SecurityConstants.INNER);
            Map<String,String> setting = (Map<String, String>) info.get("data");
            String shiftScheduleUpdate = setting.get("shiftScheduleUpdate");
            if("1".equals(shiftScheduleUpdate)){
                HrEmployees Leader = hrEmployeesMapper.selectHrEmployeesByUserId(SecurityUtils.getUserId());
                HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByUserId(hrSchedulingEmp.getUserId());
                Date schedulingData = hrSchedulingEmp.getSchedulingData();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                LocalDate schedulingData2 = schedulingData.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String schedulingDataStr = schedulingData2.format(formatter);
                SysMessage sysMessage = new SysMessage();
                sysMessage.setMessageRecipient(hrSchedulingEmp.getUserId());
                sysMessage.setMessageStatus("0");
                sysMessage.setMessageType(7);
                sysMessage.setCreateTime(DateUtils.getNowDate());
                Map<String, Object> map1 = new HashMap<>();
                Map<String, Object> map2 = new HashMap<>();
                map1.put("arranger", Leader.getFullName());
                map1.put("employee", hrEmployees.getFullName());
                map1.put("month", schedulingDataStr);
                sysMessage.setMap1(map1);
                sysMessage.setMap2(map2);
                remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
            }
        }
        return i;
    }

    /**
     * Update Employee scheduling
     *
     * @param hrSchedulingEmp Employee scheduling
     * @return Result
     */
    @Override
    @Transactional
    public int updateHrSchedulingEmp(HrSchedulingEmp hrSchedulingEmp) {
        HrSchedulingEmp schedulingEmp = baseMapper.selectHrSchedulingEmpInfo(hrSchedulingEmp);
        if (!ObjectUtils.isEmpty(schedulingEmp) && !schedulingEmp.getSchedulingEmpId().equals(hrSchedulingEmp.getSchedulingEmpId())) {
            throw new RuntimeException("The shift already exists, please select again");
        }
        hrSchedulingEmp.setUpdateTime(DateUtils.getNowDate());
        int i = baseMapper.updateById(hrSchedulingEmp);

        if (i > 0) {
            AjaxResult info = remoteMessageService.getInfo(hrSchedulingEmp.getUserId(),SecurityConstants.INNER);
            Map<String,String> setting = (Map<String, String>) info.get("data");
            String shiftScheduleUpdate = setting.get("shiftScheduleUpdate");
            if("1".equals(shiftScheduleUpdate)){
                HrEmployees Leader = hrEmployeesMapper.selectHrEmployeesByUserId(SecurityUtils.getUserId());
                HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByUserId(hrSchedulingEmp.getUserId());
                Date schedulingData = hrSchedulingEmp.getSchedulingData();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                LocalDate schedulingData2 = schedulingData.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String schedulingDataStr = schedulingData2.format(formatter);
                SysMessage sysMessage = new SysMessage();
                sysMessage.setMessageRecipient(hrSchedulingEmp.getUserId());
                sysMessage.setMessageStatus("0");
                sysMessage.setMessageType(7);
                sysMessage.setCreateTime(DateUtils.getNowDate());
                Map<String, Object> map1 = new HashMap<>();
                Map<String, Object> map2 = new HashMap<>();
                if(ObjectUtils.isNotEmpty( Leader)){
                    map1.put("arranger", Leader.getFullName());
                }else{
                    map1.put("arranger", "Unknown");
                }
                map1.put("employee", hrEmployees.getFullName());
                map1.put("month", schedulingDataStr);
                sysMessage.setMap1(map1);
                sysMessage.setMap2(map2);
                remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
            }
        }
        return i;
    }

    @Override
    public HrSchedulingEmp getUserInfo(Long userId) {
        HrSchedulingEmp hrSchedulingEmp = new HrSchedulingEmp();
        hrSchedulingEmp.setUserId(SecurityUtils.getUserId());
        // Obtain the first day of the current year and month
        LocalDate firstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        hrSchedulingEmp.setSearchTime(firstDay.toString());
        List<HrSchedulingEmp> hrSchedulingEmps = baseMapper.selectHrSchedulingEmpByUserId(hrSchedulingEmp);
        if (ObjectUtils.isNotEmpty(hrSchedulingEmps)) {
            double hours = calculateHoursBetween(hrSchedulingEmps.get(0).getDefaultStartTime(),
                    hrSchedulingEmps.get(0).getDefaultEndTime());
            double restHours = calculateHoursBetween(hrSchedulingEmps.get(0).getRestStartTime(),
                    hrSchedulingEmps.get(0).getRestEndTime());
            hours = hours - restHours;
            // Round to one decimal place
            double roundedHours = Math.round(hours * 10) / 10.0;
            hrSchedulingEmps.get(0).setWorkTime(roundedHours);
            return hrSchedulingEmps.get(0);
        } else {
            HrSettings hrSettings = hrSettingsMapper.selectHrSettingsByEid(SecurityUtils.getUserEnterpriseId());
            if (ObjectUtils.isNotEmpty(hrSettings)) {
                HrEmpScheduling hrEmpScheduling = hrEmpSchedulingMapper
                        .selectHrEmpSchedulingBySchedulingId(hrSettings.getSchedulingId());
                HrSchedulingEmp schedulingEmp = new HrSchedulingEmp();
                double hours = 0.0;
                double restHours = 0.0;
                if (ObjectUtils.isNotEmpty(hrEmpScheduling)) {
                    if (ObjectUtils.isNotEmpty(hrEmpScheduling.getDefaultStartTime()) && ObjectUtils.isNotEmpty(hrEmpScheduling.getDefaultEndTime())) {
                        BeanUtils.copyProperties(hrEmpScheduling, schedulingEmp);
                        hours = calculateHoursBetween(hrEmpScheduling.getDefaultStartTime(),
                                hrEmpScheduling.getDefaultEndTime());
                    }
                    if (ObjectUtils.isNotEmpty(hrEmpScheduling.getRestStartTime()) && ObjectUtils.isNotEmpty(hrEmpScheduling.getRestEndTime())) {
                        restHours = calculateHoursBetween(hrEmpScheduling.getRestStartTime(),
                                hrEmpScheduling.getRestEndTime());
                    }
                    hours = hours - restHours;
                    // Round to one decimal place
                    double roundedHours = Math.round(hours * 10) / 10.0;
                    schedulingEmp.setWorkTime(roundedHours);
                    return schedulingEmp;
                }
            }
            return new HrSchedulingEmp();
        }
    }

    public static double calculateHoursBetween(LocalTime start, LocalTime end) {
        // Check if it crosses midnight (end time is earlier than start time)
        if (end.isBefore(start)) {
            // Calculate the duration from the start time to midnight
            Duration firstHalf = Duration.between(start, LocalTime.MAX).plusSeconds(1);
            // Calculate the duration from midnight to the next day's end time
            Duration secondHalf = Duration.between(LocalTime.MIN, end);
            // Combine the two time segments
            return firstHalf.plus(secondHalf).toMinutes() / 60.0;
        } else {
            // Direct calculation without crossing midnight
            return Duration.between(start, end).toMinutes() / 60.0;
        }
    }
}
