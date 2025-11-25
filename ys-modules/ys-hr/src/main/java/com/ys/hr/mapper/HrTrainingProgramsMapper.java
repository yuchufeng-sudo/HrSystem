package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrTrainingPrograms;

/**
 *  Training Management Mapper Interface
 *
 * @author ys
 * @date 2025-05-29
 */
public interface HrTrainingProgramsMapper extends BaseMapper<HrTrainingPrograms>
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

}
