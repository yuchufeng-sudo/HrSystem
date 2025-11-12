package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmpBenefit;

import java.util.List;

/**
 *   EMPLOYEE WELFARE APPLICATION FORM Service Interface
 *
 * @author ys
 * @date 2025-06-09
 */
public interface IHrEmpBenefitService extends IService<HrEmpBenefit>
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

    /**
     * Add   EMPLOYEE WELFARE APPLICATION FORM
     *
     * @param hrEmpBenefit   EMPLOYEE WELFARE APPLICATION FORM
     * @return Result
     */
    public int insertHrEmpBenefit(HrEmpBenefit hrEmpBenefit);

    /**
     * Update   EMPLOYEE WELFARE APPLICATION FORM
     *
     * @param hrEmpBenefit   EMPLOYEE WELFARE APPLICATION FORM
     * @return Result
     */
    public int updateHrEmpBenefit(HrEmpBenefit hrEmpBenefit);

    /**
     * Batch delete   EMPLOYEE WELFARE APPLICATION FORM
     *
     * @param benefitEmpIds   EMPLOYEE WELFARE APPLICATION FORM primary keys to be deleted
     * @return Result
     */
    public int deleteHrEmpBenefitByBenefitEmpIds(String[] benefitEmpIds);

    /**
     * Delete   EMPLOYEE WELFARE APPLICATION FORM information
     *
     * @param benefitEmpId   EMPLOYEE WELFARE APPLICATION FORM primary key
     * @return Result
     */
    public int deleteHrEmpBenefitByBenefitEmpId(String benefitEmpId);
}
