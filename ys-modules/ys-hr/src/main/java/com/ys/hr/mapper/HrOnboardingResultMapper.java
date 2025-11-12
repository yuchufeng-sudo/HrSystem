package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrOnboardingResult;

/**
 * Onboarding Result Mapper Interface
 *
 * @author ys
 * @date 2025-10-14
 */
public interface HrOnboardingResultMapper extends BaseMapper<HrOnboardingResult>
{
    /**
     * Query Onboarding Result
     *
     * @param id Onboarding Result primary key
     * @return Onboarding Result
     */
    public HrOnboardingResult selectHrOnboardingResultById(String id);
    
    /**
     * Query Onboarding Result list
     *
     * @param hrOnboardingResult Onboarding Result
     * @return Onboarding Result collection
     */
    public List<HrOnboardingResult> selectHrOnboardingResultList(HrOnboardingResult hrOnboardingResult);

}