package com.ys.hr.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.hr.domain.HrTargets;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrTargetEmployeeMapper;
import com.ys.hr.domain.HrTargetEmployee;
import com.ys.hr.service.IHrTargetEmployeeService;
import com.ys.common.core.utils.DateUtils;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Target allocation of employees Service Implementation
 *
 * @author ys
 * @date 2025-06-23
 */
@Service
public class HrTargetEmployeeServiceImpl extends ServiceImpl<HrTargetEmployeeMapper, HrTargetEmployee> implements IHrTargetEmployeeService
{

    /**
     * Query Target allocation of employees
     *
     * @param id Target allocation of employees primary key
     * @return Target allocation of employees
     */
    @Override
    public HrTargetEmployee selectHrTargetEmployeeById(Long id)
    {
        return baseMapper.selectHrTargetEmployeeById(id);
    }

    /**
     * Query Target allocation of employees list
     *
     * @param hrTargetEmployee Target allocation of employees
     * @return Target allocation of employees
     */
    @Override
    public List<HrTargetEmployee> selectHrTargetEmployeeList(HrTargetEmployee hrTargetEmployee)
    {
        return baseMapper.selectHrTargetEmployeeList(hrTargetEmployee);
    }

    /**
     * Add Target allocation of employees
     *
     * @param hrTargetEmployee Target allocation of employees
     * @return Result
     */
    @Override
    public int insertHrTargetEmployee(HrTargetEmployee hrTargetEmployee)
    {
        hrTargetEmployee.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrTargetEmployee);
    }

    /**
     * Update Target allocation of employees
     *
     * @param hrTargetEmployee Target allocation of employees
     * @return Result
     */
    @Override
    public int updateHrTargetEmployee(HrTargetEmployee hrTargetEmployee)
    {
        hrTargetEmployee.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrTargetEmployee);
    }

    @Override
    public void updateByEmployeeId(Long employeeId) {
        baseMapper.updateByEmployeeId(employeeId);
    }

    @Override
    public HrTargetEmployee selectExecutionTarget(Long employeeId) {
        return baseMapper.selectExecutionTarget(employeeId);
    }

    @Override
    public int stopPause(HrTargetEmployee hrTargetEmployee) {
        hrTargetEmployee.setIsStart("3");
        Date executionTime = hrTargetEmployee.getExecutionTime();
        Long executionSecond = ObjectUtils.isNotEmpty(hrTargetEmployee.getExecutionSecond()) ? hrTargetEmployee.getExecutionSecond() : 0;
        Date nowDate = hrTargetEmployee.getStopTime();
        String s = calculateExecutionDuration(executionTime, nowDate, executionSecond);
        Long l = calculateExecutionDurationSeconds(executionTime, nowDate);
        hrTargetEmployee.setCompleteTime(s);
        hrTargetEmployee.setExecutionSecond(executionSecond + l);
        hrTargetEmployee.setExecutionTime(null);
        return baseMapper.updateById(hrTargetEmployee);
    }

    @Override
    public int finish(HrTargetEmployee hrTargetEmployee) {
        return baseMapper.finish(hrTargetEmployee);
    }

    @Override
    public List<HrTargetEmployee> selectEmployeeTargets(HrTargets hrTargets) {
        return baseMapper.selectEmployeeTargets(hrTargets);
    }

    @Override
    public List<HrTargetEmployee> selectEmployeeTargetsByAdmin(HrTargets hrTargets) {
        return baseMapper.selectEmployeeTargetsByAdmin(hrTargets);
    }

    @Override
    public void saveBatchEmployees(List<HrTargetEmployee> list) {
        baseMapper.saveBatchEmployees(list);
    }

    public String calculateExecutionDuration(Date executionTime, Date endTime, Long executionSecond) {
        long totalMillis = endTime.getTime() - executionTime.getTime();

        if (totalMillis < 0) totalMillis = 0; // 避免负值

        long seconds = (totalMillis / 1000) + executionSecond;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    public Long calculateExecutionDurationSeconds(Date executionTime, Date endTime) {
        long totalMillis = endTime.getTime() - executionTime.getTime();
        if (totalMillis < 0) totalMillis = 0; // 避免负值
        long seconds = totalMillis / 1000;
        return seconds;
    }
}
