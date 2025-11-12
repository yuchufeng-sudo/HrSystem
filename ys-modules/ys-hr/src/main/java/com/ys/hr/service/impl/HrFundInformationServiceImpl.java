package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrFundInformationMapper;
import com.ys.hr.domain.HrFundInformation;
import com.ys.hr.service.IHrFundInformationService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Comprehensive fund information Service Implementation
 *
 * @author ys
 * @date 2025-10-17
 */
@Service
public class HrFundInformationServiceImpl extends ServiceImpl<HrFundInformationMapper, HrFundInformation> implements IHrFundInformationService
{

    /**
     * Query Comprehensive fund information
     *
     * @param id Comprehensive fund information primary key
     * @return Comprehensive fund information
     */
    @Override
    public HrFundInformation selectHrFundInformationById(String id)
    {
        return baseMapper.selectHrFundInformationById(id);
    }

    /**
     * Query Comprehensive fund information list
     *
     * @param hrFundInformation Comprehensive fund information
     * @return Comprehensive fund information
     */
    @Override
    public List<HrFundInformation> selectHrFundInformationList(HrFundInformation hrFundInformation)
    {
        return baseMapper.selectHrFundInformationList(hrFundInformation);
    }

    /**
     * Add Comprehensive fund information
     *
     * @param hrFundInformation Comprehensive fund information
     * @return Result
     */
    @Override
    public int insertHrFundInformation(HrFundInformation hrFundInformation)
    {
        hrFundInformation.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrFundInformation);
    }

    /**
     * Update Comprehensive fund information
     *
     * @param hrFundInformation Comprehensive fund information
     * @return Result
     */
    @Override
    public int updateHrFundInformation(HrFundInformation hrFundInformation)
    {
        hrFundInformation.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrFundInformation);
    }

    /**
     * Batch delete Comprehensive fund information
     *
     * @param ids Comprehensive fund information primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrFundInformationByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Comprehensive fund information information
     *
     * @param id Comprehensive fund information primary key
     * @return Result
     */
    @Override
    public int deleteHrFundInformationById(String id)
    {
        return baseMapper.deleteById(id);
    }
}
