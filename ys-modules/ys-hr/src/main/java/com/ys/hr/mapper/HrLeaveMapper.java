package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrHoliday;
import com.ys.hr.domain.HrLeave;
import org.apache.ibatis.annotations.Param;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Leave Application Mapper Interface
 *
 * @author ys
 * @date 2025-05-21
 */
public interface HrLeaveMapper extends BaseMapper<HrLeave> {
    /**
     * Query Leave Application list
     *
     * @param hrLeave Leave Application
     * @return Leave Application Set
     */
    public List<HrLeave> selectHrLeaveList(HrLeave hrLeave);

    HrLeave selectHrLeaveLastTime(Long userId);

    int updateByLeaveId(HrLeave hrLeave);

    Map<String, Object> leaveCount(HrLeave hrLeave);

    Long selectLeader(Long userId);

    Map<String, Object> leaveCountByUser(HrLeave hrLeave);

    List<HrHoliday> selectLeaveDaysByEid(String userEnterpriseId);

    /**
     * QUERYAttendance Information
     *
     * @param userId
     * @param attendanceTime
     * @return
     */
    HrLeave selectLeaveByUserId(@Param("userId") Long userId,
            @Param("attendanceTime") Date attendanceTime);

    /**
     * Query the number of leave application days for yesterday
     *
     * @param userId
     * @param startTime
     * @return
     */
    Integer selectLeaveByLastDay(@Param("userId") Long userId,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    /**
     * Query the number of leave application days for today
     *
     * @param userId
     * @param startTime
     * @return
     */
    Integer selectLeaveByThisDay(@Param("userId") Long userId,
            @Param("workTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    /**
     * Check the leave status for one month
     * @param userId
     * @param searchData
     * @return
     */
    List<HrLeave> selectLeaveByUserIdAndDate(@Param("userId") Long userId,
                                             @Param("searchData") Date searchData);
}
