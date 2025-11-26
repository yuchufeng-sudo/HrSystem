package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrBenefitType;

import java.util.List;

/**
 *  welfare type list Service Interface
 *
 * @author ys
 * @date 2025-06-09
 */
public interface IHrBenefitTypeService extends IService<HrBenefitType>
{
    /**
     * Query welfare type list
     *
     * @param benefitTypeId  welfare type list primary key
     * @return welfare type list
     */
    public HrBenefitType selectHrBenefitTypeByBenefitTypeId(String benefitTypeId);

    /**
     * Query welfare type list
     *
     * @param hrBenefitType  welfare type list
     * @return welfare type list collection
     */
    public List<HrBenefitType> selectHrBenefitTypeList(HrBenefitType hrBenefitType);

    /**
     * Add welfare type list
     *
     * @param hrBenefitType  welfare type list
     * @return Result
     */
    public int insertHrBenefitType(HrBenefitType hrBenefitType);

    /**
     * Update welfare type list
     *
     * @param hrBenefitType  welfare type list
     * @return Result
     */
    public int updateHrBenefitType(HrBenefitType hrBenefitType);

    /**
     * Batch delete  welfare type list
     *
     * @param benefitTypeIds  welfare type list primary keys to be deleted
     * @return Result
     */
    public int deleteHrBenefitTypeByBenefitTypeIds(String[] benefitTypeIds);

    /**
     * Delete welfare type list information
     *
     * @param benefitTypeId  welfare type list primary key
     * @return Result
     */
    public int deleteHrBenefitTypeByBenefitTypeId(String benefitTypeId);
}
