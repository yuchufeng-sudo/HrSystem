package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrJob;

import java.util.List;

/**
 * Job Management Mapper Interface
 *
 * @author ys
 * @date 2025-06-24
 */
public interface HrJobMapper extends BaseMapper<HrJob>
{
    /**
     * Query Job Management
     *
     * @param id Job Management primary key
     * @return Job Management
     */
    public HrJob selectHrJobById(Long id);

    /**
     * Query Job Management list
     *
     * @param hrJob Job Management
     * @return Job Management collection
     */
    public List<HrJob> selectHrJobList(HrJob hrJob);

}
