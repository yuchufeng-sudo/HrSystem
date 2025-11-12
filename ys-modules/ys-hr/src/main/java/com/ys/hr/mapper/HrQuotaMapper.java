package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrQuota;

import java.util.List;

/**
 * Personnel Quota Management Mapper Interface
 *
 * @author ys
 * @date 2025-06-23
 */
public interface HrQuotaMapper extends BaseMapper<HrQuota>
{
    /**
     * Query Personnel Quota Management
     *
     * @param quotaId Personnel Quota Management primary key
     * @return Personnel Quota Management
     */
    public HrQuota selectHrQuotaByQuotaId(Long quotaId);

    /**
     * Query Personnel Quota Management list
     *
     * @param hrQuota Personnel Quota Management
     * @return Personnel Quota Management collection
     */
    public List<HrQuota> selectHrQuotaList(HrQuota hrQuota);

}
