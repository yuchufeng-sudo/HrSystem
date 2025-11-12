package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrSignPlatformTypeMapper;
import com.ys.hr.domain.HrSignPlatformType;
import com.ys.hr.service.IHrSignPlatformTypeService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Electronic signature platform type Service Implementation
 *
 * @author ys
 * @date 2025-08-08
 */
@Service
public class HrSignPlatformTypeServiceImpl extends ServiceImpl<HrSignPlatformTypeMapper, HrSignPlatformType> implements IHrSignPlatformTypeService
{

    /**
     * Query Electronic signature platform type
     *
     * @param id Electronic signature platform type primary key
     * @return Electronic signature platform type
     */
    @Override
    public HrSignPlatformType selectHrSignPlatformTypeById(Integer id)
    {
        return baseMapper.selectHrSignPlatformTypeById(id);
    }

    /**
     * Query Electronic signature platform type list
     *
     * @param hrSignPlatformType Electronic signature platform type
     * @return Electronic signature platform type
     */
    @Override
    public List<HrSignPlatformType> selectHrSignPlatformTypeList(HrSignPlatformType hrSignPlatformType)
    {
        return baseMapper.selectHrSignPlatformTypeList(hrSignPlatformType);
    }

    /**
     * Add Electronic signature platform type
     *
     * @param hrSignPlatformType Electronic signature platform type
     * @return Result
     */
    @Override
    public int insertHrSignPlatformType(HrSignPlatformType hrSignPlatformType)
    {
        hrSignPlatformType.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrSignPlatformType);
    }

    /**
     * Update Electronic signature platform type
     *
     * @param hrSignPlatformType Electronic signature platform type
     * @return Result
     */
    @Override
    public int updateHrSignPlatformType(HrSignPlatformType hrSignPlatformType)
    {
        hrSignPlatformType.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrSignPlatformType);
    }

    /**
     * Batch delete Electronic signature platform type
     *
     * @param ids Electronic signature platform type primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrSignPlatformTypeByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Electronic signature platform type information
     *
     * @param id Electronic signature platform type primary key
     * @return Result
     */
    @Override
    public int deleteHrSignPlatformTypeById(String id)
    {
        return baseMapper.deleteById(id);
    }
}
