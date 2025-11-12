package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrBenefitType;

import java.util.List;

/**
 *  WELFARE TYPE   LIST Mapper Interface
 *
 * @author ys
 * @date 2025-06-09
 */
public interface HrBenefitTypeMapper extends BaseMapper<HrBenefitType>
{
    /**
     * Query  WELFARE TYPE   LIST
     *
     * @param benefitTypeId  WELFARE TYPE   LIST primary key
     * @return  WELFARE TYPE   LIST
     */
    public HrBenefitType selectHrBenefitTypeByBenefitTypeId(String benefitTypeId);

    /**
     * Query  WELFARE TYPE   LIST list
     *
     * @param hrBenefitType  WELFARE TYPE   LIST
     * @return  WELFARE TYPE   LIST collection
     */
    public List<HrBenefitType> selectHrBenefitTypeList(HrBenefitType hrBenefitType);

}
