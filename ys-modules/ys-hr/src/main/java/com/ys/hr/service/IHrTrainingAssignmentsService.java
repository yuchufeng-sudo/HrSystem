package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrTrainingAssignments;

import java.util.List;

/**
 * TRAINING ALLOCATION RECORD Service Interface
 *
 * @author ys
 * @date 2025-05-29
 */
public interface IHrTrainingAssignmentsService extends IService<HrTrainingAssignments>
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

    /**
     * Add TRAINING ALLOCATION RECORD
     *
     * @param hrTrainingAssignments TRAINING ALLOCATION RECORD
     * @return Result
     */
    public int insertHrTrainingAssignments(HrTrainingAssignments hrTrainingAssignments);

    /**
     * Update TRAINING ALLOCATION RECORD
     *
     * @param hrTrainingAssignments TRAINING ALLOCATION RECORD
     * @return Result
     */
    public int updateHrTrainingAssignments(HrTrainingAssignments hrTrainingAssignments);

    /**
     * Batch delete TRAINING ALLOCATION RECORD
     *
     * @param assignmentIds TRAINING ALLOCATION RECORD primary keys to be deleted
     * @return Result
     */
    public int deleteHrTrainingAssignmentsByAssignmentIds(String[] assignmentIds);

    /**
     * Delete TRAINING ALLOCATION RECORD information
     *
     * @param assignmentId TRAINING ALLOCATION RECORD primary key
     * @return Result
     */
    public int deleteHrTrainingAssignmentsByAssignmentId(String assignmentId);
}
