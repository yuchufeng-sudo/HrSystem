package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrSignPlatformType;

import java.util.List;

/**
 * Electronic signature platform type Service Interface
 *
 * @author ys
 * @date 2025-08-08
 */
public interface IHrSignPlatformTypeService extends IService<HrSignPlatformType>
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

    /**
     * Add Electronic signature platform type
     *
     * @param hrSignPlatformType Electronic signature platform type
     * @return Result
     */
    public int insertHrSignPlatformType(HrSignPlatformType hrSignPlatformType);

    /**
     * Update Electronic signature platform type
     *
     * @param hrSignPlatformType Electronic signature platform type
     * @return Result
     */
    public int updateHrSignPlatformType(HrSignPlatformType hrSignPlatformType);

    /**
     * Batch delete Electronic signature platform type
     *
     * @param ids Electronic signature platform type primary keys to be deleted
     * @return Result
     */
    public int deleteHrSignPlatformTypeByIds(String[] ids);

    /**
     * Delete Electronic signature platform type information
     *
     * @param id Electronic signature platform type primary key
     * @return Result
     */
    public int deleteHrSignPlatformTypeById(String id);
}
