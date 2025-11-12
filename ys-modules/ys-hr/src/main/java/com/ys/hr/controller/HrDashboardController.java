package com.ys.hr.controller;

import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrAttendance;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.vo.AttendanceRateVo;
import com.ys.hr.domain.vo.BirthdayReportVo;
import com.ys.hr.domain.vo.DailyTimeVo;
import com.ys.hr.domain.vo.DepartmentsVo;
import com.ys.hr.service.IHrAttendanceService;
import com.ys.hr.service.IHrDeptService;
import com.ys.hr.service.IHrEmployeesService;
import com.ys.hr.service.IHrLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * hr dashboard
 */
@RestController
@RequestMapping("/hrDashboard")
public class HrDashboardController extends BaseController {

    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    private IHrEmployeesService hrEmployeesService;

    @Autowired
    private IHrAttendanceService hrAttendanceService;

    @Autowired
    private IHrLeaveService hrLeaveService;

    @Autowired
    private IHrDeptService hrDeptService;

    /**
     * Total Employee
     * @return
     */
    @GetMapping("/getEmployeeTotal")
    public AjaxResult getEmployeeTotal(){
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
//        Map<String, Object> entries = redisTemplate.opsForHash().entries("employee-total:" + userEnterpriseId);
//        if(!ObjectUtils.isEmpty(entries)){
//            return AjaxResult.success(entries);
//        }
        Map<String, Object> map = hrEmployeesService.selectEmployeeCount(userEnterpriseId);
//        redisTemplate.opsForHash().putAll("employee-total:" + userEnterpriseId, map);
//        //Cache for two hours.
//        redisTemplate.expire("employee-total:" + userEnterpriseId, 2, TimeUnit.HOURS);
        return AjaxResult.success(map);
    }

    /**
     * Total Present
     * @return
     */
    @GetMapping("/getPresentTotal")
    public AjaxResult getPresentTotal(){
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
//        Map<String, Object> entries = redisTemplate.opsForHash().entries("present-total:" + userEnterpriseId);
//        if(!ObjectUtils.isEmpty(entries)){
//            return AjaxResult.success(entries);
//        }
        Map<String, Object> map = hrAttendanceService.selectPresentTotal(userEnterpriseId);
//        redisTemplate.opsForHash().putAll("present-total:" + userEnterpriseId, map);
//        //Cache for two hours.
//        redisTemplate.expire("present-total:" + userEnterpriseId, 2, TimeUnit.HOURS);
        return AjaxResult.success(map);
    }

    /**
     * Total Absent
     * @return
     */
    @GetMapping("/getAbsentTotal")
    public AjaxResult getAbsentTotal(){
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
//        Map<String, Object> entries = redisTemplate.opsForHash().entries("absent-total:" + userEnterpriseId);
//        if(!ObjectUtils.isEmpty(entries)){
//            return AjaxResult.success(entries);
//        }
        Map<String, Object> map = hrAttendanceService.selectAbsentTotal(userEnterpriseId);
//        redisTemplate.opsForHash().putAll("absent-total:" + userEnterpriseId, map);
//        //Cache for two hours.
//        redisTemplate.expire("absent-total:" + userEnterpriseId, 2, TimeUnit.HOURS);
        return AjaxResult.success(map);
    }


    /**
     * On Leave
     * @return
     */
    @GetMapping("/getLeaveTotal")
    public AjaxResult getLeaveTotal(){
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
//        Map<String, Object> entries = redisTemplate.opsForHash().entries("leave-total:" + userEnterpriseId);
//        if(!ObjectUtils.isEmpty(entries)){
//            return AjaxResult.success(entries);
//        }
        Map<String, Object> map = hrLeaveService.selectLeaveTotal(userEnterpriseId);
//        redisTemplate.opsForHash().putAll("leave-total:" + userEnterpriseId, map);
//        //Cache for two hours.
//        redisTemplate.expire("leave-total:" + userEnterpriseId, 2, TimeUnit.HOURS);
        return AjaxResult.success(map);
    }

    /**
     * Attendance rate
     * @return
     */
    @GetMapping("/getAttendanceRate")
    public AjaxResult getAttendanceRate(HrAttendance hrAttendance){
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
//        List<AttendanceRateVo> cachedList = (List<AttendanceRateVo>) redisTemplate.opsForValue().get("attendance-rate:" + userEnterpriseId + ":" + hrAttendance.getYear());
//        if(!ObjectUtils.isEmpty(cachedList)){
//            return AjaxResult.success(cachedList);
//        }
        List<AttendanceRateVo> list = hrAttendanceService.selectAttendanceRate(userEnterpriseId, hrAttendance.getYear());
//        redisTemplate.opsForValue().set("attendance-rate:" + userEnterpriseId + ":" + hrAttendance.getYear(), list);
//        //Cache for two hours.
//        redisTemplate.expire("attendance-rate:" + userEnterpriseId + ":" + hrAttendance.getYear(), 2, TimeUnit.HOURS);
        return AjaxResult.success(list);
    }

    /**
     * Employeement status
     * @return
     */
    @GetMapping("/getEmployeeStatus")
    public AjaxResult getEmployeeStatus() {
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
//        Map<String, Object> entries = redisTemplate.opsForHash().entries("employee-status:" + userEnterpriseId);
//        if(!ObjectUtils.isEmpty(entries)){
//            return AjaxResult.success(entries);
//        }
        Map<String, Object> map =  hrEmployeesService.getEmployeeStatusCount(userEnterpriseId);
//        redisTemplate.opsForHash().putAll("employee-status:" + userEnterpriseId, map);
//        //Cache for two hours.
//        redisTemplate.expire("employee-status:" + userEnterpriseId, 2, TimeUnit.HOURS);
        return AjaxResult.success(map);
    }

    /**
     * Departments
     * @return
     */
    @GetMapping("/getDepartments")
    public AjaxResult getDepartments(){
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
        List<DepartmentsVo> list = hrDeptService.selectDepartments(userEnterpriseId);
        return AjaxResult.success(list);
    }

    /**
     * Daily Time Limits
     * @return
     */
    @GetMapping("/getThisDaySchedulingList")
    public AjaxResult getThisDaySchedulingList(){
        LocalDateTime now = LocalDateTime.now();
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        List<DailyTimeVo> cachedList = (List<DailyTimeVo>) redisTemplate.opsForValue().get("this-day-scheduling:" + userEnterpriseId + ":" + formattedDate);
//        if(!ObjectUtils.isEmpty(cachedList)){
//            return AjaxResult.success(cachedList);
//        }
        List<DailyTimeVo> list = hrEmployeesService.selectThisDaySchedulingList(userEnterpriseId, formattedDate);
//        redisTemplate.opsForValue().set("this-day-scheduling:" + userEnterpriseId + ":" + formattedDate, list);
//        //Cache for two hours.
//        redisTemplate.expire("this-day-scheduling:" + userEnterpriseId + ":" + formattedDate, 2, TimeUnit.HOURS);
        return AjaxResult.success(list);
    }

    /**
     * Upcoming Birthday
     * @param hrEmployees
     * @return
     */
    @GetMapping("/getUpcomingBirthday")
    public TableDataInfo getUpcomingBirthday(HrEmployees hrEmployees){
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<BirthdayReportVo> list = hrEmployeesService.selectEmployeeBirthday(hrEmployees);
        return getDataTable(list);
    }
}
