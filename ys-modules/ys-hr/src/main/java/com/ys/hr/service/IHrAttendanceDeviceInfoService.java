package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrAttendanceDeviceInfo;

import java.util.List;

/**
 * Attendance Device Information  Service Interface
 *
 * @author ys
 * @date 2025-06-23
 */
public interface IHrAttendanceDeviceInfoService extends IService<HrAttendanceDeviceInfo>
{
    /**
     * Query Attendance Device Information
     *
     * @param id Attendance Device Information  primary key
     * @return Attendance Device Information
     */
    public HrAttendanceDeviceInfo selectHrAttendanceDeviceInfoById(Long id);

    /**
     * Query Attendance Device Information list
     *
     * @param hrAttendanceDeviceInfo Attendance Device Information
     * @return Attendance Device Information  collection
     */
    public List<HrAttendanceDeviceInfo> selectHrAttendanceDeviceInfoList(HrAttendanceDeviceInfo hrAttendanceDeviceInfo);

    /**
     * Add Attendance Device Information
     *
     * @param hrAttendanceDeviceInfo Attendance Device Information
     * @return Result
     */
    public int insertHrAttendanceDeviceInfo(HrAttendanceDeviceInfo hrAttendanceDeviceInfo);

    /**
     * Update Attendance Device Information
     *
     * @param hrAttendanceDeviceInfo Attendance Device Information
     * @return Result
     */
    public int updateHrAttendanceDeviceInfo(HrAttendanceDeviceInfo hrAttendanceDeviceInfo);

    /**
     * Batch delete Attendance Device Information
     *
     * @param ids Attendance Device Information  primary keys to be deleted
     * @return Result
     */
    public int deleteHrAttendanceDeviceInfoByIds(String[] ids);

    /**
     * Delete Attendance Device Information  information
     *
     * @param id Attendance Device Information  primary key
     * @return Result
     */
    public int deleteHrAttendanceDeviceInfoById(String id);

    /**
     *
     * @return
     */
    List<String> selectAllSn();
}
