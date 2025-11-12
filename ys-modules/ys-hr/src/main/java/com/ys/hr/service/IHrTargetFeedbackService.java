package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrTargetFeedback;

import java.util.List;

/**
 * Target Feedback Service Interface
 *
 * @author ys
 * @date 2025-06-30
 */
public interface IHrTargetFeedbackService extends IService<HrTargetFeedback>
{
    /**
     * Query Target Feedback
     *
     * @param id Target Feedback primary key
     * @return Target Feedback
     */
    public HrTargetFeedback selectHrTargetFeedbackById(Long id);

    /**
     * Query Target Feedback list
     *
     * @param hrTargetFeedback Target Feedback
     * @return Target Feedback collection
     */
    public List<HrTargetFeedback> selectHrTargetFeedbackList(HrTargetFeedback hrTargetFeedback);

    /**
     * Add Target Feedback
     *
     * @param hrTargetFeedback Target Feedback
     * @return Result
     */
    public int insertHrTargetFeedback(HrTargetFeedback hrTargetFeedback);

    /**
     * Update Target Feedback
     *
     * @param hrTargetFeedback Target Feedback
     * @return Result
     */
    public int updateHrTargetFeedback(HrTargetFeedback hrTargetFeedback);
}
