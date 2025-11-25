package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrBenefitType;

import java.util.List;

/**
 *  welfare type list Mapper Interface
 *
 * @author ys
 * @date 2025-06-09
 */
public interface HrBenefitTypeMapper extends BaseMapper<HrBenefitType>
{
    /**
     * Query welfare type list
     *
     * @param benefitTypeId  welfare type list primary key
     * @return  welfare type list
     */
    public HrBenefitType selectHrBenefitTypeByBenefitTypeId(String benefitTypeId);

    /**
     * Query welfare type list
     *
     * @param hrBenefitType  welfare type list
     * @return  welfare type list collection
     */
    public List<HrBenefitType> selectHrBenefitTypeList(HrBenefitType hrBenefitType);

}
