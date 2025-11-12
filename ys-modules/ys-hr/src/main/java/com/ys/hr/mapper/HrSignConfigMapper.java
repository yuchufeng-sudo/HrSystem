package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrSignConfig;

import java.util.List;

/**
 * Electronic signature platform configuration Mapper Interface
 *
 * @author ys
 * @date 2025-06-12
 */
public interface HrSignConfigMapper extends BaseMapper<HrSignConfig>
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
