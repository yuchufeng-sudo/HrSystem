package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.DateUtils;
import com.ys.hr.domain.HrEnterpriseRealNameInfo;
import com.ys.hr.mapper.HrEnterpriseRealNameInfoMapper;
import com.ys.hr.service.IHrEnterpriseRealNameInfoService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 *  Enterprise Real-name authentication Information  Service Implementation
 *
 * @author ys
 * @date 2025-06-05
 */
@Service
public class HrEnterpriseRealNameInfoServiceImpl extends ServiceImpl<HrEnterpriseRealNameInfoMapper, HrEnterpriseRealNameInfo> implements IHrEnterpriseRealNameInfoService
{

    /**
     * Query Enterprise Real-name authentication Information
     *
     * @param id  Enterprise Real-name authentication Information  primary key
     * @return  Enterprise Real-name authentication Information
     */
    @Override
    public HrEnterpriseRealNameInfo selectHrEnterpriseRealNameInfoById(Long id)
    {
        return baseMapper.selectHrEnterpriseRealNameInfoById(id);
    }

    /**
     * Query Enterprise Real-name authentication Information list
     *
     * @param hrEnterpriseRealNameInfo  Enterprise Real-name authentication Information
     * @return  Enterprise Real-name authentication Information
     */
    @Override
    public List<HrEnterpriseRealNameInfo> selectHrEnterpriseRealNameInfoList(HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo)
    {
        return baseMapper.selectHrEnterpriseRealNameInfoList(hrEnterpriseRealNameInfo);
    }

    /**
     * Add Enterprise Real-name authentication Information
     *
     * @param hrEnterpriseRealNameInfo  Enterprise Real-name authentication Information
     * @return Result
     */
    @Override
    public int insertHrEnterpriseRealNameInfo(HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo)
    {
        hrEnterpriseRealNameInfo.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrEnterpriseRealNameInfo);
    }

    /**
     * Update Enterprise Real-name authentication Information
     *
     * @param hrEnterpriseRealNameInfo  Enterprise Real-name authentication Information
     * @return Result
     */
    @Override
    public int updateHrEnterpriseRealNameInfo(HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo)
    {
        hrEnterpriseRealNameInfo.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrEnterpriseRealNameInfo);
    }

    /**
     * Batch delete  Enterprise Real-name authentication Information
     *
     * @param ids  Enterprise Real-name authentication Information  primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrEnterpriseRealNameInfoByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Enterprise Real-name authentication Information  information
     *
     * @param id  Enterprise Real-name authentication Information  primary key
     * @return Result
     */
    @Override
    public int deleteHrEnterpriseRealNameInfoById(String id)
    {
        return baseMapper.deleteById(id);
    }

    /**
     * According to User idQUERY Information
     * @param userEnterpriseId
     * @return
     */
    @Override
    public HrEnterpriseRealNameInfo selectHrEnterpriseRealNameInfoByUserId(String userEnterpriseId) {
        return baseMapper.selectHrEnterpriseRealNameInfoByUserId(userEnterpriseId);
    }

    /**
     * According to  Serial NumberQUERY Information
     * @param serialNo
     * @return
     */
    @Override
    public HrEnterpriseRealNameInfo selectBySerialNo(String serialNo) {
        return baseMapper.selectBySerialNo(serialNo);
    }
}
