package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrAttendanceDeviceInfo;

/**
 * Attendance Device Information  Mapper Interface
 *
 * @author ys
 * @date 2025-06-23
 */
public interface HrAttendanceDeviceInfoMapper extends BaseMapper<HrAttendanceDeviceInfo>
{
    /**
     * Query Attendance Device Information 
     *
     * @param id Attendance Device Information  primary key
     * @return Attendance Device Information 
     */
    public HrAttendanceDeviceInfo selectHrAttendanceDeviceInfoById(Long id);
    
    /**
     * Query Attendance Device Information  list
     *
     * @param hrAttendanceDeviceInfo Attendance Device Information 
     * @return Attendance Device Information  collection
     */
    public List<HrAttendanceDeviceInfo> selectHrAttendanceDeviceInfoList(HrAttendanceDeviceInfo hrAttendanceDeviceInfo);

    List<String> selectAllSn();
}