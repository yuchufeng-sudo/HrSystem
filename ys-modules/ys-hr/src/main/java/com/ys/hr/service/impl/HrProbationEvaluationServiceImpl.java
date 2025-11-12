package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.service.IHrEmployeesService;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrProbationEvaluationMapper;
import com.ys.hr.domain.HrProbationEvaluation;
import com.ys.hr.service.IHrProbationEvaluationService;
import com.ys.common.core.utils.DateUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Employee Probationary Evaluation Service Implementation
 *
 * @author ys
 * @date 2025-06-23
 */
@Service
public class HrProbationEvaluationServiceImpl extends ServiceImpl<HrProbationEvaluationMapper, HrProbationEvaluation> implements IHrProbationEvaluationService
{

    @Resource
    private IHrEmployeesService employeesService;

    /**
     * Query Employee Probationary Evaluation
     *
     * @param id Employee Probationary Evaluation primary key
     * @return Employee Probationary Evaluation
     */
    @Override
    public HrProbationEvaluation selectHrProbationEvaluationById(Long id)
    {
        return baseMapper.selectHrProbationEvaluationById(id);
    }

    /**
     * Query Employee Probationary Evaluation list
     *
     * @param hrProbationEvaluation Employee Probationary Evaluation
     * @return Employee Probationary Evaluation
     */
    @Override
    public List<HrProbationEvaluation> selectHrProbationEvaluationList(HrProbationEvaluation hrProbationEvaluation)
    {
        return baseMapper.selectHrProbationEvaluationList(hrProbationEvaluation);
    }

    /**
     * Add Employee Probationary Evaluation
     *
     * @param hrProbationEvaluation Employee Probationary Evaluation
     * @return Result
     */
    @Transactional
    @Override
    public int insertHrProbationEvaluation(HrProbationEvaluation hrProbationEvaluation)
    {
        hrProbationEvaluation.setCreateTime(DateUtils.getNowDate());
        Long employeeId = hrProbationEvaluation.getEmployeeId();
        HrEmployees hrEmployees = new HrEmployees();
        hrEmployees.setEmployeeId(employeeId);
        hrEmployees.setProbationStatus("0");
        employeesService.updateEmployees(hrEmployees);
        return baseMapper.insert(hrProbationEvaluation);
    }

    /**
     * Update Employee Probationary Evaluation
     *
     * @param hrProbationEvaluation Employee Probationary Evaluation
     * @return Result
     */
    @Override
    public int updateHrProbationEvaluation(HrProbationEvaluation hrProbationEvaluation)
    {
        hrProbationEvaluation.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrProbationEvaluation);
    }
}
