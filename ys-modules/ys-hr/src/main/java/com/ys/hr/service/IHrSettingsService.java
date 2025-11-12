package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrSettings;

import java.util.List;

/**
 * Hr settings Service Interface
 *
 * @author ys
 * @date 2025-05-28
 */
public interface IHrSettingsService extends IService<HrSettings>
{
    /**
     * Query Hr settings
     *
     * @param id Hr settings primary key
     * @return Hr settings
     */
    public HrSettings selectHrSettingsById(Long id);

    /**
     * Query Hr settings list
     *
     * @param hrSettings Hr settings
     * @return Hr settings collection
     */
    public List<HrSettings> selectHrSettingsList(HrSettings hrSettings);

    /**
     * Add Hr settings
     *
     * @param hrSettings Hr settings
     * @return Result
     */
    public int insertHrSettings(HrSettings hrSettings);

    /**
     * Update Hr settings
     *
     * @param hrSettings Hr settings
     * @return Result
     */
    public int updateHrSettings(HrSettings hrSettings);

    /**
     * Batch delete Hr settings
     *
     * @param ids Hr settings primary keys to be deleted
     * @return Result
     */
    public int deleteHrSettingsByIds(Long[] ids);

    /**
     * Delete Hr settings information
     *
     * @param id Hr settings primary key
     * @return Result
     */
    public int deleteHrSettingsById(Long id);

    HrSettings selectHrSettingsByEid(String eid);
}
