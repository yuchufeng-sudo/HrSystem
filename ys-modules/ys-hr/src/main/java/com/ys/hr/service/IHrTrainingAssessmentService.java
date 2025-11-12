package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrTrainingAssessment;

import java.util.List;

/**
 * Training Assessment Service Interface
 *
 * @author ys
 * @date 2025-06-25
 */
public interface IHrTrainingAssessmentService extends IService<HrTrainingAssessment>
{
    /**
     * Query Training Assessment
     *
     * @param id Training Assessment primary key
     * @return Training Assessment
     */
    public HrTrainingAssessment selectHrTrainingAssessmentById(Long id);

    /**
     * Query Training Assessment list
     *
     * @param hrTrainingAssessment Training Assessment
     * @return Training Assessment collection
     */
    public List<HrTrainingAssessment> selectHrTrainingAssessmentList(HrTrainingAssessment hrTrainingAssessment);

    /**
     * Add Training Assessment
     *
     * @param hrTrainingAssessment Training Assessment
     * @return Result
     */
    public int insertHrTrainingAssessment(HrTrainingAssessment hrTrainingAssessment);

    /**
     * Update Training Assessment
     *
     * @param hrTrainingAssessment Training Assessment
     * @return Result
     */
    public int updateHrTrainingAssessment(HrTrainingAssessment hrTrainingAssessment);

    /**
     * Batch delete Training Assessment
     *
     * @param ids Training Assessment primary keys to be deleted
     * @return Result
     */
    public int deleteHrTrainingAssessmentByIds(Long[] ids);

    /**
     * Delete Training Assessment information
     *
     * @param id Training Assessment primary key
     * @return Result
     */
    public int deleteHrTrainingAssessmentById(Long id);
}
