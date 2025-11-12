package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrBenefitGroupMapper;
import com.ys.hr.domain.HrBenefitGroup;
import com.ys.hr.service.IHrBenefitGroupService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Table of benefit types Service Implementation
 *
 * @author ys
 * @date 2025-07-10
 */
@Service
public class HrBenefitGroupServiceImpl extends ServiceImpl<HrBenefitGroupMapper, HrBenefitGroup> implements IHrBenefitGroupService
{

    /**
     * Query Table of benefit types
     *
     * @param benefitGroupId Table of benefit types primary key
     * @return Table of benefit types
     */
    @Override
    public HrBenefitGroup selectHrBenefitGroupByBenefitGroupId(String benefitGroupId)
    {
        return baseMapper.selectHrBenefitGroupByBenefitGroupId(benefitGroupId);
    }

    /**
     * Query Table of benefit types list
     *
     * @param hrBenefitGroup Table of benefit types
     * @return Table of benefit types
     */
    @Override
    public List<HrBenefitGroup> selectHrBenefitGroupList(HrBenefitGroup hrBenefitGroup)
    {
        return baseMapper.selectHrBenefitGroupList(hrBenefitGroup);
    }

    /**
     * Add Table of benefit types
     *
     * @param hrBenefitGroup Table of benefit types
     * @return Result
     */
    @Override
    public int insertHrBenefitGroup(HrBenefitGroup hrBenefitGroup)
    {
        hrBenefitGroup.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrBenefitGroup);
    }

    /**
     * Update Table of benefit types
     *
     * @param hrBenefitGroup Table of benefit types
     * @return Result
     */
    @Override
    public int updateHrBenefitGroup(HrBenefitGroup hrBenefitGroup)
    {
        hrBenefitGroup.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrBenefitGroup);
    }

    /**
     * Batch delete Table of benefit types
     *
     * @param benefitGroupIds Table of benefit types primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrBenefitGroupByBenefitGroupIds(String[] benefitGroupIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(benefitGroupIds));
    }

    /**
     * Delete Table of benefit types information
     *
     * @param benefitGroupId Table of benefit types primary key
     * @return Result
     */
    @Override
    public int deleteHrBenefitGroupByBenefitGroupId(String benefitGroupId)
    {
        return baseMapper.deleteById(benefitGroupId);
    }
}
