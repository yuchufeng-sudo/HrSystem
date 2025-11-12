package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrJobListingsQuestion;

/**
 * Job listings questions Mapper Interface
 *
 * @author ys
 * @date 2025-09-27
 */
public interface HrJobListingsQuestionMapper extends BaseMapper<HrJobListingsQuestion>
{
    /**
     * Query Job listings questions
     *
     * @param id Job listings questions primary key
     * @return Job listings questions
     */
    public HrJobListingsQuestion selectHrJobListingsQuestionById(String id);
    
    /**
     * Query Job listings questions list
     *
     * @param hrJobListingsQuestion Job listings questions
     * @return Job listings questions collection
     */
    public List<HrJobListingsQuestion> selectHrJobListingsQuestionList(HrJobListingsQuestion hrJobListingsQuestion);

}