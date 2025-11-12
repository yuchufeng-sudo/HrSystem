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
 *  SEAL INFORMATION  Service Implementation
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
     * Query  SEAL INFORMATION 
     *
     * @param id  SEAL INFORMATION  primary key
     * @return  SEAL INFORMATION 
     */
    @Override
    public HrSealInfo selectHrSealInfoById(Long id)
    {
        return baseMapper.selectHrSealInfoById(id);
    }

    /**
     * Query  SEAL INFORMATION  list
     *
     * @param hrSealInfo  SEAL INFORMATION 
     * @return  SEAL INFORMATION 
     */
    @Override
    public List<HrSealInfo> selectHrSealInfoList(HrSealInfo hrSealInfo)
    {
        return baseMapper.selectHrSealInfoList(hrSealInfo);
    }

    /**
     * Add  SEAL INFORMATION 
     *
     * @param hrSealInfo  SEAL INFORMATION 
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
     * Update  SEAL INFORMATION 
     *
     * @param hrSealInfo  SEAL INFORMATION 
     * @return Result
     */
    @Override
    public int updateHrSealInfo(HrSealInfo hrSealInfo)
    {
        hrSealInfo.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrSealInfo);
    }

    /**
     * Batch delete  SEAL INFORMATION 
     *
     * @param ids  SEAL INFORMATION  primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrSealInfoByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete  SEAL INFORMATION  information
     *
     * @param id  SEAL INFORMATION  primary key
     * @return Result
     */
    @Override
    public int deleteHrSealInfoById(String id)
    {
        return baseMapper.deleteById(id);
    }
}