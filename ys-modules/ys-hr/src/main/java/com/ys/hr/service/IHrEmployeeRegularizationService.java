package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmployeeRegularization;

import java.util.List;

/**
 * Employee Regularization Management Service Interface
 *
 * @author ys
 * @date 2025-06-23
 */
public interface IHrEmployeeRegularizationService extends IService<HrEmployeeRegularization>
{
    /**
     * Query Employee Regularization Management
     *
     * @param id Employee Regularization Management primary key
     * @return Employee Regularization Management
     */
    public HrEmployeeRegularization selectHrEmployeeRegularizationById(Long id);

    /**
     * Query Employee Regularization Management list
     *
     * @param hrEmployeeRegularization Employee Regularization Management
     * @return Employee Regularization Management collection
     */
    public List<HrEmployeeRegularization> selectHrEmployeeRegularizationList(HrEmployeeRegularization hrEmployeeRegularization);

    /**
     * Add Employee Regularization Management
     *
     * @param hrEmployeeRegularization Employee Regularization Management
     * @return Result
     */
    public int insertHrEmployeeRegularization(HrEmployeeRegularization hrEmployeeRegularization);

    /**
     * Update Employee Regularization Management
     *
     * @param hrEmployeeRegularization Employee Regularization Management
     * @return Result
     */
    public int updateHrEmployeeRegularization(HrEmployeeRegularization hrEmployeeRegularization);
}
