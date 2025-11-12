package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrBenefitMapper;
import com.ys.hr.domain.HrBenefit;
import com.ys.hr.service.IHrBenefitService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 *  WELFARE BENEFITS Service Implementation
 *
 * @author ys
 * @date 2025-06-09
 */
@Service
public class HrBenefitServiceImpl extends ServiceImpl<HrBenefitMapper, HrBenefit> implements IHrBenefitService
{

    /**
     * Query  WELFARE BENEFITS
     *
     * @param benefitId  WELFARE BENEFITS primary key
     * @return  WELFARE BENEFITS
     */
    @Override
    public HrBenefit selectHrBenefitByBenefitId(String benefitId)
    {
        return baseMapper.selectHrBenefitByBenefitId(benefitId);
    }

    /**
     * Query  WELFARE BENEFITS list
     *
     * @param hrBenefit  WELFARE BENEFITS
     * @return  WELFARE BENEFITS
     */
    @Override
    public List<HrBenefit> selectHrBenefitList(HrBenefit hrBenefit)
    {
        return baseMapper.selectHrBenefitList(hrBenefit);
    }

    /**
     * Add  WELFARE BENEFITS
     *
     * @param hrBenefit  WELFARE BENEFITS
     * @return Result
     */
    @Override
    public int insertHrBenefit(HrBenefit hrBenefit)
    {
        hrBenefit.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrBenefit);
    }

    /**
     * Update  WELFARE BENEFITS
     *
     * @param hrBenefit  WELFARE BENEFITS
     * @return Result
     */
    @Override
    public int updateHrBenefit(HrBenefit hrBenefit)
    {
        hrBenefit.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrBenefit);
    }

    /**
     * Batch delete  WELFARE BENEFITS
     *
     * @param benefitIds  WELFARE BENEFITS primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrBenefitByBenefitIds(String[] benefitIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(benefitIds));
    }

    /**
     * Delete  WELFARE BENEFITS information
     *
     * @param benefitId  WELFARE BENEFITS primary key
     * @return Result
     */
    @Override
    public int deleteHrBenefitByBenefitId(String benefitId)
    {
        return baseMapper.deleteById(benefitId);
    }
}
