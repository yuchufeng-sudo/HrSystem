package com.ys.hr.service.impl;

import java.util.Collections;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.system.api.domain.SysUser;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrQuestionAnswerMapper;
import com.ys.hr.domain.HrQuestionAnswer;
import com.ys.hr.service.IHrQuestionAnswerService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Candidate question answers table Service Implementation
 *
 * @author ys
 * @date 2025-09-27
 */
@Service
public class HrQuestionAnswerServiceImpl extends ServiceImpl<HrQuestionAnswerMapper, HrQuestionAnswer> implements IHrQuestionAnswerService
{
    /**
     * Query Candidate question answers table list
     *
     * @param hrQuestionAnswer Candidate question answers table
     * @return Candidate question answers table
     */
    @Override
    public List<HrQuestionAnswer> selectHrQuestionAnswerList(HrQuestionAnswer hrQuestionAnswer)
    {
        return baseMapper.selectHrQuestionAnswerList(hrQuestionAnswer);
    }

    @Override
    public List<SysUser> selectHrList(String enterpriseId) {
        return baseMapper.selectHrList(enterpriseId);
    }
}
