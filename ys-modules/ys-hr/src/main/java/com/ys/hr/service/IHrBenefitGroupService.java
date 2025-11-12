package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrBenefitGroup;

import java.util.List;

/**
 * Table of benefit types Service Interface
 *
 * @author ys
 * @date 2025-07-10
 */
public interface IHrBenefitGroupService extends IService<HrBenefitGroup>
{
    /**
     * Query Table of benefit types
     *
     * @param benefitGroupId Table of benefit types primary key
     * @return Table of benefit types
     */
    public HrBenefitGroup selectHrBenefitGroupByBenefitGroupId(String benefitGroupId);

    /**
     * Query Table of benefit types list
     *
     * @param hrBenefitGroup Table of benefit types
     * @return Table of benefit types collection
     */
    public List<HrBenefitGroup> selectHrBenefitGroupList(HrBenefitGroup hrBenefitGroup);

    /**
     * Add Table of benefit types
     *
     * @param hrBenefitGroup Table of benefit types
     * @return Result
     */
    public int insertHrBenefitGroup(HrBenefitGroup hrBenefitGroup);

    /**
     * Update Table of benefit types
     *
     * @param hrBenefitGroup Table of benefit types
     * @return Result
     */
    public int updateHrBenefitGroup(HrBenefitGroup hrBenefitGroup);

    /**
     * Batch delete Table of benefit types
     *
     * @param benefitGroupIds Table of benefit types primary keys to be deleted
     * @return Result
     */
    public int deleteHrBenefitGroupByBenefitGroupIds(String[] benefitGroupIds);

    /**
     * Delete Table of benefit types information
     *
     * @param benefitGroupId Table of benefit types primary key
     * @return Result
     */
    public int deleteHrBenefitGroupByBenefitGroupId(String benefitGroupId);
}
