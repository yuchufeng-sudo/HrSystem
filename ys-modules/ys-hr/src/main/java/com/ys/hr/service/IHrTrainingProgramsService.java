package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrTrainingPrograms;

import java.util.List;

/**
 *  TRAINING PROGRAM SUPERVISOR  Service Interface
 *
 * @author ys
 * @date 2025-05-29
 */
public interface IHrTrainingProgramsService extends IService<HrTrainingPrograms>
{
    /**
     * Query  TRAINING PROGRAM SUPERVISOR
     *
     * @param programId  TRAINING PROGRAM SUPERVISOR  primary key
     * @return  TRAINING PROGRAM SUPERVISOR
     */
    public HrTrainingPrograms selectHrTrainingProgramsByProgramId(Long programId);

    /**
     * Query  TRAINING PROGRAM SUPERVISOR  list
     *
     * @param hrTrainingPrograms  TRAINING PROGRAM SUPERVISOR
     * @return  TRAINING PROGRAM SUPERVISOR  collection
     */
    public List<HrTrainingPrograms> selectHrTrainingProgramsList(HrTrainingPrograms hrTrainingPrograms);

    /**
     * Add  TRAINING PROGRAM SUPERVISOR
     *
     * @param hrTrainingPrograms  TRAINING PROGRAM SUPERVISOR
     * @return Result
     */
    public int insertHrTrainingPrograms(HrTrainingPrograms hrTrainingPrograms);

    /**
     * Update  TRAINING PROGRAM SUPERVISOR
     *
     * @param hrTrainingPrograms  TRAINING PROGRAM SUPERVISOR
     * @return Result
     */
    public int updateHrTrainingPrograms(HrTrainingPrograms hrTrainingPrograms);

    /**
     * Batch delete  TRAINING PROGRAM SUPERVISOR
     *
     * @param programIds  TRAINING PROGRAM SUPERVISOR  primary keys to be deleted
     * @return Result
     */
    public int deleteHrTrainingProgramsByProgramIds(Long[] programIds);

    /**
     * Delete  TRAINING PROGRAM SUPERVISOR  information
     *
     * @param programId  TRAINING PROGRAM SUPERVISOR  primary key
     * @return Result
     */
    public int deleteHrTrainingProgramsByProgramId(Long programId);

    public int checkTrainingPrograms(HrTrainingPrograms hrTrainingPrograms);

}
