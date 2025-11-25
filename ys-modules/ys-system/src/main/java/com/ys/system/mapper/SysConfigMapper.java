package com.ys.system.mapper;

import com.ys.system.domain.SysConfig;

import java.util.List;

/**
 * Parameter configuration data layer
 *
 * @author ys
 */
public interface SysConfigMapper
{
    /**
     * Query parameter configuration information
     *
     * @param config Parameter configuration information
     * @return Parameter configuration information
     */
    public SysConfig selectConfig(SysConfig config);

    /**
     * Query configuration by ID
     *
     * @param configId Parameter ID
     * @return Parameter configuration information
     */
    public SysConfig selectConfigById(Long configId);

    /**
     * Query parameter configuration list
     *
     * @param config Parameter configuration information
     * @return Parameter configuration set
     */
    public List<SysConfig> selectConfigList(SysConfig config);

    /**
     * Query parameter configuration information by key "name"
     *
     * @param configKey Parameter key name
     * @return Parameter configuration information
     */
    public SysConfig checkConfigKeyUnique(String configKey);

    /**
     * Add parameter configuration
     *
     * @param config Parameter configuration information
     * @return Result
     */
    public int insertConfig(SysConfig config);

    /**
     * Update parameter configuration
     *
     * @param config Parameter configuration information
     * @return Result
     */
    public int updateConfig(SysConfig config);

    /**
     * Delete parameter configuration
     *
     * @param configId Parameter ID
     * @return Result
     */
    public int deleteConfigById(Long configId);

    /**
     * Batch delete parameter information
     *
     * @param configIds Parameter IDs to be deleted
     * @return Result
     */
    public int deleteConfigByIds(Long[] configIds);
}
