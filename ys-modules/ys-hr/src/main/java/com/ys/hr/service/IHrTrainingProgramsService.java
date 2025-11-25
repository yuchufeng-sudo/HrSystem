package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrTrainingPrograms;

import java.util.List;

/**
 *  Training Management Service Interface
 *
 * @author ys
 * @date 2025-05-29
 */
public interface IHrTrainingProgramsService extends IService<HrTrainingPrograms>
{
    /**
     * Query Training Management
     *
     * @param programId  Training Management primary key
     * @return  Training Management
     */
    public HrTrainingPrograms selectHrTrainingProgramsByProgramId(Long programId);

    /**
     * Query Training Management list
     *
     * @param hrTrainingPrograms  Training Management
     * @return  Training Management collection
     */
    public List<HrTrainingPrograms> selectHrTrainingProgramsList(HrTrainingPrograms hrTrainingPrograms);

    /**
     * Add Training Management
     *
     * @param hrTrainingPrograms  Training Management
     * @return Result
     */
    public int insertHrTrainingPrograms(HrTrainingPrograms hrTrainingPrograms);

    /**
     * Update Training Management
     *
     * @param hrTrainingPrograms  Training Management
     * @return Result
     */
    public int updateHrTrainingPrograms(HrTrainingPrograms hrTrainingPrograms);

    /**
     * Batch delete  Training Management
     *
     * @param programIds  Training Management primary keys to be deleted
     * @return Result
     */
    public int deleteHrTrainingProgramsByProgramIds(Long[] programIds);

    /**
     * Delete Training Management information
     *
     * @param programId  Training Management primary key
     * @return Result
     */
    public int deleteHrTrainingProgramsByProgramId(Long programId);

    public int checkTrainingPrograms(HrTrainingPrograms hrTrainingPrograms);

}
