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
 *  WELFARE TYPE   LIST Service Implementation
 *
 * @author ys
 * @date 2025-06-09
 */
@Service
public class HrBenefitTypeServiceImpl extends ServiceImpl<HrBenefitTypeMapper, HrBenefitType> implements IHrBenefitTypeService
{

    /**
     * Query  WELFARE TYPE   LIST
     *
     * @param benefitTypeId  WELFARE TYPE   LIST primary key
     * @return  WELFARE TYPE   LIST
     */
    @Override
    public HrBenefitType selectHrBenefitTypeByBenefitTypeId(String benefitTypeId)
    {
        return baseMapper.selectHrBenefitTypeByBenefitTypeId(benefitTypeId);
    }

    /**
     * Query  WELFARE TYPE   LIST list
     *
     * @param hrBenefitType  WELFARE TYPE   LIST
     * @return  WELFARE TYPE   LIST
     */
    @Override
    public List<HrBenefitType> selectHrBenefitTypeList(HrBenefitType hrBenefitType)
    {
        return baseMapper.selectHrBenefitTypeList(hrBenefitType);
    }

    /**
     * Add  WELFARE TYPE   LIST
     *
     * @param hrBenefitType  WELFARE TYPE   LIST
     * @return Result
     */
    @Override
    public int insertHrBenefitType(HrBenefitType hrBenefitType)
    {
        hrBenefitType.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrBenefitType);
    }

    /**
     * Update  WELFARE TYPE   LIST
     *
     * @param hrBenefitType  WELFARE TYPE   LIST
     * @return Result
     */
    @Override
    public int updateHrBenefitType(HrBenefitType hrBenefitType)
    {
        hrBenefitType.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrBenefitType);
    }

    /**
     * Batch delete  WELFARE TYPE   LIST
     *
     * @param benefitTypeIds  WELFARE TYPE   LIST primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrBenefitTypeByBenefitTypeIds(String[] benefitTypeIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(benefitTypeIds));
    }

    /**
     * Delete  WELFARE TYPE   LIST information
     *
     * @param benefitTypeId  WELFARE TYPE   LIST primary key
     * @return Result
     */
    @Override
    public int deleteHrBenefitTypeByBenefitTypeId(String benefitTypeId)
    {
        return baseMapper.deleteById(benefitTypeId);
    }
}
