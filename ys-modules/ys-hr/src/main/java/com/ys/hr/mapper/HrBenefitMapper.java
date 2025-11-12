package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrBenefit;

import java.util.List;

/**
 *  WELFARE BENEFITS Mapper Interface
 *
 * @author ys
 * @date 2025-06-09
 */
public interface HrBenefitMapper extends BaseMapper<HrBenefit>
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

}
