package com.ys.hr.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrJobListingsQuestionMapper;
import com.ys.hr.domain.HrJobListingsQuestion;
import com.ys.hr.service.IHrJobListingsQuestionService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Job listings questions Service Implementation
 *
 * @author ys
 * @date 2025-09-27
 */
@Service
public class HrJobListingsQuestionServiceImpl extends ServiceImpl<HrJobListingsQuestionMapper, HrJobListingsQuestion> implements IHrJobListingsQuestionService
{
    /**
     * Query Job listings questions list
     *
     * @param hrJobListingsQuestion Job listings questions
     * @return Job listings questions
     */
    @Override
    public List<HrJobListingsQuestion> selectHrJobListingsQuestionList(HrJobListingsQuestion hrJobListingsQuestion)
    {
        return baseMapper.selectHrJobListingsQuestionList(hrJobListingsQuestion);
    }
}
