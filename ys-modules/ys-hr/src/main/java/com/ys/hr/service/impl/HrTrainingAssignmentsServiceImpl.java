package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrTrainingAssignmentsMapper;
import com.ys.hr.domain.HrTrainingAssignments;
import com.ys.hr.service.IHrTrainingAssignmentsService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * TRAINING ALLOCATION RECORD Service Implementation
 *
 * @author ys
 * @date 2025-05-29
 */
@Service
public class HrTrainingAssignmentsServiceImpl extends ServiceImpl<HrTrainingAssignmentsMapper, HrTrainingAssignments> implements IHrTrainingAssignmentsService
{

    /**
     * Query TRAINING ALLOCATION RECORD
     *
     * @param assignmentId TRAINING ALLOCATION RECORD primary key
     * @return TRAINING ALLOCATION RECORD
     */
    @Override
    public HrTrainingAssignments selectHrTrainingAssignmentsByAssignmentId(String assignmentId)
    {
        return baseMapper.selectHrTrainingAssignmentsByAssignmentId(assignmentId);
    }

    /**
     * Query TRAINING ALLOCATION RECORD list
     *
     * @param hrTrainingAssignments TRAINING ALLOCATION RECORD
     * @return TRAINING ALLOCATION RECORD
     */
    @Override
    public List<HrTrainingAssignments> selectHrTrainingAssignmentsList(HrTrainingAssignments hrTrainingAssignments)
    {
        return baseMapper.selectHrTrainingAssignmentsList(hrTrainingAssignments);
    }

    /**
     * Add TRAINING ALLOCATION RECORD
     *
     * @param hrTrainingAssignments TRAINING ALLOCATION RECORD
     * @return Result
     */
    @Override
    public int insertHrTrainingAssignments(HrTrainingAssignments hrTrainingAssignments)
    {
        hrTrainingAssignments.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrTrainingAssignments);
    }

    /**
     * Update TRAINING ALLOCATION RECORD
     *
     * @param hrTrainingAssignments TRAINING ALLOCATION RECORD
     * @return Result
     */
    @Override
    public int updateHrTrainingAssignments(HrTrainingAssignments hrTrainingAssignments)
    {
        hrTrainingAssignments.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrTrainingAssignments);
    }

    /**
     * Batch delete TRAINING ALLOCATION RECORD
     *
     * @param assignmentIds TRAINING ALLOCATION RECORD primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrTrainingAssignmentsByAssignmentIds(String[] assignmentIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(assignmentIds));
    }

    /**
     * Delete TRAINING ALLOCATION RECORD information
     *
     * @param assignmentId TRAINING ALLOCATION RECORD primary key
     * @return Result
     */
    @Override
    public int deleteHrTrainingAssignmentsByAssignmentId(String assignmentId)
    {
        return baseMapper.deleteById(assignmentId);
    }
}