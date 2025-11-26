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
 *  welfare benefits Service Implementation
 *
 * @author ys
 * @date 2025-06-09
 */
@Service
public class HrBenefitServiceImpl extends ServiceImpl<HrBenefitMapper, HrBenefit> implements IHrBenefitService
{

    /**
     * Query welfare benefits
     *
     * @param benefitId  welfare benefits primary key
     * @return welfare benefits
     */
    @Override
    public HrBenefit selectHrBenefitByBenefitId(String benefitId)
    {
        return baseMapper.selectHrBenefitByBenefitId(benefitId);
    }

    /**
     * Query welfare benefits list
     *
     * @param hrBenefit  welfare benefits
     * @return welfare benefits
     */
    @Override
    public List<HrBenefit> selectHrBenefitList(HrBenefit hrBenefit)
    {
        return baseMapper.selectHrBenefitList(hrBenefit);
    }

    /**
     * Add welfare benefits
     *
     * @param hrBenefit  welfare benefits
     * @return Result
     */
    @Override
    public int insertHrBenefit(HrBenefit hrBenefit)
    {
        hrBenefit.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrBenefit);
    }

    /**
     * Update welfare benefits
     *
     * @param hrBenefit  welfare benefits
     * @return Result
     */
    @Override
    public int updateHrBenefit(HrBenefit hrBenefit)
    {
        hrBenefit.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrBenefit);
    }

    /**
     * Batch delete  welfare benefits
     *
     * @param benefitIds  welfare benefits primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrBenefitByBenefitIds(String[] benefitIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(benefitIds));
    }

    /**
     * Delete welfare benefits information
     *
     * @param benefitId  welfare benefits primary key
     * @return Result
     */
    @Override
    public int deleteHrBenefitByBenefitId(String benefitId)
    {
        return baseMapper.deleteById(benefitId);
    }
}
