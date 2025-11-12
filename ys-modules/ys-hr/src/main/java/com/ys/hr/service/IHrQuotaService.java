package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrQuota;

import java.util.List;

/**
 * Personnel Quota Management Service Interface
 *
 * @author ys
 * @date 2025-06-23
 */
public interface IHrQuotaService extends IService<HrQuota>
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

    /**
     * Add Personnel Quota Management
     *
     * @param hrQuota Personnel Quota Management
     * @return Result
     */
    public int insertHrQuota(HrQuota hrQuota);

    /**
     * Update Personnel Quota Management
     *
     * @param hrQuota Personnel Quota Management
     * @return Result
     */
    public int updateHrQuota(HrQuota hrQuota);

    /**
     * Batch delete Personnel Quota Management
     *
     * @param quotaIds Personnel Quota Management primary keys to be deleted
     * @return Result
     */
    public int deleteHrQuotaByQuotaIds(String[] quotaIds);

    /**
     * Delete Personnel Quota Management information
     *
     * @param quotaId Personnel Quota Management primary key
     * @return Result
     */
    public int deleteHrQuotaByQuotaId(String quotaId);
}
