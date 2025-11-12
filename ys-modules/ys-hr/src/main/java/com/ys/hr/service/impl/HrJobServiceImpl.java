package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrJobMapper;
import com.ys.hr.domain.HrJob;
import com.ys.hr.service.IHrJobService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Job Management Service Implementation
 *
 * @author ys
 * @date 2025-06-24
 */
@Service
public class HrJobServiceImpl extends ServiceImpl<HrJobMapper, HrJob> implements IHrJobService
{

    /**
     * Query Job Management
     *
     * @param id Job Management primary key
     * @return Job Management
     */
    @Override
    public HrJob selectHrJobById(Long id)
    {
        return baseMapper.selectHrJobById(id);
    }

    /**
     * Query Job Management list
     *
     * @param hrJob Job Management
     * @return Job Management
     */
    @Override
    public List<HrJob> selectHrJobList(HrJob hrJob)
    {
        return baseMapper.selectHrJobList(hrJob);
    }

    /**
     * Add Job Management
     *
     * @param hrJob Job Management
     * @return Result
     */
    @Override
    public int insertHrJob(HrJob hrJob)
    {
        hrJob.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrJob);
    }

    /**
     * Update Job Management
     *
     * @param hrJob Job Management
     * @return Result
     */
    @Override
    public int updateHrJob(HrJob hrJob)
    {
        hrJob.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrJob);
    }

    @Override
    public boolean deleteHrJobByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids));
    }
}
