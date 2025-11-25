package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrEmpSchedulingMapper;
import com.ys.hr.domain.HrEmpScheduling;
import com.ys.hr.service.IHrEmpSchedulingService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 *  Employee Scheduling Service Implementation
 *
 * @author ys
 * @date 2025-06-04
 */
@Service
public class HrEmpSchedulingServiceImpl extends ServiceImpl<HrEmpSchedulingMapper, HrEmpScheduling> implements IHrEmpSchedulingService
{

    /**
     * Query Employee Scheduling
     *
     * @param schedulingId  Employee Scheduling primary key
     * @return  Employee Scheduling
     */
    @Override
    public HrEmpScheduling selectHrEmpSchedulingBySchedulingId(String schedulingId)
    {
        return baseMapper.selectHrEmpSchedulingBySchedulingId(schedulingId);
    }

    /**
     * Query Employee Scheduling list
     *
     * @param hrEmpScheduling Employee Scheduling
     * @return  Employee Scheduling
     */
    @Override
    public List<HrEmpScheduling> selectHrEmpSchedulingList(HrEmpScheduling hrEmpScheduling)
    {
        return baseMapper.selectHrEmpSchedulingList(hrEmpScheduling);
    }

    /**
     * Add Employee Scheduling
     *
     * @param hrEmpScheduling Employee Scheduling
     * @return Result
     */
    @Override
    public int insertHrEmpScheduling(HrEmpScheduling hrEmpScheduling)
    {
        hrEmpScheduling.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrEmpScheduling);
    }

    /**
     * Update Employee Scheduling
     *
     * @param hrEmpScheduling Employee Scheduling
     * @return Result
     */
    @Override
    public int updateHrEmpScheduling(HrEmpScheduling hrEmpScheduling)
    {
        hrEmpScheduling.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrEmpScheduling);
    }

    /**
     * Batch delete  Employee Scheduling
     *
     * @param schedulingIds  Employee Scheduling primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrEmpSchedulingBySchedulingIds(String[] schedulingIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(schedulingIds));
    }

    /**
     * Delete Employee Scheduling information
     *
     * @param schedulingId  Employee Scheduling primary key
     * @return Result
     */
    @Override
    public int deleteHrEmpSchedulingBySchedulingId(String schedulingId)
    {
        return baseMapper.deleteById(schedulingId);
    }
}
