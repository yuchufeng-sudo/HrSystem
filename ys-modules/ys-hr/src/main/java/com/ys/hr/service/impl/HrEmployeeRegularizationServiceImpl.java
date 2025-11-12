package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.DateUtils;
import com.ys.hr.domain.HrEmployeeRegularization;
import com.ys.hr.mapper.HrEmployeeRegularizationMapper;
import com.ys.hr.service.IHrEmployeeRegularizationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Employee Regularization Management Service Implementation
 *
 * @author ys
 * @date 2025-06-23
 */
@Service
public class HrEmployeeRegularizationServiceImpl extends ServiceImpl<HrEmployeeRegularizationMapper, HrEmployeeRegularization> implements IHrEmployeeRegularizationService
{

    /**
     * Query Employee Regularization Management
     *
     * @param id Employee Regularization Management primary key
     * @return Employee Regularization Management
     */
    @Override
    public HrEmployeeRegularization selectHrEmployeeRegularizationById(Long id)
    {
        return baseMapper.selectHrEmployeeRegularizationById(id);
    }

    /**
     * Query Employee Regularization Management list
     *
     * @param hrEmployeeRegularization Employee Regularization Management
     * @return Employee Regularization Management
     */
    @Override
    public List<HrEmployeeRegularization> selectHrEmployeeRegularizationList(HrEmployeeRegularization hrEmployeeRegularization)
    {
        return baseMapper.selectHrEmployeeRegularizationList(hrEmployeeRegularization);
    }

    /**
     * Add Employee Regularization Management
     *
     * @param hrEmployeeRegularization Employee Regularization Management
     * @return Result
     */
    @Override
    public int insertHrEmployeeRegularization(HrEmployeeRegularization hrEmployeeRegularization)
    {
        hrEmployeeRegularization.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrEmployeeRegularization);
    }

    /**
     * Update Employee Regularization Management
     *
     * @param hrEmployeeRegularization Employee Regularization Management
     * @return Result
     */
    @Override
    public int updateHrEmployeeRegularization(HrEmployeeRegularization hrEmployeeRegularization)
    {
        hrEmployeeRegularization.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrEmployeeRegularization);
    }
}
