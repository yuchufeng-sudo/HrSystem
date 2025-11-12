package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrProbationEvaluation;

/**
 * Employee Probationary Evaluation Mapper Interface
 *
 * @author ys
 * @date 2025-06-23
 */
public interface HrProbationEvaluationMapper extends BaseMapper<HrProbationEvaluation>
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

}
