package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrSettings;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Hr settings Mapper Interface
 *
 * @author ys
 * @date 2025-05-28
 */
public interface HrSettingsMapper extends BaseMapper<HrSettings>
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

    HrSettings selectHrSettingsByEid(@Param("eid") String eid);
}
