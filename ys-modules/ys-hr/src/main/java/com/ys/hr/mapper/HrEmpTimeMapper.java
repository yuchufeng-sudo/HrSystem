package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrEmpTime;

import java.util.List;

/**
 *  EMPLOYEE  TIME  Mapper Interface
 *
 * @author ys
 * @date 2025-06-03
 */
public interface HrEmpTimeMapper extends BaseMapper<HrEmpTime>
{
    /**
     * Query  EMPLOYEE  TIME
     *
     * @param empTimeId  EMPLOYEE  TIME  primary key
     * @return  EMPLOYEE  TIME
     */
    public HrEmpTime selectHrEmpTimeByEmpTimeId(String empTimeId);

    /**
     * Query  EMPLOYEE  TIME  list
     *
     * @param hrEmpTime  EMPLOYEE  TIME
     * @return  EMPLOYEE  TIME  collection
     */
    public List<HrEmpTime> selectHrEmpTimeList(HrEmpTime hrEmpTime);

    HrEmpTime getTimeByUserId(HrEmpTime hrEmpTime);
}
