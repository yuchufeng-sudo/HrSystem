package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrAttendance;
import com.ys.hr.domain.HrPayroll;
import com.ys.hr.domain.vo.AnnualPayrollStatisticsVo;
import com.ys.hr.domain.vo.payRollVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *  EMPLOYEE  SALARY  Mapper Interface
 *
 * @author ys
 * @date 2025-05-30
 */
public interface HrPayrollMapper extends BaseMapper<HrPayroll>
{
    /**
     * Query  EMPLOYEE  SALARY
     *
     * @param payrollId  EMPLOYEE  SALARY  primary key
     * @return  EMPLOYEE  SALARY
     */
    public HrPayroll selectHrPayrollByPayrollId(String payrollId);

    /**
     * Query  EMPLOYEE  SALARY  list
     *
     * @param hrPayroll  EMPLOYEE  SALARY
     * @return  EMPLOYEE  SALARY  collection
     */
    public List<HrPayroll> selectHrPayrollList(HrPayroll hrPayroll);

    HrPayroll getPayRollByUserId(HrPayroll hrPayroll);

    int updataByIds(@Param("list") List<String> list);

    Map<String, Object> getPayrollsCoutByHr(HrAttendance hrAttendance);

    List<AnnualPayrollStatisticsVo> selectAnnualPayrollStatisticsList(
            @Param("enterpriseId") String enterpriseId,
            @Param("year") String year
    );

    List<payRollVo> selectHrPayrollListVo(HrPayroll hrPayroll);

    List<payRollVo> selectHrPayrollListVoByIds(String payrollIds);

    HrPayroll lastPayrollByUserId(HrPayroll hrPayroll);
}
