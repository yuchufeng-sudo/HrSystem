package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrSignPlatformType;

/**
 * Electronic signature platform type Mapper Interface
 *
 * @author ys
 * @date 2025-08-08
 */
public interface HrSignPlatformTypeMapper extends BaseMapper<HrSignPlatformType>
{
    /**
     * Query Electronic signature platform type
     *
     * @param id Electronic signature platform type primary key
     * @return Electronic signature platform type
     */
    public HrSignPlatformType selectHrSignPlatformTypeById(Integer id);
    
    /**
     * Query Electronic signature platform type list
     *
     * @param hrSignPlatformType Electronic signature platform type
     * @return Electronic signature platform type collection
     */
    public List<HrSignPlatformType> selectHrSignPlatformTypeList(HrSignPlatformType hrSignPlatformType);

}