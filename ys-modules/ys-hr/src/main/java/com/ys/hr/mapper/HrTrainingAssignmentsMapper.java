package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrTrainingAssignments;

/**
 * TRAINING ALLOCATION RECORD Mapper Interface
 *
 * @author ys
 * @date 2025-05-29
 */
public interface HrTrainingAssignmentsMapper extends BaseMapper<HrTrainingAssignments>
{
    /**
     * Query TRAINING ALLOCATION RECORD
     *
     * @param assignmentId TRAINING ALLOCATION RECORD primary key
     * @return TRAINING ALLOCATION RECORD
     */
    public HrTrainingAssignments selectHrTrainingAssignmentsByAssignmentId(String assignmentId);
    
    /**
     * Query TRAINING ALLOCATION RECORD list
     *
     * @param hrTrainingAssignments TRAINING ALLOCATION RECORD
     * @return TRAINING ALLOCATION RECORD collection
     */
    public List<HrTrainingAssignments> selectHrTrainingAssignmentsList(HrTrainingAssignments hrTrainingAssignments);

}