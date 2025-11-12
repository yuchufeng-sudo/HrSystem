package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrEmpBenefit;

import java.util.List;

/**
 *   EMPLOYEE WELFARE APPLICATION FORM Mapper Interface
 *
 * @author ys
 * @date 2025-06-09
 */
public interface HrEmpBenefitMapper extends BaseMapper<HrEmpBenefit>
{
    /**
     * Query   EMPLOYEE WELFARE APPLICATION FORM
     *
     * @param benefitEmpId   EMPLOYEE WELFARE APPLICATION FORM primary key
     * @return   EMPLOYEE WELFARE APPLICATION FORM
     */
    public HrEmpBenefit selectHrEmpBenefitByBenefitEmpId(String benefitEmpId);

    /**
     * Query   EMPLOYEE WELFARE APPLICATION FORM list
     *
     * @param hrEmpBenefit   EMPLOYEE WELFARE APPLICATION FORM
     * @return   EMPLOYEE WELFARE APPLICATION FORM collection
     */
    public List<HrEmpBenefit> selectHrEmpBenefitList(HrEmpBenefit hrEmpBenefit);

}
