package com.ys.hr.controller;

import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrHoliday;
import com.ys.hr.domain.HrLeave;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.hr.service.IHrLeaveService;
import com.ys.hr.service.impl.HrHolidayServiceImpl;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *  LEAVE APPLICATION Controller
 *
 * @author ys
 * @date 2025-05-21
 */
@RestController
@RequestMapping("/leave")
public class HrLeaveController extends BaseController
{
    @Resource
    private IHrLeaveService hrLeaveService;

//    @Resource
//    private ISyncLeaveService syncLeaveService;

    @Resource
    private HrEmployeesMapper hrEmployeesMapper;

    @Resource
    private RemoteMessageService remoteMessageService;

    @Resource
    private HrHolidayServiceImpl hrHolidayService;

    /**
     * QUERY LEAVE APPLICATION   LIST
     */
    @RequiresPermissions("hr:leave:list")
    @GetMapping("/list")
    public TableDataInfo list(HrLeave hrLeave)
    {
        // EMPLOYEE
        if(ObjectUtils.isNotEmpty(hrLeave.getFlag())){
            if(ObjectUtils.isEmpty(hrLeave.getUserId())){
                hrLeave.setUserId(SecurityUtils.getUserId());
            }
        }else{
        //hr USER
            hrLeave.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        }
        startPage();
        List<HrLeave> list = hrLeaveService.selectHrLeaveList(hrLeave);
        return getDataTable(list);
    }

    /**
     * EXPORT LEAVE APPLICATION   LIST
     */
    @RequiresPermissions("hr:leave:export")
    @Log(title = " LEAVE APPLICATION ", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrLeave hrLeave)
    {
        List<HrLeave> list = hrLeaveService.selectHrLeaveList(hrLeave);
        ExcelUtil<HrLeave> util = new ExcelUtil<HrLeave>(HrLeave.class);
        util.exportExcel(response, list, "Leave Application Data");
    }

    /**
     * OBTAIN  LEAVE APPLICATION DETAILEDLY INFORMATION
     */
    @GetMapping(value = "/{leaveId}")
    public AjaxResult getInfo(@PathVariable("leaveId") Long leaveId) {
        HrLeave hrLeave1 = hrLeaveService.getById(leaveId);
        HrLeave hrLeave = hrLeaveService.selectHrLeaveLastTime(hrLeave1.getUserId(),hrLeave1.getStateTime(),hrLeave1.getLeaveType());
        if(ObjectUtils.isNotEmpty(hrLeave)){
            hrLeave1.setLastStateTime(hrLeave.getStateTime());
            hrLeave1.setLastEndTime(hrLeave.getEndTime());
            hrLeave1.setRemainingDays(hrLeave.getRemainingDays());
        }else{
            hrLeave1.setRemainingDays(hrLeave.getRemainingDays());
        }
        return success(hrLeave1);
    }

    /**
     * ADD LEAVE APPLICATION
     */
    @RequiresPermissions("hr:leave:add")
    @Log(title = " LEAVE APPLICATION ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HrLeave hrLeave) {
        Long leader =  hrLeaveService.selectLeader(SecurityUtils.getUserId());
        hrLeave.setManagerId(leader);
        hrLeave.setUserId(SecurityUtils.getUserId());
        hrLeave.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        hrLeave.setLeaveStatus("3");
        hrLeave.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        hrLeave.setCreateTime(DateUtils.getNowDate());
        HrHoliday hrHoliday = new HrHoliday();
        hrHoliday.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        hrHoliday.setHolidayType(hrLeave.getLeaveType());
        List<HrHoliday> hrHolidays = hrHolidayService.selectHrHolidayList(hrHoliday);
        if(ObjectUtils.isNotEmpty(hrHolidays)){
            hrLeave.setPaidLeave(hrHolidays.get(0).getPaidLeave());
        }else{
            hrLeave.setPaidLeave("2");
        }
//        LeaveResponse leaveResponse = syncLeaveService.createLeave("940257", "test", hrLeave.getStateTime(), hrLeave.getEndTime(), hrLeave.getLeaveReason());
//        hrLeave.setSyncId(leaveResponse.getId());
        boolean save = hrLeaveService.save(hrLeave);
        if(save){
            HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesById(leader);
            HrEmployees leavePerson = hrEmployeesMapper.selectHrEmployeesByUserId(SecurityUtils.getUserId());
            AjaxResult info = remoteMessageService.getInfo(SecurityUtils.getUserId(),SecurityConstants.INNER);
            Map<String,String> setting = (Map<String, String>) info.get("data");
            String leaveRequestNotification = setting.get("leaveRequestNotification");
            if(ObjectUtils.isNotEmpty(hrEmployees) && "1".equals(leaveRequestNotification)){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                SysMessage sysMessage = new SysMessage();
                sysMessage.setMessageRecipient(hrEmployees.getUserId());
                sysMessage.setMessageStatus("0");
                sysMessage.setMessageType(4);
                sysMessage.setCreateTime(DateUtils.getNowDate());
                Map<String, Object> map1 = new HashMap<>();
                Map<String, Object> map2 = new HashMap<>();
                Date stateTime = hrLeave.getStateTime();
                Date endTime = hrLeave.getEndTime();
                LocalDate stateTime2 = stateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endTime2 = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String state = stateTime2.format(formatter);
                String end = endTime2.format(formatter);
                Map<Integer, String> leaveTypeMap = new HashMap<>();
                leaveTypeMap.put(1, "Annual Leave");
                leaveTypeMap.put(2, "Sick Leave");
                leaveTypeMap.put(3, "Maternity Leave");
                leaveTypeMap.put(6, "Personal Leave");
                leaveTypeMap.put(5, "Paid Leave");
                map1.put("name",leavePerson.getFullName());
                map1.put("laeveType",leaveTypeMap.get(Integer.valueOf(hrLeave.getLeaveType())));
                map1.put("leaveTime",state + "~" + end);
                sysMessage.setMap1(map1);
                sysMessage.setMap2(map2);
                remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
            }
        }
        return toAjax(save);
    }

    /**
     * MODIFY LEAVE APPLICATION
     */
    @RequiresPermissions("hr:leave:edit")
    @Log(title = " LEAVE APPLICATION ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrLeave hrLeave) {
        Long userId = SecurityUtils.getUserId();
        hrLeave.setUpdateBy(String.valueOf(userId));
        hrLeave.setUpdateTime(DateUtils.getNowDate());
        int i = hrLeaveService.updateByLeaveId(hrLeave);
        //1 同意 4 申请 name rejectReason
        if(i>0){
            HrLeave byId = hrLeaveService.getById(hrLeave.getLeaveId());
            if(ObjectUtils.isNotEmpty(byId)){
                HrEmployees leavePerson = hrEmployeesMapper.selectHrEmployeesByUserId(byId.getUserId());
                if("1".equals(hrLeave.getLeaveStatus())){
                    SysMessage sysMessage = new SysMessage();
                    sysMessage.setMessageRecipient(byId.getUserId());
                    sysMessage.setMessageStatus("0");
                    sysMessage.setMessageType(5);
                    sysMessage.setCreateTime(DateUtils.getNowDate());
                    Map<String, Object> map1 = new HashMap<>();
                    Map<String, Object> map2 = new HashMap<>();
                    map1.put("name",leavePerson.getFullName());
                    if(ObjectUtils.isNotEmpty(byId.getRejectReason())){
                        map1.put("rejectReason",byId.getRejectReason());
                    }else{
                        map1.put("rejectReason","no reason");
                    }
                    sysMessage.setMap1(map1);
                    sysMessage.setMap2(map2);
                    remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
//                    syncLeaveService.updateLeave(byId.getSyncId(),null,null,null,null,null,DateUtils.getNowDate(),leavePerson.getFullName(),null,null,null );
                }else if("4".equals(hrLeave.getLeaveStatus())){
                    SysMessage sysMessage = new SysMessage();
                    sysMessage.setMessageRecipient(byId.getUserId());
                    sysMessage.setMessageStatus("0");
                    sysMessage.setMessageType(6);
                    sysMessage.setCreateTime(DateUtils.getNowDate());
                    Map<String, Object> map1 = new HashMap<>();
                    Map<String, Object> map2 = new HashMap<>();
                    map1.put("name",leavePerson.getFullName());
                    if(ObjectUtils.isNotEmpty(byId.getRejectReason())){
                        map1.put("rejectReason",byId.getRejectReason());
                    }else{
                        map1.put("rejectReason","no reason");
                    }
                    sysMessage.setMap1(map1);
                    sysMessage.setMap2(map2);
                    remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
//                    syncLeaveService.updateLeave(byId.getSyncId(),null,null,null,null,null,null,null,DateUtils.getNowDate(),byId.getRejectReason(),null );
                }
            }
        }
        return toAjax( i );
    }

    /**
     * DELETE LEAVE APPLICATION
     */
    @RequiresPermissions("hr:leave:remove")
    @Log(title = " LEAVE APPLICATION ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{leaveIds}")
    public AjaxResult remove(@PathVariable Long[] leaveIds) {
        return toAjax(hrLeaveService.removeByIds(Arrays.asList(leaveIds)));
    }

    // Leave Application data statistics
    @GetMapping("/leaveCount")
    public AjaxResult candidateCount()
    {
        HrLeave hrLeave = new HrLeave();
        hrLeave.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return AjaxResult.success(hrLeaveService.leaveCount(hrLeave));
    }

    //USER  Leave Application data statistics
    @GetMapping("/leaveCountByUser")
    public AjaxResult leaveCountByUser()
    {
        HrLeave hrLeave = new HrLeave();
        hrLeave.setUserId(SecurityUtils.getUserId());
        return AjaxResult.success(hrLeaveService.leaveCountByUser(hrLeave));
    }
}
