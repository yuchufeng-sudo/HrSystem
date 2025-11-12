package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrBenefit;

import java.util.List;

/**
 *  WELFARE BENEFITS Service Interface
 *
 * @author ys
 * @date 2025-06-09
 */
public interface IHrBenefitService extends IService<HrBenefit>
{
    /**
     * Query  WELFARE BENEFITS
     *
     * @param benefitId  WELFARE BENEFITS primary key
     * @return  WELFARE BENEFITS
     */
    public HrBenefit selectHrBenefitByBenefitId(String benefitId);

    /**
     * Query  WELFARE BENEFITS list
     *
     * @param hrBenefit  WELFARE BENEFITS
     * @return  WELFARE BENEFITS collection
     */
    public List<HrBenefit> selectHrBenefitList(HrBenefit hrBenefit);

    /**
     * Add  WELFARE BENEFITS
     *
     * @param hrBenefit  WELFARE BENEFITS
     * @return Result
     */
    public int insertHrBenefit(HrBenefit hrBenefit);

    /**
     * Update  WELFARE BENEFITS
     *
     * @param hrBenefit  WELFARE BENEFITS
     * @return Result
     */
    public int updateHrBenefit(HrBenefit hrBenefit);

    /**
     * Batch delete  WELFARE BENEFITS
     *
     * @param benefitIds  WELFARE BENEFITS primary keys to be deleted
     * @return Result
     */
    public int deleteHrBenefitByBenefitIds(String[] benefitIds);

    /**
     * Delete  WELFARE BENEFITS information
     *
     * @param benefitId  WELFARE BENEFITS primary key
     * @return Result
     */
    public int deleteHrBenefitByBenefitId(String benefitId);
}
