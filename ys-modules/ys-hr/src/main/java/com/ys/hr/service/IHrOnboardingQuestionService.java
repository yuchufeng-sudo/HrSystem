package com.ys.hr.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrOnboardingQuestion;

/**
 * Job listings questions table Service Interface
 *
 * @author ys
 * @date 2025-10-13
 */
public interface IHrOnboardingQuestionService extends IService<HrOnboardingQuestion>
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

    /**
     * Add Job listings questions table
     *
     * @param hrOnboardingQuestion Job listings questions table
     * @return Result
     */
    public int insertHrOnboardingQuestion(HrOnboardingQuestion hrOnboardingQuestion);

    /**
     * Update Job listings questions table
     *
     * @param hrOnboardingQuestion Job listings questions table
     * @return Result
     */
    public int updateHrOnboardingQuestion(HrOnboardingQuestion hrOnboardingQuestion);

    List<HrOnboardingQuestion> selectHrOnboardingQuestionListByEmp(HrOnboardingQuestion hrOnboardingQuestion);
}
