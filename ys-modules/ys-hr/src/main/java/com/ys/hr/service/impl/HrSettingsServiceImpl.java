package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrSettingsMapper;
import com.ys.hr.domain.HrSettings;
import com.ys.hr.service.IHrSettingsService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Hr settings Service Implementation
 *
 * @author ys
 * @date 2025-05-28
 */
@Service
public class HrSettingsServiceImpl extends ServiceImpl<HrSettingsMapper, HrSettings> implements IHrSettingsService
{

    /**
     * Query Hr settings
     *
     * @param id Hr settings primary key
     * @return Hr settings
     */
    @Override
    public HrSettings selectHrSettingsById(Long id)
    {
        return baseMapper.selectHrSettingsById(id);
    }

    /**
     * Query Hr settings list
     *
     * @param hrSettings Hr settings
     * @return Hr settings
     */
    @Override
    public List<HrSettings> selectHrSettingsList(HrSettings hrSettings)
    {
        return baseMapper.selectHrSettingsList(hrSettings);
    }

    /**
     * Add Hr settings
     *
     * @param hrSettings Hr settings
     * @return Result
     */
    @Override
    public int insertHrSettings(HrSettings hrSettings)
    {
        hrSettings.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrSettings);
    }

    /**
     * Update Hr settings
     *
     * @param hrSettings Hr settings
     * @return Result
     */
    @Override
    public int updateHrSettings(HrSettings hrSettings)
    {
        hrSettings.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrSettings);
    }

    /**
     * Batch delete Hr settings
     *
     * @param ids Hr settings primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrSettingsByIds(Long[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Hr settings information
     *
     * @param id Hr settings primary key
     * @return Result
     */
    @Override
    public int deleteHrSettingsById(Long id)
    {
        return baseMapper.deleteById(id);
    }

    @Override
    public HrSettings selectHrSettingsByEid(String eid) {
        return baseMapper.selectHrSettingsByEid(eid);
    }
}