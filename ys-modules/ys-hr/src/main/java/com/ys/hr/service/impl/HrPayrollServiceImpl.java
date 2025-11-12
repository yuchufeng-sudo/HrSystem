package com.ys.hr.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.bean.BeanUtils;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrAttendance;
import com.ys.hr.domain.HrEmpTime;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.vo.AnnualPayrollStatisticsVo;
import com.ys.hr.domain.vo.payRollVo;
import com.ys.hr.mapper.HrEmpTimeMapper;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrPayrollMapper;
import com.ys.hr.domain.HrPayroll;
import com.ys.hr.service.IHrPayrollService;
import com.ys.common.core.utils.DateUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * EMPLOYEE SALARY Service Implementation
 *
 * @author ys
 * @date 2025-05-30
 */
@Service
public class HrPayrollServiceImpl extends ServiceImpl<HrPayrollMapper, HrPayroll> implements IHrPayrollService {

    @Autowired
    private HrEmpTimeMapper hrEmpTimeMapper;

    @Autowired
    private HrEmpTimeServiceImpl hrEmpTimeServiceImpl;

    @Autowired
    private HrEmployeesMapper hrEmployeesMapper;

    @Resource
    private RemoteMessageService remoteMessageService;

    /**
     * Query EMPLOYEE SALARY
     *
     * @param payrollId EMPLOYEE SALARY primary key
     * @return EMPLOYEE SALARY
     */
    @Override
    public HrPayroll selectHrPayrollByPayrollId(String payrollId) {
        return baseMapper.selectHrPayrollByPayrollId(payrollId);
    }

    /**
     * Query EMPLOYEE SALARY list
     *
     * @param hrPayroll EMPLOYEE SALARY
     * @return EMPLOYEE SALARY
     */
    @Override
    public List<HrPayroll> selectHrPayrollList(HrPayroll hrPayroll) {
        return baseMapper.selectHrPayrollList(hrPayroll);
    }

    /**
     * Add EMPLOYEE SALARY
     *
     * @param hrPayroll EMPLOYEE SALARY
     * @return Result
     */
    @Override
    public int insertHrPayroll(HrPayroll hrPayroll) {
        HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByUserId(hrPayroll.getUserId());
        if(ObjectUtils.isNotEmpty(hrEmployees.getPayrollId())){
            hrPayroll.setPayrollNum(hrEmployees.getPayrollId());
        }
        hrPayroll.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        hrPayroll.setCreateTime(DateUtils.getNowDate());
        // Calculate the salary: divide the basic salary by the number of working days
        // in the current month, multiply by the effective working time, and finally
        // subtract penalties such as lateness and early leaving.
        // Basic salary
        Long baseSalary = hrPayroll.getBaseSalary();
        // Working time, in hours
        Double workHours = hrPayroll.getWorkHours();
        // Effective working time
        Double presentHours = hrPayroll.getPresentHours();
        // Overtime salary
        Double overHours = hrPayroll.getOverHours();
        // Overtime pay
        Double overSalary = hrPayroll.getOverTimeCost();
        hrPayroll.setOverTimeCost(overSalary);

        // Salary deduction for lateness and early leaving
        Double badSalary = hrPayroll.getAbnormalTimeCost();
        hrPayroll.setAbnormalTimeCost(badSalary);

        // Normal attendance salary
        Double presentSalary = hrPayroll.getPresentCost();
        hrPayroll.setPresentCost(presentSalary);

        // Pre-tax salary
        Double beforeTaxSalary = hrPayroll.getBeforeTaxSalary();
        hrPayroll.setBeforeTaxSalary(beforeTaxSalary);

        // After-tax salary
        // Social security cost
        Double socialSecurityCost = 0.0;
        if (ObjectUtils.isNotEmpty(hrPayroll.getSocialSecurityCost())) {
            socialSecurityCost = hrPayroll.getSocialSecurityCost();
        }
        // Commercial insurance cost
        Double comInsuIdCost = 0.0;
        if (ObjectUtils.isNotEmpty(hrPayroll.getComInsuIdCost())) {
            comInsuIdCost = hrPayroll.getComInsuIdCost();
        }
        // Housing provident fund cost
        Double accuFundIdCost = 0.0;
        if (ObjectUtils.isNotEmpty(hrPayroll.getAccuFundIdCost())) {
            accuFundIdCost = hrPayroll.getAccuFundIdCost();
        }
        // Additional costs
        Double additionalCosts = 0.0;
        if (ObjectUtils.isNotEmpty(hrPayroll.getAdditionalCosts())) {
            additionalCosts = hrPayroll.getAdditionalCosts();
        }
        hrPayroll.setAfterTaxSalary(
                beforeTaxSalary - socialSecurityCost - comInsuIdCost - accuFundIdCost + additionalCosts);
        hrPayroll.setPayrollStatus('1');
        hrPayroll.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        int insert = baseMapper.insert(hrPayroll);
        if (insert > 0) {
            HrEmpTime hrEmpTime = hrEmpTimeMapper.selectHrEmpTimeByEmpTimeId(hrPayroll.getEmpTimeId());
                if (ObjectUtils.isNotEmpty(hrEmpTime)) {
                hrEmpTime.setPayrollStatus("1");
                hrEmpTime.setUpdateTime(DateUtils.getNowDate());
                hrEmpTime.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
                hrEmpTimeMapper.updateById(hrEmpTime);
            }
        }
        return insert;
    }

    @Override
    public int insertHrPayrolls(List<HrPayroll> hrPayrolls) {
        int i = 0;
        for (HrPayroll hrPayroll : hrPayrolls) {
            HrEmpTime hrEmpTime = new HrEmpTime();
            BeanUtils.copyProperties(hrPayroll, hrEmpTime);
            HrEmpTime timeByUserId = hrEmpTimeServiceImpl.getTimeByUserId(hrEmpTime);
            BeanUtils.copyProperties(timeByUserId, hrPayroll);
            hrPayroll.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
            hrPayroll.setCreateTime(DateUtils.getNowDate());
            // Calculate the salary: divide the basic salary by the number of working days
            // in the current month, multiply by the effective working time, and finally
            // subtract penalties such as lateness and early leaving.
            // Basic salary
            Long baseSalary = hrPayroll.getBaseSalary();
            // Working time, in hours
            Double workHours = hrPayroll.getWorkHours();
            Double hoursSalary = 0.0;
            // Hourly salary
            if(ObjectUtils.isNotEmpty(baseSalary) && ObjectUtils.isNotEmpty(workHours)){
                hoursSalary = baseSalary / workHours;
            }
            // Effective working time
            Double presentHours = hrPayroll.getPresentHours();
            // Normal attendance salary
            Double presentSalary = presentHours * hoursSalary;
            hrPayroll.setPresentCost(presentSalary);
            // Overtime hours
            Double overHours = hrPayroll.getOverHours();
            // Overtime pay
            Double overSalary = hoursSalary * overHours;
            hrPayroll.setOverTimeCost(overSalary);
            // Hours of lateness, early leaving, leave and absence
            Double badHours = hrPayroll.getLateHours() + hrPayroll.getEarlyHours() + hrPayroll.getLeaveHours()
                    + hrPayroll.getAbsentHours();
            // Deduction for lateness and early leaving
            Double badSalary = hoursSalary * badHours;
            hrPayroll.setAbnormalTimeCost(badSalary);
            // Additional costs
            Double additionalCosts = 0.0;
            if (ObjectUtils.isNotEmpty(hrPayroll.getAdditionalCosts())) {
                additionalCosts = hrPayroll.getAdditionalCosts();
            }
            // Pre-tax salary
            hrPayroll.setBeforeTaxSalary(presentSalary + overSalary - badSalary + additionalCosts);
            Double beforeTaxSalary = hrPayroll.getBeforeTaxSalary();

            // After-tax salary
            // Social security cost
            Double socialSecurityCost = 0.0;
            if (ObjectUtils.isNotEmpty(hrPayroll.getSocialSecurityCost())) {
                socialSecurityCost = hrPayroll.getSocialSecurityCost();
            }
            // Commercial insurance cost
            Double comInsuIdCost = 0.0;
            if (ObjectUtils.isNotEmpty(hrPayroll.getComInsuIdCost())) {
                comInsuIdCost = hrPayroll.getComInsuIdCost();
            }
            // Housing provident fund cost
            Double accuFundIdCost = 0.0;
            if (ObjectUtils.isNotEmpty(hrPayroll.getAccuFundIdCost())) {
                accuFundIdCost = hrPayroll.getAccuFundIdCost();
            }

            hrPayroll.setAfterTaxSalary(beforeTaxSalary - socialSecurityCost - comInsuIdCost - accuFundIdCost);
            hrPayroll.setPayrollStatus('1');
            hrPayroll.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
            baseMapper.insert(hrPayroll);
            i++;
        }
        return i;
    }

    /**
     * Update EMPLOYEE SALARY
     *
     * @param hrPayroll EMPLOYEE SALARY
     * @return Result
     */
    @Override
    public int updateHrPayroll(HrPayroll hrPayroll) {
        hrPayroll.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrPayroll);
    }

    /**
     * Batch delete EMPLOYEE SALARY
     *
     * @param payrollIds EMPLOYEE SALARY primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrPayrollByPayrollIds(String[] payrollIds) {
        return baseMapper.deleteBatchIds(Arrays.asList(payrollIds));
    }

    /**
     * Delete EMPLOYEE SALARY information
     *
     * @param payrollId EMPLOYEE SALARY primary key
     * @return Result
     */
    @Override
    public int deleteHrPayrollByPayrollId(String payrollId) {
        return baseMapper.deleteById(payrollId);
    }

    @Override
    public HrPayroll getPayRollByUserId(HrPayroll hrPayroll) {
        return baseMapper.getPayRollByUserId(hrPayroll);
    }

    @Override
    public int updataByIds(List<String> list) {
        for (String payRollId : list) {
            HrPayroll hrPayroll = baseMapper.selectHrPayrollByPayrollId(payRollId);
            HrEmpTime hrEmpTime = new HrEmpTime();
            hrEmpTime.setEmpTimeId(hrPayroll.getEmpTimeId());
            hrEmpTime.setPayrollStatus("2");
            int i = hrEmpTimeMapper.updateById(hrEmpTime);
            if(i>0){
                AjaxResult info = remoteMessageService.getInfo(hrPayroll.getUserId(),SecurityConstants.INNER);
                Map<String,String> setting = (Map<String, String>) info.get("data");
                String salaryAndBenefitsNotifications = setting.get("salaryAndBenefitsNotifications");
                if("1".equals(salaryAndBenefitsNotifications)){
                    SysMessage sysMessage = new SysMessage();
                    sysMessage.setMessageRecipient(hrPayroll.getUserId());
                    sysMessage.setMessageStatus("0");
                    sysMessage.setMessageType(8);
                    sysMessage.setCreateTime(DateUtils.getNowDate());
                    Date payrollData = hrPayroll.getPayrollData();
                    Date hireData = hrPayroll.getHireData();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String hireData2 = sdf.format(hireData);
                    String payrollData2 = sdf.format(payrollData);
                    Map<String, Object> map1 = new HashMap<>();
                    Map<String, Object> map2 = new HashMap<>();
                    map1.put("name", hrPayroll.getFullName());
                    map1.put("payrollData", payrollData2);
                    map1.put("hireData", hireData2);
                    sysMessage.setMap1(map1);
                    sysMessage.setMap2(map2);
                    remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
                }
            }
        }
        return baseMapper.updataByIds(list);
    }

    @Override
    public Map<String, Object> getPayrollsCoutByHr(HrAttendance hrAttendance) {
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
        hrAttendance.setParams(timeMap);
        Map<String, Object> monthlyData = baseMapper.getPayrollsCoutByHr(hrAttendance);
        BigDecimal LastMonthTotal = (BigDecimal) monthlyData.get("total_after_tax");
        Double LastMonthHolidays = (Double) monthlyData.get("total_holidays");
        Double LastMonthPresent = (Double) monthlyData.get("total_present");
        BigDecimal LastMonthAbnormal = (BigDecimal) monthlyData.get("total_abnormal");
        map.put("LastMonthTotal", LastMonthTotal);
        map.put("LastMonthHolidays", LastMonthHolidays);
        map.put("LastMonthPresent", LastMonthPresent);
        map.put("LastMonthAbnormal", LastMonthAbnormal);

        // OBTAIN this month's data
        YearMonth currentYearMonth = YearMonth.from(today);
        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth();
        timeMap.put("beginCreateTime", firstDayOfMonth.format(formatter));
        timeMap.put("endCreateTime", lastDayOfMonth.format(formatter));
        hrAttendance.setParams(timeMap);
        Map<String, Object> NowMonthMap = baseMapper.getPayrollsCoutByHr(hrAttendance);
        BigDecimal NowMonthTotal = (BigDecimal) NowMonthMap.get("total_after_tax");
        Double NowMonthHolidays = (Double) NowMonthMap.get("total_holidays");
        Double NowMonthPresent = (Double) NowMonthMap.get("total_present");
        BigDecimal NowMonthAbnormal = (BigDecimal) NowMonthMap.get("total_abnormal");
        map.put("NowMonthTotal", NowMonthTotal);
        map.put("NowMonthHolidays", NowMonthHolidays);
        map.put("NowMonthPresent", NowMonthPresent);
        map.put("NowMonthAbnormal", NowMonthAbnormal);
        return map;
    }

    @Override
    public List<AnnualPayrollStatisticsVo> selectAnnualPayrollStatisticsList(HrPayroll hrPayroll) {
        // Verify year parameters.
        if (StringUtils.isEmpty(hrPayroll.getSearchTime())) {
            throw new ServiceException("The year parameter cannot be empty.");
        }

        return baseMapper.selectAnnualPayrollStatisticsList(
                hrPayroll.getEnterpriseId(),
                hrPayroll.getSearchTime());
    }

    @Override
    public Map<String, Object> getPayrollsCoutByEmp(HrAttendance hrAttendance) {
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
        hrAttendance.setParams(timeMap);
        Map<String, Object> monthlyData = baseMapper.getPayrollsCoutByHr(hrAttendance);
        BigDecimal LastMonthTotal = (BigDecimal) monthlyData.get("total_after_tax");
        Double LastMonthHolidays = (Double) monthlyData.get("total_holidays");
        Double LastMonthPresent = (Double) monthlyData.get("total_present");
        BigDecimal LastMonthAbnormal = (BigDecimal) monthlyData.get("total_abnormal");
        map.put("LastMonthTotal", LastMonthTotal);
        map.put("LastMonthHolidays", LastMonthHolidays);
        map.put("LastMonthPresent", LastMonthPresent);
        map.put("LastMonthAbnormal", LastMonthAbnormal);
        // OBTAIN this month's data
        YearMonth currentYearMonth = YearMonth.from(today);
        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth();
        timeMap.put("beginCreateTime", firstDayOfMonth.format(formatter));
        timeMap.put("endCreateTime", lastDayOfMonth.format(formatter));
        hrAttendance.setParams(timeMap);
        Map<String, Object> NowMonthMap = baseMapper.getPayrollsCoutByHr(hrAttendance);
        BigDecimal NowMonthTotal = (BigDecimal) NowMonthMap.get("total_after_tax");
        Double NowMonthHolidays = (Double) NowMonthMap.get("total_holidays");
        Double NowMonthPresent = (Double) NowMonthMap.get("total_present");
        BigDecimal NowMonthAbnormal = (BigDecimal) NowMonthMap.get("total_abnormal");
        map.put("NowMonthTotal", NowMonthTotal);
        map.put("NowMonthHolidays", NowMonthHolidays);
        map.put("NowMonthPresent", NowMonthPresent);
        map.put("NowMonthAbnormal", NowMonthAbnormal);
        return map;
    }

    @Override
    public List<payRollVo> selectHrPayrollListVo(HrPayroll hrPayroll) {
        return baseMapper.selectHrPayrollListVo(hrPayroll);
    }

    @Override
    public List<payRollVo> selectHrPayrollListVoByIds(String payrollIds) {
        return baseMapper.selectHrPayrollListVoByIds(payrollIds);
    }

    @Override
    public Map<String, Object> lastPayrollByUserId(HrPayroll hrPayroll) {
        Map<String, Object>  hashMap = new HashMap<>();
        HrPayroll hrPayroll2  =  baseMapper.lastPayrollByUserId(hrPayroll);
        if(ObjectUtils.isNotEmpty(hrPayroll2)){
            Date hireData = hrPayroll2.getHireData();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
            String formatted = sdf.format(hireData);
            LocalDate hireDate = hireData.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate today = LocalDate.now();
            long daysBetween = ChronoUnit.DAYS.between(today, hireDate);
            hashMap.put("Remaining",daysBetween);
            hashMap.put("payDay",formatted);
        }
        return hashMap;
    }
}
