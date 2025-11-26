package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrBenefit;

import java.util.List;

/**
 *  welfare benefits Service Interface
 *
 * @author ys
 * @date 2025-06-09
 */
public interface IHrBenefitService extends IService<HrBenefit>
{
    /**
     * Query welfare benefits
     *
     * @param benefitId  welfare benefits primary key
     * @return welfare benefits
     */
    public HrBenefit selectHrBenefitByBenefitId(String benefitId);

    /**
     * Query welfare benefits list
     *
     * @param hrBenefit  welfare benefits
     * @return welfare benefits collection
     */
    public List<HrBenefit> selectHrBenefitList(HrBenefit hrBenefit);

    /**
     * Add welfare benefits
     *
     * @param hrBenefit  welfare benefits
     * @return Result
     */
    public int insertHrBenefit(HrBenefit hrBenefit);

    /**
     * Update welfare benefits
     *
     * @param hrBenefit  welfare benefits
     * @return Result
     */
    public int updateHrBenefit(HrBenefit hrBenefit);

    /**
     * Batch delete  welfare benefits
     *
     * @param benefitIds  welfare benefits primary keys to be deleted
     * @return Result
     */
    public int deleteHrBenefitByBenefitIds(String[] benefitIds);

    /**
     * Delete welfare benefits information
     *
     * @param benefitId  welfare benefits primary key
     * @return Result
     */
    public int deleteHrBenefitByBenefitId(String benefitId);
}
