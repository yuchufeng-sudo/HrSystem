package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrOnboardingResultMapper;
import com.ys.hr.domain.HrOnboardingResult;
import com.ys.hr.service.IHrOnboardingResultService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Onboarding Result Service Implementation
 *
 * @author ys
 * @date 2025-10-14
 */
@Service
public class HrOnboardingResultServiceImpl extends ServiceImpl<HrOnboardingResultMapper, HrOnboardingResult> implements IHrOnboardingResultService
{

    /**
     * Query Onboarding Result
     *
     * @param id Onboarding Result primary key
     * @return Onboarding Result
     */
    @Override
    public HrOnboardingResult selectHrOnboardingResultById(String id)
    {
        return baseMapper.selectHrOnboardingResultById(id);
    }

    /**
     * Query Onboarding Result list
     *
     * @param hrOnboardingResult Onboarding Result
     * @return Onboarding Result
     */
    @Override
    public List<HrOnboardingResult> selectHrOnboardingResultList(HrOnboardingResult hrOnboardingResult)
    {
        return baseMapper.selectHrOnboardingResultList(hrOnboardingResult);
    }

    /**
     * Add Onboarding Result
     *
     * @param hrOnboardingResult Onboarding Result
     * @return Result
     */
    @Override
    public int insertHrOnboardingResult(HrOnboardingResult hrOnboardingResult)
    {
        hrOnboardingResult.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrOnboardingResult);
    }

    /**
     * Update Onboarding Result
     *
     * @param hrOnboardingResult Onboarding Result
     * @return Result
     */
    @Override
    public int updateHrOnboardingResult(HrOnboardingResult hrOnboardingResult)
    {
        hrOnboardingResult.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrOnboardingResult);
    }

    /**
     * Batch delete Onboarding Result
     *
     * @param ids Onboarding Result primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrOnboardingResultByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Onboarding Result information
     *
     * @param id Onboarding Result primary key
     * @return Result
     */
    @Override
    public int deleteHrOnboardingResultById(String id)
    {
        return baseMapper.deleteById(id);
    }
}
