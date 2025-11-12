package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrJob;

import java.util.List;

/**
 * Job Management Service Interface
 *
 * @author ys
 * @date 2025-06-24
 */
public interface IHrJobService extends IService<HrJob>
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

    /**
     * Add Job Management
     *
     * @param hrJob Job Management
     * @return Result
     */
    public int insertHrJob(HrJob hrJob);

    /**
     * Update Job Management
     *
     * @param hrJob Job Management
     * @return Result
     */
    public int updateHrJob(HrJob hrJob);

    boolean deleteHrJobByIds(Long[] ids);
}
