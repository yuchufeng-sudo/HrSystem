package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrSignConfig;

import java.util.List;

/**
 * Electronic signature platform configuration Service Interface
 *
 * @author ys
 * @date 2025-06-12
 */
public interface IHrSignConfigService extends IService<HrSignConfig>
{
    /**
     * Query Electronic signature platform configuration
     *
     * @param id Electronic signature platform configuration primary key
     * @return Electronic signature platform configuration
     */
    public HrSignConfig selectHrSignConfigById(Long id);

    /**
     * Query Electronic signature platform configuration list
     *
     * @param hrSignConfig Electronic signature platform configuration
     * @return Electronic signature platform configuration collection
     */
    public List<HrSignConfig> selectHrSignConfigList(HrSignConfig hrSignConfig);

    /**
     * Add Electronic signature platform configuration
     *
     * @param hrSignConfig Electronic signature platform configuration
     * @return Result
     */
    public int insertHrSignConfig(HrSignConfig hrSignConfig);

    /**
     * Update Electronic signature platform configuration
     *
     * @param hrSignConfig Electronic signature platform configuration
     * @return Result
     */
    public int updateHrSignConfig(HrSignConfig hrSignConfig);

    /**
     * Batch delete Electronic signature platform configuration
     *
     * @param ids Electronic signature platform configuration primary keys to be deleted
     * @return Result
     */
    public int deleteHrSignConfigByIds(String[] ids);

    /**
     * Delete Electronic signature platform configuration information
     *
     * @param id Electronic signature platform configuration primary key
     * @return Result
     */
    public int deleteHrSignConfigById(String id);

    /**
     * According to  TypeQUERY
     * @param signType
     * @return
     */
    HrSignConfig selectBySignType(Integer signType);

    /**
     * MODIFYStatus
     */
    void updateByStatus();

    HrSignConfig selectConfigInfo(Integer signaturePlatformId);
}
