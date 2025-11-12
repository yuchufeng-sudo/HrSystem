package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrTrainingAssessment;

/**
 * Training Assessment Mapper Interface
 *
 * @author ys
 * @date 2025-06-25
 */
public interface HrTrainingAssessmentMapper extends BaseMapper<HrTrainingAssessment>
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

}