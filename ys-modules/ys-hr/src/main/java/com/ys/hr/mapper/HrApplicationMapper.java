package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrApplication;

import java.util.List;

/**
 * application Mapper Interface
 *
 * @author ys
 * @date 2025-06-17
 */
public interface HrApplicationMapper extends BaseMapper<HrApplication>
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

}
