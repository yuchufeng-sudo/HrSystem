package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrBenefitTypeMapper;
import com.ys.hr.domain.HrBenefitType;
import com.ys.hr.service.IHrBenefitTypeService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 *  welfare type list Service Implementation
 *
 * @author ys
 * @date 2025-06-09
 */
@Service
public class HrBenefitTypeServiceImpl extends ServiceImpl<HrBenefitTypeMapper, HrBenefitType> implements IHrBenefitTypeService
{

    /**
     * Query welfare type list
     *
     * @param benefitTypeId  welfare type list primary key
     * @return  welfare type list
     */
    @Override
    public HrBenefitType selectHrBenefitTypeByBenefitTypeId(String benefitTypeId)
    {
        return baseMapper.selectHrBenefitTypeByBenefitTypeId(benefitTypeId);
    }

    /**
     * Query welfare type list
     *
     * @param hrBenefitType  welfare type list
     * @return  welfare type list
     */
    @Override
    public List<HrBenefitType> selectHrBenefitTypeList(HrBenefitType hrBenefitType)
    {
        return baseMapper.selectHrBenefitTypeList(hrBenefitType);
    }

    /**
     * Add welfare type list
     *
     * @param hrBenefitType  welfare type list
     * @return Result
     */
    @Override
    public int insertHrBenefitType(HrBenefitType hrBenefitType)
    {
        hrBenefitType.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrBenefitType);
    }

    /**
     * Update welfare type list
     *
     * @param hrBenefitType  welfare type list
     * @return Result
     */
    @Override
    public int updateHrBenefitType(HrBenefitType hrBenefitType)
    {
        hrBenefitType.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrBenefitType);
    }

    /**
     * Batch delete  welfare type list
     *
     * @param benefitTypeIds  welfare type list primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrBenefitTypeByBenefitTypeIds(String[] benefitTypeIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(benefitTypeIds));
    }

    /**
     * Delete welfare type list information
     *
     * @param benefitTypeId  welfare type list primary key
     * @return Result
     */
    @Override
    public int deleteHrBenefitTypeByBenefitTypeId(String benefitTypeId)
    {
        return baseMapper.deleteById(benefitTypeId);
    }
}
