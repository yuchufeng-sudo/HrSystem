package com.ys.sync.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.sync.domain.SyncShiftcareUser;

/**
 * sync shiftcare user Service Interface
 *
 * @author ys
 * @date 2025-10-30
 */
public interface ISyncShiftcareUserService extends IService<SyncShiftcareUser>
{
    /**
     * Query sync shiftcare user
     *
     * @param id sync shiftcare user primary key
     * @return sync shiftcare user
     */
    public SyncShiftcareUser selectSyncShiftcareUserByUserId(Long id);

    /**
     * Add sync shiftcare user
     *
     * @param syncShiftcareUser sync shiftcare user
     * @return Result
     */
    public int insertSyncShiftcareUser(SyncShiftcareUser syncShiftcareUser);

    /**
     * Update sync shiftcare user
     *
     * @param syncShiftcareUser sync shiftcare user
     * @return Result
     */
    public int updateSyncShiftcareUser(SyncShiftcareUser syncShiftcareUser);

    /**
     * Delete sync shiftcare user information
     *
     * @param id sync shiftcare user primary key
     * @return Result
     */
    public int deleteSyncShiftcareUserByUserId(Long userId);
}
