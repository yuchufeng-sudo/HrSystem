package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrBenefit;

import java.util.List;

/**
 *  welfare benefits Mapper Interface
 *
 * @author ys
 * @date 2025-06-09
 */
public interface HrBenefitMapper extends BaseMapper<HrBenefit>
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

}
