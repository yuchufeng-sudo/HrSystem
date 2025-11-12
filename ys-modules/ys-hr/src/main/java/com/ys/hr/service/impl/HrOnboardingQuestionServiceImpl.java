package com.ys.hr.service.impl;

import java.util.Collections;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrOnboardingQuestionMapper;
import com.ys.hr.domain.HrOnboardingQuestion;
import com.ys.hr.service.IHrOnboardingQuestionService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Job listings questions table Service Implementation
 *
 * @author ys
 * @date 2025-10-13
 */
@Service
public class HrOnboardingQuestionServiceImpl extends ServiceImpl<HrOnboardingQuestionMapper, HrOnboardingQuestion> implements IHrOnboardingQuestionService
{

    /**
     * Query Job listings questions table
     *
     * @param id Job listings questions table primary key
     * @return Job listings questions table
     */
    @Override
    public HrOnboardingQuestion selectHrOnboardingQuestionById(String id)
    {
        return baseMapper.selectHrOnboardingQuestionById(id);
    }

    /**
     * Query Job listings questions table list
     *
     * @param hrOnboardingQuestion Job listings questions table
     * @return Job listings questions table
     */
    @Override
    public List<HrOnboardingQuestion> selectHrOnboardingQuestionList(HrOnboardingQuestion hrOnboardingQuestion)
    {
        return baseMapper.selectHrOnboardingQuestionList(hrOnboardingQuestion);
    }

    /**
     * Add Job listings questions table
     *
     * @param hrOnboardingQuestion Job listings questions table
     * @return Result
     */
    @Override
    public int insertHrOnboardingQuestion(HrOnboardingQuestion hrOnboardingQuestion)
    {
        hrOnboardingQuestion.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrOnboardingQuestion);
    }

    /**
     * Update Job listings questions table
     *
     * @param hrOnboardingQuestion Job listings questions table
     * @return Result
     */
    @Override
    public int updateHrOnboardingQuestion(HrOnboardingQuestion hrOnboardingQuestion)
    {
        hrOnboardingQuestion.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrOnboardingQuestion);
    }

    /**
     * Batch delete Job listings questions table
     *
     * @param ids Job listings questions table primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrOnboardingQuestionByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Job listings questions table information
     *
     * @param id Job listings questions table primary key
     * @return Result
     */
    @Override
    public int deleteHrOnboardingQuestionById(String id)
    {
        return baseMapper.deleteById(id);
    }

    @Override
    public List<HrOnboardingQuestion> selectHrOnboardingQuestionListByEmp(HrOnboardingQuestion hrOnboardingQuestion) {
        return baseMapper.selectHrOnboardingQuestionListByEmp(hrOnboardingQuestion);
    }
}
