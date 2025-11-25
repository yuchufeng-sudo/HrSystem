package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.hr.domain.HrAttendance;
import com.ys.hr.domain.HrEmpPayrollDetail;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.vo.AttendanceRateVo;
import com.ys.utils.vo.HrAttendanceVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Attendance RecordService Interface
 *
 * @author ys
 * @date 2025-05-26
 */
public interface IHrAttendanceService extends IService<HrAttendance>
{

    /**
     * QUERYAttendance Record  list
     *
     * @param hrAttendance Attendance Record
     * @return Attendance RecordSet
     */
    public List<HrAttendance> selectHrAttendanceList(HrAttendance hrAttendance);

    String importData(List<HrAttendanceVo> HrAttendanceList,String eid);

    Map getCountTime(String userEnterpriseId);

    int updateByIdNew(HrAttendance hrAttendance);

    Map getCountByUserId(Long userId);

    List<HrEmployees> selectHrAttendanceListByHr(HrAttendance hrAttendance);

    TableDataInfo selectHrAttendanceListByUser(HrAttendance hrAttendance);

    List<Map<String, Object>> getCountByWeekly(HrAttendance hrAttendance);

    Map<String, Object> getCountByWeeklyRate(HrAttendance hrAttendance);

    ArrayList<HrEmpPayrollDetail> getPayrollsByEmp(HrAttendance hrAttendance);

    ArrayList<HrEmpPayrollDetail> getPayrollsByHr(HrAttendance hrAttendance);

    List<Map> getPayrollsCount(HrAttendance hrAttendance);

    /**
     * Query User Weekly Attendance
     * @param userId
     * @return
     */
    List<HrAttendance> getUserWeekAttendance(Long userId);

    /**
     * Total Present
     * @param userEnterpriseId
     * @return
     */
    Map<String, Object> selectPresentTotal(String userEnterpriseId);

    /**
     * Total Absent
     * @param userEnterpriseId
     * @return
     */
    Map<String, Object> selectAbsentTotal(String userEnterpriseId);

    /**
     * Attendance rate
     * @param userEnterpriseId
     * @return
     */
    List<AttendanceRateVo> selectAttendanceRate(String userEnterpriseId, Integer year);

    /**
     * Statistics of employees' working hours for the month
     * @param hrAttendance
     * @return
     */
    List<HrEmpPayrollDetail> selectPayrollsByHr(HrAttendance hrAttendance,List<HrEmployees> hrEmployeesList);
}
