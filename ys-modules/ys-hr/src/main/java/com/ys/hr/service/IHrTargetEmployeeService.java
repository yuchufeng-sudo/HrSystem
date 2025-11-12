package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrTargetEmployee;
import com.ys.hr.domain.HrTargets;

import java.util.List;

/**
 * Target allocation of employees Service Interface
 *
 * @author ys
 * @date 2025-06-23
 */
public interface IHrTargetEmployeeService extends IService<HrTargetEmployee>
{
    /**
     * Query Target allocation of employees
     *
     * @param id Target allocation of employees primary key
     * @return Target allocation of employees
     */
    public HrTargetEmployee selectHrTargetEmployeeById(Long id);

    /**
     * Query Target allocation of employees list
     *
     * @param hrTargetEmployee Target allocation of employees
     * @return Target allocation of employees collection
     */
    public List<HrTargetEmployee> selectHrTargetEmployeeList(HrTargetEmployee hrTargetEmployee);

    /**
     * Add Target allocation of employees
     *
     * @param hrTargetEmployee Target allocation of employees
     * @return Result
     */
    public int insertHrTargetEmployee(HrTargetEmployee hrTargetEmployee);

    /**
     * Update Target allocation of employees
     *
     * @param hrTargetEmployee Target allocation of employees
     * @return Result
     */
    public int updateHrTargetEmployee(HrTargetEmployee hrTargetEmployee);

    void updateByEmployeeId(Long employeeId);

    HrTargetEmployee selectExecutionTarget(Long employeeId);

    int stopPause(HrTargetEmployee hrTargetEmployee);

    int finish(HrTargetEmployee hrTargetEmployee);

    List<HrTargetEmployee> selectEmployeeTargets(HrTargets hrTargets);

    List<HrTargetEmployee> selectEmployeeTargetsByAdmin(HrTargets hrTargets);

    void saveBatchEmployees(List<HrTargetEmployee> list);
}
