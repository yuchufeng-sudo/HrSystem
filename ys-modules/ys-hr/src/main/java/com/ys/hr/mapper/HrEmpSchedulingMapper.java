package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrEmpScheduling;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Employee Scheduling Mapper Interface
 *
 * @author ys
 * @date 2025-06-04
 */
public interface HrEmpSchedulingMapper extends BaseMapper<HrEmpScheduling>
{
    /**
     * Query Employee Scheduling
     *
     * @param schedulingId  Employee Scheduling primary key
     * @return  Employee Scheduling
     */
    public HrEmpScheduling selectHrEmpSchedulingBySchedulingId(@Param("schedulingId") String schedulingId);

    /**
     * Query Employee Scheduling list
     *
     * @param hrEmpScheduling Employee Scheduling
     * @return  Employee Scheduling collection
     */
    public List<HrEmpScheduling> selectHrEmpSchedulingList(HrEmpScheduling hrEmpScheduling);

}
