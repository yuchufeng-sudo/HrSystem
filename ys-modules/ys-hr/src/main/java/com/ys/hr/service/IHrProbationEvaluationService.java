package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrProbationEvaluation;

import java.util.List;

/**
 * Employee Probationary Evaluation Service Interface
 *
 * @author ys
 * @date 2025-06-23
 */
public interface IHrProbationEvaluationService extends IService<HrProbationEvaluation>
{
    /**
     * Query Employee Probationary Evaluation
     *
     * @param id Employee Probationary Evaluation primary key
     * @return Employee Probationary Evaluation
     */
    public HrProbationEvaluation selectHrProbationEvaluationById(Long id);

    /**
     * Query Employee Probationary Evaluation list
     *
     * @param hrProbationEvaluation Employee Probationary Evaluation
     * @return Employee Probationary Evaluation collection
     */
    public List<HrProbationEvaluation> selectHrProbationEvaluationList(HrProbationEvaluation hrProbationEvaluation);

    /**
     * Add Employee Probationary Evaluation
     *
     * @param hrProbationEvaluation Employee Probationary Evaluation
     * @return Result
     */
    public int insertHrProbationEvaluation(HrProbationEvaluation hrProbationEvaluation);

    /**
     * Update Employee Probationary Evaluation
     *
     * @param hrProbationEvaluation Employee Probationary Evaluation
     * @return Result
     */
    public int updateHrProbationEvaluation(HrProbationEvaluation hrProbationEvaluation);
}
