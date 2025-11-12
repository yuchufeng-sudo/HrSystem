package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrSignConfigMapper;
import com.ys.hr.domain.HrSignConfig;
import com.ys.hr.service.IHrSignConfigService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Electronic signature platform configuration Service Implementation
 *
 * @author ys
 * @date 2025-06-12
 */
@Service
public class HrSignConfigServiceImpl extends ServiceImpl<HrSignConfigMapper, HrSignConfig> implements IHrSignConfigService
{

    /**
     * Query Electronic signature platform configuration
     *
     * @param id Electronic signature platform configuration primary key
     * @return Electronic signature platform configuration
     */
    @Override
    public HrSignConfig selectHrSignConfigById(Long id)
    {
        return baseMapper.selectHrSignConfigById(id);
    }

    /**
     * Query Electronic signature platform configuration list
     *
     * @param hrSignConfig Electronic signature platform configuration
     * @return Electronic signature platform configuration
     */
    @Override
    public List<HrSignConfig> selectHrSignConfigList(HrSignConfig hrSignConfig)
    {
        return baseMapper.selectHrSignConfigList(hrSignConfig);
    }

    /**
     * Add Electronic signature platform configuration
     *
     * @param hrSignConfig Electronic signature platform configuration
     * @return Result
     */
    @Override
    public int insertHrSignConfig(HrSignConfig hrSignConfig)
    {
        hrSignConfig.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrSignConfig);
    }

    /**
     * Update Electronic signature platform configuration
     *
     * @param hrSignConfig Electronic signature platform configuration
     * @return Result
     */
    @Override
    public int updateHrSignConfig(HrSignConfig hrSignConfig)
    {
        hrSignConfig.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrSignConfig);
    }

    /**
     * Batch delete Electronic signature platform configuration
     *
     * @param ids Electronic signature platform configuration primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrSignConfigByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Electronic signature platform configuration information
     *
     * @param id Electronic signature platform configuration primary key
     * @return Result
     */
    @Override
    public int deleteHrSignConfigById(String id)
    {
        return baseMapper.deleteById(id);
    }

    /**
     * According to  TypeQUERY
     * @param signType
     * @return
     */
    @Override
    public HrSignConfig selectBySignType(Integer signType) {
        return baseMapper.selectBySignType(signType);
    }

    /**
     * MODIFYStatus
     */
    @Override
    public void updateByStatus() {
        baseMapper.updateByStatus();
    }

    @Override
    public HrSignConfig selectConfigInfo(Integer signaturePlatformId) {
        return baseMapper.selectConfigInfo(signaturePlatformId);
    }
}
