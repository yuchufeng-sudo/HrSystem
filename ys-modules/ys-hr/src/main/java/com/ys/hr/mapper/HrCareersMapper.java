package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrCareers;

/**
 * Company careers information Mapper Interface
 *
 * @author ys
 * @date 2025-09-27
 */
public interface HrCareersMapper extends BaseMapper<HrCareers>
{
    /**
     * Query Company careers information
     *
     * @param id Company careers information primary key
     * @return Company careers information
     */
    public HrCareers selectHrCareersById(String id);
    
    /**
     * Query Company careers information list
     *
     * @param hrCareers Company careers information
     * @return Company careers information collection
     */
    public List<HrCareers> selectHrCareersList(HrCareers hrCareers);

}