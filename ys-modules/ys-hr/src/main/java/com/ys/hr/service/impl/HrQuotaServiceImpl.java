package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.hr.domain.HrCandidateInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrQuotaMapper;
import com.ys.hr.domain.HrQuota;
import com.ys.hr.service.IHrQuotaService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Personnel Quota Management Service Implementation
 *
 * @author ys
 * @date 2025-06-23
 */
@Service
public class HrQuotaServiceImpl extends ServiceImpl<HrQuotaMapper, HrQuota> implements IHrQuotaService
{

    @Autowired
    private HrCandidateInfoServiceImpl candidateInfoService;

    /**
     * Query Personnel Quota Management
     *
     * @param quotaId Personnel Quota Management primary key
     * @return Personnel Quota Management
     */
    @Override
    public HrQuota selectHrQuotaByQuotaId(Long quotaId)
    {
        return baseMapper.selectHrQuotaByQuotaId(quotaId);
    }

    /**
     * Query Personnel Quota Management list
     *
     * @param hrQuota Personnel Quota Management
     * @return Personnel Quota Management
     */
    @Override
    public List<HrQuota> selectHrQuotaList(HrQuota hrQuota)
    {
        List<HrQuota> hrQuotas = baseMapper.selectHrQuotaList(hrQuota);
        hrQuotas.forEach(temp -> {
            HrCandidateInfo info = new HrCandidateInfo();
            info.setJobInformation(temp.getPostId());
            info.setCandidateStatus("3");
            List<HrCandidateInfo> hrCandidateInfos = candidateInfoService.selectHrCandidateInfoList(info);
            temp.setQuotaNumberExist((long) hrCandidateInfos.size());
        });
        return hrQuotas;

    }

    /**
     * Add Personnel Quota Management
     *
     * @param hrQuota Personnel Quota Management
     * @return Result
     */
    @Override
    public int insertHrQuota(HrQuota hrQuota)
    {
        hrQuota.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrQuota);
    }

    /**
     * Update Personnel Quota Management
     *
     * @param hrQuota Personnel Quota Management
     * @return Result
     */
    @Override
    public int updateHrQuota(HrQuota hrQuota)
    {
        hrQuota.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrQuota);
    }

    /**
     * Batch delete Personnel Quota Management
     *
     * @param quotaIds Personnel Quota Management primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrQuotaByQuotaIds(String[] quotaIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(quotaIds));
    }

    /**
     * Delete Personnel Quota Management information
     *
     * @param quotaId Personnel Quota Management primary key
     * @return Result
     */
    @Override
    public int deleteHrQuotaByQuotaId(String quotaId)
    {
        return baseMapper.deleteById(quotaId);
    }
}
