package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrEmpScheduling;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  EMPLOYEE  LAYOUT  Mapper Interface
 *
 * @author ys
 * @date 2025-06-04
 */
public interface HrEmpSchedulingMapper extends BaseMapper<HrEmpScheduling>
{
    /**
     * Query  EMPLOYEE  LAYOUT
     *
     * @param schedulingId  EMPLOYEE  LAYOUT  primary key
     * @return  EMPLOYEE  LAYOUT
     */
    public HrEmpScheduling selectHrEmpSchedulingBySchedulingId(@Param("schedulingId") String schedulingId);

    /**
     * Query  EMPLOYEE  LAYOUT  list
     *
     * @param hrEmpScheduling  EMPLOYEE  LAYOUT
     * @return  EMPLOYEE  LAYOUT  collection
     */
    public List<HrEmpScheduling> selectHrEmpSchedulingList(HrEmpScheduling hrEmpScheduling);

}
