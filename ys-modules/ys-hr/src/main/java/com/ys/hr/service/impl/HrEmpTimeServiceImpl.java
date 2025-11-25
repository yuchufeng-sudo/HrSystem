package com.ys.hr.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.mapper.HrEmployeesMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrEmpTimeMapper;
import com.ys.hr.domain.HrEmpTime;
import com.ys.hr.service.IHrEmpTimeService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * Employee time Service Implementation
 *
 * @author ys
 * @date 2025-06-03
 */
@Service
public class HrEmpTimeServiceImpl extends ServiceImpl<HrEmpTimeMapper, HrEmpTime> implements IHrEmpTimeService {

    @Autowired
    private HrEmployeesMapper hrEmployeesMapper;

    /**
     * Query Employee time
     *
     * @param empTimeId Employee time primary key
     * @return Employee time
     */
    @Override
    public HrEmpTime selectHrEmpTimeByEmpTimeId(String empTimeId) {
        return baseMapper.selectHrEmpTimeByEmpTimeId(empTimeId);
    }

    /**
     * Query Employee time list
     *
     * @param hrEmpTime Employee time
     * @return Employee time
     */
    @Override
    public List<HrEmpTime> selectHrEmpTimeList(HrEmpTime hrEmpTime) {
        return baseMapper.selectHrEmpTimeList(hrEmpTime);
    }

    /**
     * Add Employee time
     *
     * @param hrEmpTime Employee time
     * @return Result
     */
    @Override
    public int insertHrEmpTime(HrEmpTime hrEmpTime) {
        return baseMapper.insert(hrEmpTime);
    }

    /**
     * Update Employee time
     *
     * @param hrEmpTime Employee time
     * @return Result
     */
    @Override
    public int updateHrEmpTime(HrEmpTime hrEmpTime) {
        return baseMapper.updateById(hrEmpTime);
    }

    /**
     * Batch delete Employee time
     *
     * @param empTimeIds Employee time primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrEmpTimeByEmpTimeIds(String[] empTimeIds) {
        return baseMapper.deleteBatchIds(Arrays.asList(empTimeIds));
    }

    /**
     * Delete Employee time information
     *
     * @param empTimeId Employee time primary key
     * @return Result
     */
    @Override
    public int deleteHrEmpTimeByEmpTimeId(String empTimeId) {
        return baseMapper.deleteById(empTimeId);
    }

    @Override
    public HrEmpTime getTimeByUserId(HrEmpTime hrEmpTime) {
        HrEmpTime timeByUserId = baseMapper.getTimeByUserId(hrEmpTime);
        HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByUserId(hrEmpTime.getUserId());
        if (ObjectUtils.isNotEmpty(hrEmployees) && ObjectUtils.isNotEmpty(timeByUserId)) {
            if ("2".equals(hrEmployees.getEmploymentType())) {
                timeByUserId.setBaseSalary(0L);
                timeByUserId.setPresentCost(
                        BigDecimal.valueOf(Double.valueOf(hrEmployees.getHoursPerPeriod()) * (timeByUserId.getPresentHours() - timeByUserId.getOverHours()))
                                .setScale(2, RoundingMode.HALF_UP)
                                .doubleValue()
                        );
                timeByUserId.setOverTimeCost(
                        BigDecimal.valueOf(Double.valueOf(hrEmployees.getHoursPerPeriod()) * timeByUserId.getOverHours())
                                .setScale(2, RoundingMode.HALF_UP)
                                .doubleValue()
                );
                timeByUserId.setBeforeTaxSalary(timeByUserId.getPresentCost() + timeByUserId.getOverTimeCost());
                timeByUserId.setSocialSecurityCost(0.0);
                timeByUserId.setComInsuIdCost(0.0);
                timeByUserId.setAccuFundIdCost(0.0);
            }else {
                extracted(hrEmpTime, timeByUserId, hrEmployees);
            }
        }
        return timeByUserId;
    }

    private static void extracted(HrEmpTime hrEmpTime, HrEmpTime timeByUserId, HrEmployees hrEmployees) {
        timeByUserId.setBaseSalary(hrEmployees.getBaseSalary());
        timeByUserId.setSocialSecurityCost(hrEmployees.getSocialSecurityCost());
        timeByUserId.setComInsuIdCost(hrEmployees.getComInsuIdCost());
        timeByUserId.setAccuFundIdCost(hrEmployees.getAccuFundIdCost());
        // Calculate the salary: divide the basic salary by the number of working days
        // in the current month, multiply by the effective working time, and finally
        // subtract penalties such as lateness and early leaving.
        // Basic salary
        Long baseSalary = timeByUserId.getBaseSalary();
        // Working time, in hours
        Double workHours = hrEmpTime.getWorkHours();
        Double hoursSalary = 0.0;
        if(ObjectUtils.isNotEmpty(baseSalary) && ObjectUtils.isNotEmpty(workHours)){
            //Hourly salary
            hoursSalary = baseSalary / workHours;
        }
        // Overtime salary
        Double overHours = hrEmpTime.getOverHours();
        // Overtime pay
        Double overSalary = hoursSalary * overHours;
        timeByUserId.setOverTimeCost(BigDecimal.valueOf(overSalary)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue());
        // Abesent salary
        Double absentHours = hrEmpTime.getAbsentHours();
        Double leaveHours = hrEmpTime.getLeaveHours();
        Double lateHours = hrEmpTime.getLateHours();
        Double earlyHours = hrEmpTime.getEarlyHours();
        Double badHours = absentHours + leaveHours + lateHours + earlyHours;
        // Abesent pay
        Double badSalsy = hoursSalary * badHours;
        timeByUserId.setAbnormalTimeCost(BigDecimal.valueOf(badSalsy)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue());
        // present
        //Effective working time
        Double presentHours = hrEmpTime.getPresentHours();

        // Normal attendance salary
        Double presentSalary = presentHours * hoursSalary;
        timeByUserId.setPresentCost(BigDecimal.valueOf(presentSalary - overSalary + badSalsy)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue());
        // Pre-tax salary
        timeByUserId.setBeforeTaxSalary( BigDecimal.valueOf(presentSalary)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue());
    }

    @Override
    @Transactional
    public int insertHrEmpTimes(List<HrEmpTime> hrEmpTimes) {
        int i = 0;
        List<HrEmpTime> hrEmpTimeList = new ArrayList<>();
        for (HrEmpTime hrEmpTime : hrEmpTimes) {
            HrEmpTime timeByUserId = baseMapper.getTimeByUserId(hrEmpTime);
            if (ObjectUtils.isEmpty(timeByUserId)) {
                hrEmpTimeList.add(hrEmpTime);
                i++;
            }
        }
        saveBatch(hrEmpTimeList);
        return i;
    }
}
