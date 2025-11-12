package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.security.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrTrainingAssessmentMapper;
import com.ys.hr.domain.HrTrainingAssessment;
import com.ys.hr.service.IHrTrainingAssessmentService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Training Assessment Service Implementation
 *
 * @author ys
 * @date 2025-06-25
 */
@Service
public class HrTrainingAssessmentServiceImpl extends ServiceImpl<HrTrainingAssessmentMapper, HrTrainingAssessment> implements IHrTrainingAssessmentService
{

    /**
     * Query Training Assessment
     *
     * @param id Training Assessment primary key
     * @return Training Assessment
     */
    @Override
    public HrTrainingAssessment selectHrTrainingAssessmentById(Long id)
    {
        return baseMapper.selectHrTrainingAssessmentById(id);
    }

    /**
     * Query Training Assessment list
     *
     * @param hrTrainingAssessment Training Assessment
     * @return Training Assessment
     */
    @Override
    public List<HrTrainingAssessment> selectHrTrainingAssessmentList(HrTrainingAssessment hrTrainingAssessment)
    {
        return baseMapper.selectHrTrainingAssessmentList(hrTrainingAssessment);
    }

    /**
     * Add Training Assessment
     *
     * @param hrTrainingAssessment Training Assessment
     * @return Result
     */
    @Override
    public int insertHrTrainingAssessment(HrTrainingAssessment hrTrainingAssessment)
    {
        hrTrainingAssessment.setAssessorId(SecurityUtils.getUserId());
        hrTrainingAssessment.setAssessmentTime(DateUtils.getNowDate());
        hrTrainingAssessment.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrTrainingAssessment);
    }

    /**
     * Update Training Assessment
     *
     * @param hrTrainingAssessment Training Assessment
     * @return Result
     */
    @Override
    public int updateHrTrainingAssessment(HrTrainingAssessment hrTrainingAssessment)
    {
        hrTrainingAssessment.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrTrainingAssessment);
    }

    /**
     * Batch delete Training Assessment
     *
     * @param ids Training Assessment primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrTrainingAssessmentByIds(Long[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Training Assessment information
     *
     * @param id Training Assessment primary key
     * @return Result
     */
    @Override
    public int deleteHrTrainingAssessmentById(Long id)
    {
        return baseMapper.deleteById(id);
    }
}
