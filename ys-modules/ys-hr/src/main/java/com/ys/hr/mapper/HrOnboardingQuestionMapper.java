package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrOnboardingQuestion;

/**
 * Job listings questions table Mapper Interface
 *
 * @author ys
 * @date 2025-10-13
 */
public interface HrOnboardingQuestionMapper extends BaseMapper<HrOnboardingQuestion>
{
    /**
     * Query Job listings questions table
     *
     * @param id Job listings questions table primary key
     * @return Job listings questions table
     */
    public HrOnboardingQuestion selectHrOnboardingQuestionById(String id);

    /**
     * Query Job listings questions table list
     *
     * @param hrOnboardingQuestion Job listings questions table
     * @return Job listings questions table collection
     */
    public List<HrOnboardingQuestion> selectHrOnboardingQuestionList(HrOnboardingQuestion hrOnboardingQuestion);
    public List<HrOnboardingQuestion> selectHrOnboardingQuestionListByEmp(HrOnboardingQuestion hrOnboardingQuestion);

}
