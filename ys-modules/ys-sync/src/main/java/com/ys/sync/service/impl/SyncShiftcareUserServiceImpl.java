package com.ys.sync.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.sync.mapper.SyncShiftcareUserMapper;
import com.ys.sync.domain.SyncShiftcareUser;
import com.ys.sync.service.ISyncShiftcareUserService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * sync shiftcare user Service Implementation
 *
 * @author ys
 * @date 2025-10-30
 */
@Service
public class SyncShiftcareUserServiceImpl extends ServiceImpl<SyncShiftcareUserMapper, SyncShiftcareUser> implements ISyncShiftcareUserService
{

    /**
     * Query sync shiftcare user
     *
     * @param id sync shiftcare user primary key
     * @return sync shiftcare user
     */
    @Override
    public SyncShiftcareUser selectSyncShiftcareUserByUserId(Long id)
    {
        return baseMapper.selectSyncShiftcareUserByUserId(id);
    }

    /**
     * Add sync shiftcare user
     *
     * @param syncShiftcareUser sync shiftcare user
     * @return Result
     */
    @Override
    public int insertSyncShiftcareUser(SyncShiftcareUser syncShiftcareUser)
    {
        syncShiftcareUser.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(syncShiftcareUser);
    }

    /**
     * Update sync shiftcare user
     *
     * @param syncShiftcareUser sync shiftcare user
     * @return Result
     */
    @Override
    public int updateSyncShiftcareUser(SyncShiftcareUser syncShiftcareUser)
    {
        syncShiftcareUser.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(syncShiftcareUser);
    }

    /**
     * Delete sync shiftcare user information
     *
     * @param id sync shiftcare user primary key
     * @return Result
     */
    @Override
    public int deleteSyncShiftcareUserByUserId(Long id)
    {
        return baseMapper.deleteSyncShiftcareUserByUserId(id);
    }
}
