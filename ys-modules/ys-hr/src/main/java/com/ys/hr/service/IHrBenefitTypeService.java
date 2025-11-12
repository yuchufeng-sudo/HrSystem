package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrBenefitType;

import java.util.List;

/**
 *  WELFARE TYPE   LIST Service Interface
 *
 * @author ys
 * @date 2025-06-09
 */
public interface IHrBenefitTypeService extends IService<HrBenefitType>
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

    /**
     * Add  WELFARE TYPE   LIST
     *
     * @param hrBenefitType  WELFARE TYPE   LIST
     * @return Result
     */
    public int insertHrBenefitType(HrBenefitType hrBenefitType);

    /**
     * Update  WELFARE TYPE   LIST
     *
     * @param hrBenefitType  WELFARE TYPE   LIST
     * @return Result
     */
    public int updateHrBenefitType(HrBenefitType hrBenefitType);

    /**
     * Batch delete  WELFARE TYPE   LIST
     *
     * @param benefitTypeIds  WELFARE TYPE   LIST primary keys to be deleted
     * @return Result
     */
    public int deleteHrBenefitTypeByBenefitTypeIds(String[] benefitTypeIds);

    /**
     * Delete  WELFARE TYPE   LIST information
     *
     * @param benefitTypeId  WELFARE TYPE   LIST primary key
     * @return Result
     */
    public int deleteHrBenefitTypeByBenefitTypeId(String benefitTypeId);
}
