package com.ys.hr.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.uuid.IdUtils;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.RemoteFileService;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrSealInfoMapper;
import com.ys.hr.domain.HrSealInfo;
import com.ys.hr.service.IHrSealInfoService;
import com.ys.common.core.utils.DateUtils;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 *  seal Information  Service Implementation
 *
 * @author ys
 * @date 2025-06-05
 */
@Service
public class HrSealInfoServiceImpl extends ServiceImpl<HrSealInfoMapper, HrSealInfo> implements IHrSealInfoService
{

    @Resource
    private RemoteFileService remoteFileService;

    /**
     * Query seal Information 
     *
     * @param id  seal Information  primary key
     * @return  seal Information 
     */
    @Override
    public HrSealInfo selectHrSealInfoById(Long id)
    {
        return baseMapper.selectHrSealInfoById(id);
    }

    /**
     * Query seal Information list
     *
     * @param hrSealInfo  seal Information 
     * @return  seal Information 
     */
    @Override
    public List<HrSealInfo> selectHrSealInfoList(HrSealInfo hrSealInfo)
    {
        return baseMapper.selectHrSealInfoList(hrSealInfo);
    }

    /**
     * Add seal Information 
     *
     * @param hrSealInfo  seal Information 
     * @return Result
     */
    @Override
    public int insertHrSealInfo(HrSealInfo hrSealInfo)
    {
        hrSealInfo.setCreateTime(DateUtils.getNowDate());
        hrSealInfo.setCreateBy(SecurityUtils.getUserEnterpriseId());
        hrSealInfo.setAccount(SecurityUtils.getUserEnterpriseId());
        hrSealInfo.setSealNo(IdUtils.fastUUID());
        return baseMapper.insert(hrSealInfo);
    }

    /**
     * Update seal Information 
     *
     * @param hrSealInfo  seal Information 
     * @return Result
     */
    @Override
    public int updateHrSealInfo(HrSealInfo hrSealInfo)
    {
        hrSealInfo.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrSealInfo);
    }

    /**
     * Batch delete  seal Information 
     *
     * @param ids  seal Information  primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrSealInfoByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete seal Information  information
     *
     * @param id  seal Information  primary key
     * @return Result
     */
    @Override
    public int deleteHrSealInfoById(String id)
    {
        return baseMapper.deleteById(id);
    }
}
