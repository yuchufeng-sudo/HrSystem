package com.ys.hr.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrOnboardingResult;

/**
 * Onboarding Result Service Interface
 *
 * @author ys
 * @date 2025-10-14
 */
public interface IHrOnboardingResultService extends IService<HrOnboardingResult>
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

    /**
     * Add Onboarding Result
     *
     * @param hrOnboardingResult Onboarding Result
     * @return Result
     */
    public int insertHrOnboardingResult(HrOnboardingResult hrOnboardingResult);

    /**
     * Update Onboarding Result
     *
     * @param hrOnboardingResult Onboarding Result
     * @return Result
     */
    public int updateHrOnboardingResult(HrOnboardingResult hrOnboardingResult);

    /**
     * Batch delete Onboarding Result
     *
     * @param ids Onboarding Result primary keys to be deleted
     * @return Result
     */
    public int deleteHrOnboardingResultByIds(String[] ids);

    /**
     * Delete Onboarding Result information
     *
     * @param id Onboarding Result primary key
     * @return Result
     */
    public int deleteHrOnboardingResultById(String id);
}