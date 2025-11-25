package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrAttendance;
import com.ys.hr.domain.vo.AttendanceRateVo;
import com.ys.hr.domain.vo.EmployeePresenCountVo;
import com.ys.system.api.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Attendance RecordMapper Interface
 *
 * @author ys
 * @date 2025-05-26
 */
public interface HrAttendanceMapper extends BaseMapper<HrAttendance> {
    /**
     * QUERYAttendance Record list
     *
     * @param hrAttendance Attendance Record
     * @return Attendance RecordSet
     */
    public List<HrAttendance> selectHrAttendanceList(HrAttendance hrAttendance);

    SysUser selectByUserName(SysUser sysUser);

    Map<String, Object> getCountTime(HrAttendance hrAttendance);

    int insertHrAttendance(HrAttendance hrAttendance);

    int updateByIdNew(HrAttendance hrAttendance);

    Map<String, Object> getCountByUserId(HrAttendance hrAttendance);

    ArrayList<HrAttendance> selectHrAttendanceListByHr(@Param("enterpriseId") String enterpriseId,
            @Param("startDate") String startDate, @Param("endDate") String endDate);

    ArrayList<HrAttendance> selectHrAttendanceListByUser(HrAttendance hrAttendance);

    HrAttendance selectHrAttendance(HrAttendance attendance);

    List<Map> getPayrollsCount(HrAttendance hrAttendance);

    /**
     * Query Employee Weekly Attendance
     *
     * @param userId
     * @return
     */
    List<HrAttendance> selectUserWeekAttendance(Long userId);

    /**
     * Query Attendance Days
     *
     * @param userEnterpriseId
     * @return
     */
    EmployeePresenCountVo selectPresentTotal(String userEnterpriseId);

    List<HrAttendance> selectLastMonthAttendances(Long userId);

    List<HrAttendance> selectThisMonthAttendances(Long userId);

    /**
     * Statistics of Annual Attendance
     *
     * @param userEnterpriseId
     * @param year
     * @return
     */
    List<AttendanceRateVo> selectAttendanceRate(@Param("userEnterpriseId") String userEnterpriseId,
            @Param("year") Integer year);

    /**
     * Query Yesterday's Clock-in Record
     *
     * @param userId
     * @return
     */
    Integer selectAttendanceByLastDay(Long userId);

    /**
     * Query Today's Clock-in Record
     *
     * @param userId
     * @return
     */
    Integer selectAttendanceByThisDay(Long userId);

    HrAttendance selectAttendanceByUserIdAndDate(@Param("userId") Long userId,
            @Param("date") String formatted);

    List<HrAttendance> selectPresentHours(@Param("userId") Long userId,
                                          @Param("searchData")Date searchData);

    String selectByPresentStatus(@Param("presentStatus") String presentStatus);
}
