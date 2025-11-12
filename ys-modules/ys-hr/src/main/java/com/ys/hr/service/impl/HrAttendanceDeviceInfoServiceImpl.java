package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.DateUtils;
import com.ys.hr.domain.HrAttendanceDeviceInfo;
import com.ys.hr.mapper.HrAttendanceDeviceInfoMapper;
import com.ys.hr.service.IHrAttendanceDeviceInfoService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Attendance Device Information  Service Implementation
 *
 * @author ys
 * @date 2025-06-23
 */
@Service
public class HrAttendanceDeviceInfoServiceImpl extends ServiceImpl<HrAttendanceDeviceInfoMapper, HrAttendanceDeviceInfo> implements IHrAttendanceDeviceInfoService
{

    /**
     * Query Attendance Device Information
     *
     * @param id Attendance Device Information  primary key
     * @return Attendance Device Information
     */
    @Override
    public HrAttendanceDeviceInfo selectHrAttendanceDeviceInfoById(Long id)
    {
        return baseMapper.selectHrAttendanceDeviceInfoById(id);
    }

    /**
     * Query Attendance Device Information  list
     *
     * @param hrAttendanceDeviceInfo Attendance Device Information
     * @return Attendance Device Information
     */
    @Override
    public List<HrAttendanceDeviceInfo> selectHrAttendanceDeviceInfoList(HrAttendanceDeviceInfo hrAttendanceDeviceInfo)
    {
        return baseMapper.selectHrAttendanceDeviceInfoList(hrAttendanceDeviceInfo);
    }

    /**
     * Add Attendance Device Information
     *
     * @param hrAttendanceDeviceInfo Attendance Device Information
     * @return Result
     */
    @Override
    public int insertHrAttendanceDeviceInfo(HrAttendanceDeviceInfo hrAttendanceDeviceInfo)
    {
        hrAttendanceDeviceInfo.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrAttendanceDeviceInfo);
    }

    /**
     * Update Attendance Device Information
     *
     * @param hrAttendanceDeviceInfo Attendance Device Information
     * @return Result
     */
    @Override
    public int updateHrAttendanceDeviceInfo(HrAttendanceDeviceInfo hrAttendanceDeviceInfo)
    {
        hrAttendanceDeviceInfo.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrAttendanceDeviceInfo);
    }

    /**
     * Batch delete Attendance Device Information
     *
     * @param ids Attendance Device Information  primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrAttendanceDeviceInfoByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Attendance Device Information  information
     *
     * @param id Attendance Device Information  primary key
     * @return Result
     */
    @Override
    public int deleteHrAttendanceDeviceInfoById(String id)
    {
        return baseMapper.deleteById(id);
    }

    @Override
    public List<String> selectAllSn() {
        return baseMapper.selectAllSn();
    }
}
