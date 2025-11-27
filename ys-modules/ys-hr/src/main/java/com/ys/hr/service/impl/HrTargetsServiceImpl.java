package com.ys.hr.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.*;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.hr.mapper.HrCandidateInfoMapper;
import com.ys.hr.service.IHrTargetEmployeeService;
import com.ys.hr.service.IHrTargetTasksService;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import com.ys.system.api.model.LoginUser;
import com.ys.utils.email.EmailUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrTargetsMapper;
import com.ys.hr.service.IHrTargetsService;
import com.ys.common.core.utils.DateUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Main table storing all target information Service Implementation
 *
 * @author ys
 * @date 2025-06-18
 */
@Service
@EnableCaching
public class HrTargetsServiceImpl extends ServiceImpl<HrTargetsMapper, HrTargets> implements IHrTargetsService {

    @Resource
    private IHrTargetEmployeeService hrTargetEmployeeService;

    @Resource
    private HrEmployeesMapper hrEmployeesMapper;

    @Resource
    private IHrTargetTasksService hrTargetTasksService;

    @Resource
    private RemoteMessageService remoteMessageService;

    @Resource
    private EmailUtils emailUtils;

    @Resource
    private HrCandidateInfoMapper candidateInfoMapper;

    /**
     * Query Main table storing all target information
     *
     * @param id Main table storing all target information primary key
     * @return Main table storing all target information
     */
    @Override
    public HrTargets selectHrTargetsById(Long id) {
        return baseMapper.selectHrTargetsById(id);
    }

    @Override
    @Transactional
    public HrTargets selectHrTargetsInfo(Long id) {
        checkParticipants(id);
        HrTargets data = selectHrTargetsById(id);
        String enterpriseId = data.getEnterpriseId();
        if (!enterpriseId.equals(SecurityUtils.getUserEnterpriseId())) {
            throw new ServiceException("No authority to access this data");
        }
        QueryWrapper<HrTargetTasks> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("target_id",id);
        long count = hrTargetTasksService.count(queryWrapper);
        QueryWrapper<HrTargetTasks> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("target_id",id);
        queryWrapper1.in("status","Complete","Confirm");
        long count1 = hrTargetTasksService.count(queryWrapper1);
        if (count!=0){
            data.setProgress((int) (((double) count1 / (double) count) * 100));
        }else {
            data.setProgress(0);
        }
        Integer progress = data.getProgress();
        String status = data.getStatus();
        if (progress>0&&progress!=100&&"Not Started".equals(status)){
            data.setStatus("In Progress");
            this.updateHrTargets(data);
        }
        if (progress==100&&!"Complete".equals(status)){
            data.setStatus("Complete");
            completeTargets(id);
        }
        return data;
    }

    @Transactional
    @Override
    public void completeTargets(Long id) {
        checkParticipants(id);
        HrTargets hrTargets = new HrTargets();
        hrTargets.setId(id);
        hrTargets.setStatus("Complete");
        hrTargets.setCompleteTime(DateUtils.getNowDate());
        HrTargets hrTargets1 = selectHrTargetsById(id);
        HrTargetEmployee hrTargetEmployee = new HrTargetEmployee();
        hrTargetEmployee.setTargetId(id);
        List<HrTargetEmployee> hrTargetEmployees = hrTargetEmployeeService.selectHrTargetEmployeeList(hrTargetEmployee);
        if(ObjectUtils.isNotEmpty(hrTargetEmployees)){
            //Use streams to convert employee IDs into an array.
            for (HrTargetEmployee employeeId : hrTargetEmployees) {
                HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesById(employeeId.getEmployeeId());
                HrEmployees leader = hrEmployeesMapper.selectHrEmployeesByUserId(hrTargets1.getHead());
                SysMessage sysMessage = new SysMessage();
                sysMessage.setMessageRecipient(hrEmployees.getUserId());
                sysMessage.setMessageStatus("0");
                sysMessage.setMessageType(17);
                sysMessage.setCreateTime(DateUtils.getNowDate());
                Map<String, Object> map1 = new HashMap<>();
                Map<String, Object> map2 = new HashMap<>();
                map2.put("id", id);
                if(ObjectUtils.isNotEmpty(leader)){
                    map1.put("HrName", leader.getFullName());
                }else{
                    map1.put("HrName", "The leader is not indicated");
                }
                map1.put("title", hrTargets1.getName());
                Date startTime = DateUtils.getNowDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(startTime);
                map1.put("data", formattedDate);
                sysMessage.setMap1(map1);
                sysMessage.setMap2(map2);
                remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
            }
        }
        Long resignEmployeeId = hrTargets1.getResignEmployeeId();
        if (resignEmployeeId!=null){
            QueryWrapper<HrTargets> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("resign_employee_id",resignEmployeeId);
            queryWrapper.orderByDesc("create_time");
            queryWrapper.select("status");
            queryWrapper.last("limit 2");
            List<HrTargets> list = list(queryWrapper);
            int count = 0;
            for (HrTargets targets : list) {
                if (targets.getStatus().equals("Complete")) {
                    count++;
                }
            }
            if (count==1){
                resignComplete(resignEmployeeId,hrTargets1.getHead());
            }
        }
        updateHrTargets(hrTargets);
    }

    public void resignComplete(Long employeeId, Long hrUserId) {
        HrEmployees employees = hrEmployeesMapper.selectHrEmployeesById(employeeId);
        if (employees==null){
            return;
        }
        HrEnterprise hrEnterprise = candidateInfoMapper.seleEid(SecurityUtils.getUserEnterpriseId());
        Map<String, Object> params = new HashMap<>();
        params.put("FullName", employees.getFullName());
        params.put("CompanyName", hrEnterprise.getEnterpriseName());
        emailUtils.sendEmailByTemplate(params, employees.getEmail(), "offboardingComplete");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("fullName",employees.getFullName());

        QueryWrapper<HrTargets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resign_employee_id", employees.getEmployeeId())
                .eq("name", "Complete offboarding - HR");
        List<HrTargets> hrOffboardingTargets = list(queryWrapper);
        Map<String, Object> map2 = new HashMap<>();
        if (!hrOffboardingTargets.isEmpty()) {
            map2.put("id", hrOffboardingTargets.get(0).getId());
        }
        // Build system message
        SysMessage sysMessage = buildSysMessage(
                hrUserId,
                25,
                map1,
                map2
        );
        remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
    }

    /**
     * Generic method to build system message object with common properties
     *
     * @param recipientId User ID of the message recipient
     * @param messageType Type of the system message (see MESSAGE_TYPE_* constants)
     * @param map1        Additional message parameters (basic info)
     * @param map2        Additional message parameters (target task info)
     * @return Constructed SysMessage object
     */
    private SysMessage buildSysMessage(Long recipientId, int messageType, Map<String, Object> map1, Map<String, Object> map2) {
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMessageSender(0L);
        sysMessage.setMessageRecipient(recipientId);
        sysMessage.setMessageStatus("0");
        sysMessage.setMessageType(messageType);
        sysMessage.setCreateTime(DateUtils.getNowDate());
        sysMessage.setMap1(map1);
        sysMessage.setMap2(map2);
        return sysMessage;
    }

    /**
     * Query Main table storing all target information list
     *
     * @param hrTargets Main table storing all target information
     * @return Main table storing all target information
     */
    @Override
    public List<HrTargets> selectHrTargetsList(HrTargets hrTargets) {
        return baseMapper.selectHrTargetsList(hrTargets);
    }

    /**
     * Add Main table storing all target information
     *
     * @param hrTargets Main table storing all target information
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HrTargets insertHrTargets(HrTargets hrTargets) {
        hrTargets.setCreateTime(DateUtils.getNowDate());
        int insert = baseMapper.insert(hrTargets);
        batchSaveTargetEmployee(hrTargets);
        if (insert > 0) {
            Long[] employeeIds = hrTargets.getEmployeeIds();
            if (ObjectUtils.isNotEmpty(employeeIds)) {
                for (Long employeeId : employeeIds) {
                    HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesById(employeeId);
                    SysMessage sysMessage = buildMessage(hrTargets, hrEmployees);
                    remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
                }
            }
        }
        return hrTargets;
    }

    @NotNull
    private static SysMessage buildMessage(HrTargets hrTargets, HrEmployees hrEmployees) {
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMessageRecipient(hrEmployees.getUserId());
        sysMessage.setMessageStatus("0");
        sysMessage.setMessageType(15);
        sysMessage.setCreateTime(DateUtils.getNowDate());
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", hrTargets.getId());
        map1.put("title", hrTargets.getName());
        sysMessage.setMap1(map1);
        sysMessage.setMap2(map2);
        return sysMessage;
    }

    private void batchSaveTargetEmployee(HrTargets hrTargets) {
        Long[] employeeIds = hrTargets.getEmployeeIds();
        if (employeeIds != null && employeeIds.length != 0) {
            List<HrTargetEmployee> list = new ArrayList<>();
            for (Long employeeId : employeeIds) {
                HrTargetEmployee hrTargetEmployee = new HrTargetEmployee();
                hrTargetEmployee.setEmployeeId(employeeId);
                hrTargetEmployee.setTargetId(hrTargets.getId());
                hrTargetEmployee.setCreateTime(DateUtils.getNowDate());
                hrTargetEmployee.setType(1L);
                list.add(hrTargetEmployee);
            }
            hrTargetEmployeeService.saveBatchEmployees(list);
        }
    }

    /**
     * Update Main table storing all target information
     *
     * @param hrTargets Main table storing all target information
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HrTargets updateHrTargets(HrTargets hrTargets) {
        hrTargets.setUpdateTime(DateUtils.getNowDate());
        int rows = baseMapper.updateById(hrTargets);
        if (rows == 0) {
            throw new OptimisticLockingFailureException("Update failed. The target information has been modified by another user. Please refresh and try again");
        }
        Long[] employeeIds = hrTargets.getEmployeeIds();
        if (employeeIds != null && employeeIds.length != 0) {
            QueryWrapper<HrTargetEmployee> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("target_id",hrTargets.getId());
            queryWrapper.notIn("employee_id",hrTargets.getEmployeeIds());
            hrTargetEmployeeService.remove(queryWrapper);
        }
        batchSaveTargetEmployee(hrTargets);
        return hrTargets;
    }

    /**
     * Delete Main table storing all target information
     *
     * @param id Main table storing all target information primary key
     * @return Result
     */
    @Override
    public int deleteHrTargetsById(Long id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public List<HrTargetEmployee> selectHrTargets(HrTargets hrTargets) {
        List<HrTargetEmployee> hrTargetEmployees = null;
        // Select User Role
        String role = baseMapper.selectUserRole(SecurityUtils.getUserId());
        if ("admin".equals(role)) {
            QueryWrapper<HrEmployees> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", SecurityUtils.getUserId());
            queryWrapper.select("employee_id");
            HrEmployees hrEmployees = hrEmployeesMapper.selectOne(queryWrapper);
            hrTargets.setUserId(hrEmployees.getEmployeeId());
            hrTargets.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
            hrTargetEmployees  = hrTargetEmployeeService.selectEmployeeTargetsByAdmin(hrTargets);
        }else {
            QueryWrapper<HrEmployees> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", SecurityUtils.getUserId());
            queryWrapper.select("employee_id");
            HrEmployees hrEmployees = hrEmployeesMapper.selectOne(queryWrapper);
            hrTargets.setUserId(hrEmployees.getEmployeeId());
            hrTargetEmployees  = hrTargetEmployeeService.selectEmployeeTargets(hrTargets);
        }
        for (HrTargetEmployee hrTargetEmployee : hrTargetEmployees) {
            if ("0".equals(hrTargetEmployee.getIsStart()) || "3".equals(hrTargetEmployee.getIsStart()) || hrTargetEmployee.getIsAssigned() == 0) {
                hrTargetEmployee.setStartStatus(false);
            } else {
                hrTargetEmployee.setStartStatus(true);
            }
            if (hrTargetEmployee.getIsAssigned() == 0){
                hrTargetEmployee.setTargetExecutionTime("00:00:00");
            }else {
                long hh = hrTargetEmployee.getExecutionSecond() / 3600;
                long mm = (hrTargetEmployee.getExecutionSecond() % 3600) / 60;
                long ss = hrTargetEmployee.getExecutionSecond() % 60;
                hrTargetEmployee.setTargetExecutionTime(String.format("%02d:%02d:%02d", hh, mm, ss));
            }
        }
        return hrTargetEmployees;
    }

    @Override
    public int startTask(HrTargetEmployee hrTargetEmployee) {
        HrTargetEmployee targetEmployee = hrTargetEmployeeService.selectHrTargetEmployeeById(hrTargetEmployee.getId());
        hrTargetEmployee.setIsStart("1");
        //Determine whether the current task is started
        if ("1".equals(targetEmployee.getIsStart())) {
            HrTargets targets = selectHrTargetsById(hrTargetEmployee.getTargetId());
            throw new ServiceException(targets.getName() + " executing");
        }
        HrTargetEmployee hrTargetEmployee1 = new HrTargetEmployee();
        hrTargetEmployee1.setEmployeeId(hrTargetEmployee.getEmployeeId());
        hrTargetEmployee1.setIsStart("1");
        List<HrTargetEmployee> hrTargetEmployees = hrTargetEmployeeService.selectHrTargetEmployeeList(hrTargetEmployee1);
        if (!hrTargetEmployees.isEmpty()) {
            throw new ServiceException("There is an start task, please stop it first");
        }
        return hrTargetEmployeeService.updateHrTargetEmployee(hrTargetEmployee);
    }

    @Override
    public void checkHead(Long id){
        if (isAdmin()){
            return;
        }
        if (isNotHr() &&!isHead(id)){
            throw new ServiceException("Non target creators have no authority to operate");
        }
    }

    @Override
    public void checkParticipants(Long id){
        if (isAdmin()){
            return;
        }
        boolean b = isParticipants(id);
        boolean head = isHead(id);
        if (isNotHr() && !head && !b){
            throw new ServiceException("Non target participants have no authority to operate");
        }
    }

    @Override
    public boolean isAdmin(){
        return "01".equals(SecurityUtils.getUserType()) || "00".equals(SecurityUtils.getUserType()) || SecurityUtils.getLoginUser().getRoles().contains("hr");
    }

    public boolean isNotHr(){
        return !SecurityUtils.getLoginUser().getRoles().contains("hr");
    }

    public boolean isHead(Long id){
        HrTargets hrTargets = selectHrTargetsById(id);
        if (ObjectUtils.isNotEmpty(hrTargets)){
            Long head = hrTargets.getHead();
            if (ObjectUtils.isNotEmpty(head)) {
                return SecurityUtils.getUserId().equals(head);
            }
        }
        return false;
    }

    @Override
    public boolean isParticipants(Long id){
        QueryWrapper<HrTargetEmployee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("target_id",id);
        Long userId = SecurityUtils.getUserId();
        HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByUserId(userId);
        queryWrapper.eq("employee_id", hrEmployees.getEmployeeId());
        long count = hrTargetEmployeeService.count(queryWrapper);
        return count != 0;
    }

    @Override
    public void checkHeadByTasks(Long id) {
        if (isAdmin()){
            return;
        }
        if (isNotHr() &&!isHeadByTasks(id)){
            throw new ServiceException("Non target creators have no authority to operate");
        }
    }

    @Override
    public void checkParticipantsByTasks(Long id) {
        if (isAdmin()){
            return;
        }
        boolean isParticipants = isParticipantsByTasks(id);
        boolean isHead = isHeadByTasks(id);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        System.out.println(loginUser.getRoles().contains("hr"));
        if (isNotHr() && !isHead && !isParticipants){
            throw new ServiceException("Non target participants have no authority to operate");
        }
    }

    public boolean isHeadByTasks(Long id){
        HrTargetTasks data = hrTargetTasksService.selectHrTargetTasksById(id);
        return isHead(data.getTargetId());
    }

    public boolean isParticipantsByTasks(Long id){
        HrTargetTasks data = hrTargetTasksService.selectHrTargetTasksById(id);
        Long targetId = data.getTargetId();
        return isParticipants(targetId);
    }
}
