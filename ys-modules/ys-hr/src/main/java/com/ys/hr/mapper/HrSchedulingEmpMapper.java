package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrSchedulingEmp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *    EMPLOYEE SCHEDULING Mapper Interface
 *
 * @author ys
 * @date 2025-06-08
 */
public interface HrSchedulingEmpMapper extends BaseMapper<HrSchedulingEmp>
{
    /**
     * Query    EMPLOYEE SCHEDULING
     *
     * @param schedulingEmpId    EMPLOYEE SCHEDULING primary key
     * @return    EMPLOYEE SCHEDULING
     */
    public HrSchedulingEmp selectHrSchedulingEmpBySchedulingEmpId(String schedulingEmpId);

    /**
     * Query    EMPLOYEE SCHEDULING list
     *
     * @param hrSchedulingEmp    EMPLOYEE SCHEDULING
     * @return    EMPLOYEE SCHEDULING collection
     */
    public List<HrSchedulingEmp> selectHrSchedulingEmpList(HrSchedulingEmp hrSchedulingEmp);

    HrSchedulingEmp selectHrSchedulingEmpByDataAndUserId(HrSchedulingEmp hrSchedulingEmp);

    List<HrSchedulingEmp> selectHrSchedulingEmpByUserId(HrSchedulingEmp hrSchedulingEmp);

    /**
     * Query the scheduling arrangement according to the USER ID and the date of the current month.
     * @param userId
     * @param formattedDate
     * @return
     */
    HrSchedulingEmp selectSchedulingEmpByUserIdAndDate(@Param("userId") Long userId,
                                                       @Param("formattedDate") String formattedDate);

    HrSchedulingEmp selectHrSchedulingEmpInfo(HrSchedulingEmp hrSchedulingEmp);
}
