package com.ys.system.service;

import com.ys.system.domain.SysConfig;

import java.util.List;

/**
 * Parameters configuration - Service layer
 *
 * @author ruoyi
 */
public interface ISysConfigService
{
    /**
     * QUERY PARAMETERS CONFIGURATION INFORMATION
     *
     * @param configId Parameters Configuration ID
     * @return Parameters Configuration INFORMATION
     */
    public SysConfig selectConfigById(Long configId);

    /**
     * According to the key "Name" in QUERY PARAMETERS CONFIGURATION INFORMATION
     *
     * @param configKey Parameters keyName
     * @return Parameters Key-Value
     */
    public String selectConfigByKey(String configKey);

    /**
     * QUERY PARAMETERS CONFIGURATION LIST
     *
     * @param config Parameters Configuration INFORMATION
     * @return Parameters Configuration Set
     */
    public List<SysConfig> selectConfigList(SysConfig config);

    /**
     * ADD Parameters Configuration
     *
     * @param config Parameters Configuration INFORMATION
     * @return Result
     */
    public int insertConfig(SysConfig config);

    /**
     * MODIFY Parameters Configuration
     *
     * @param config Parameters Configuration INFORMATION
     * @return Result
     */
    public int updateConfig(SysConfig config);

    /**
     * Batch DELETE Parameters INFORMATION
     *
     * @param configIds Parameters ID to be DELETED
     */
    public void deleteConfigByIds(Long[] configIds);

    /**
     * Load Parameters Cache Data
     */
    public void loadingConfigCache();

    /**
     * CLEAR Parameters Cache Data
     */
    public void clearConfigCache();

    /**
     * RESET Parameters Cache Data
     */
    public void resetConfigCache();

    /**
     *  Verify Whether the keyName Parameter Is Unique
     *
     * @param config Parameters INFORMATION
     * @return Result
     */
    public boolean checkConfigKeyUnique(SysConfig config);
}
