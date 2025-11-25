package com.ys.system.service;

import com.ys.system.domain.SysConfig;

import java.util.List;

/**
 * Parameters configuration - Service layer
 *
 * @author ys
 */
public interface ISysConfigService
{
    /**
     * Query PARAMETERS CONFIGURATION Information
     *
     * @param configId Parameters Configuration ID
     * @return Parameters Configuration Information
     */
    public SysConfig selectConfigById(Long configId);

    /**
     * According to the key "Name" in Query PARAMETERS CONFIGURATION Information
     *
     * @param configKey Parameters keyName
     * @return Parameters Key-Value
     */
    public String selectConfigByKey(String configKey);

    /**
     * Query PARAMETERS CONFIGURATION list
     *
     * @param config Parameters Configuration Information
     * @return Parameters Configuration Set
     */
    public List<SysConfig> selectConfigList(SysConfig config);

    /**
     * Add Parameters Configuration
     *
     * @param config Parameters Configuration Information
     * @return Result
     */
    public int insertConfig(SysConfig config);

    /**
     * Update Parameters Configuration
     *
     * @param config Parameters Configuration Information
     * @return Result
     */
    public int updateConfig(SysConfig config);

    /**
     * Batch Delete Parameters Information
     *
     * @param configIds Parameters ID to be DELETED
     */
    public void deleteConfigByIds(Long[] configIds);

    /**
     * Load Parameters Cache Data
     */
    public void loadingConfigCache();

    /**
     * Clear Parameters Cache Data
     */
    public void clearConfigCache();

    /**
     * RESET Parameters Cache Data
     */
    public void resetConfigCache();

    /**
     *  Verify Whether the keyName Parameter Is Unique
     *
     * @param config Parameters Information
     * @return Result
     */
    public boolean checkConfigKeyUnique(SysConfig config);
}
