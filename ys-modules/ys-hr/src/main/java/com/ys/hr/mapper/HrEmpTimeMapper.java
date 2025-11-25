package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrEmpTime;

import java.util.List;

/**
 *  Employee time  Mapper Interface
 *
 * @author ys
 * @date 2025-06-03
 */
public interface HrEmpTimeMapper extends BaseMapper<HrEmpTime>
{
    /**
     * Query Employee time
     *
     * @param empTimeId  Employee time  primary key
     * @return  Employee time
     */
    public HrEmpTime selectHrEmpTimeByEmpTimeId(String empTimeId);

    /**
     * Query Employee time  list
     *
     * @param hrEmpTime  Employee time
     * @return  Employee time  collection
     */
    public List<HrEmpTime> selectHrEmpTimeList(HrEmpTime hrEmpTime);

    HrEmpTime getTimeByUserId(HrEmpTime hrEmpTime);
}
