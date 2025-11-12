package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrTargetFeedback;

import java.util.List;

/**
 * Target Feedback Mapper Interface
 *
 * @author ys
 * @date 2025-06-30
 */
public interface HrTargetFeedbackMapper extends BaseMapper<HrTargetFeedback>
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

}
