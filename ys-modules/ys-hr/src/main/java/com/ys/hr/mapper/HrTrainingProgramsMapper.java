package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrTrainingPrograms;

/**
 *  TRAINING PROGRAM SUPERVISOR  Mapper Interface
 *
 * @author ys
 * @date 2025-05-29
 */
public interface HrTrainingProgramsMapper extends BaseMapper<HrTrainingPrograms>
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

}