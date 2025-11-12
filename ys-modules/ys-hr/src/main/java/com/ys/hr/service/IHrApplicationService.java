package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrApplication;

import java.util.List;

/**
 * application Service Interface
 *
 * @author ys
 * @date 2025-06-17
 */
public interface IHrApplicationService extends IService<HrApplication>
{
    /**
     * Query application
     *
     * @param applicationId application primary key
     * @return application
     */
    public HrApplication selectHrApplicationByApplicationId(String applicationId);

    /**
     * Query application list
     *
     * @param hrApplication application
     * @return application collection
     */
    public List<HrApplication> selectHrApplicationList(HrApplication hrApplication);

    /**
     * Add application
     *
     * @param hrApplication application
     * @return Result
     */
    public int insertHrApplication(HrApplication hrApplication);

    /**
     * Update application
     *
     * @param hrApplication application
     * @return Result
     */
    public int updateHrApplication(HrApplication hrApplication);

    /**
     * Batch delete application
     *
     * @param applicationIds application primary keys to be deleted
     * @return Result
     */
    public int deleteHrApplicationByApplicationIds(String[] applicationIds);

    /**
     * Delete application information
     *
     * @param applicationId application primary key
     * @return Result
     */
    public int deleteHrApplicationByApplicationId(String applicationId);
}
