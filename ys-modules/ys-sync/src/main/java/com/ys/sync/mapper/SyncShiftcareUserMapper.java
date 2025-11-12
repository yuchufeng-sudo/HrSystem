package com.ys.sync.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.sync.domain.SyncShiftcareUser;

/**
 * sync shiftcare user Mapper Interface
 *
 * @author ys
 * @date 2025-10-30
 */
public interface SyncShiftcareUserMapper extends BaseMapper<SyncShiftcareUser>
{

    SyncShiftcareUser selectSyncShiftcareUserByUserId(Long id);

    int deleteSyncShiftcareUserByUserId(Long id);
}
