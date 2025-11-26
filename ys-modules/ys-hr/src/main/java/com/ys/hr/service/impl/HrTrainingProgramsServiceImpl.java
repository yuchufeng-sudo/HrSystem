package com.ys.hr.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrTrainingAssignments;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.hr.service.IHrTrainingAssignmentsService;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrTrainingProgramsMapper;
import com.ys.hr.domain.HrTrainingPrograms;
import com.ys.hr.service.IHrTrainingProgramsService;
import com.ys.common.core.utils.DateUtils;

import javax.annotation.Resource;

/**
 *  Training Management Service Implementation
 *
 * @author ys
 * @date 2025-05-29
 */
@Service
public class HrTrainingProgramsServiceImpl extends ServiceImpl<HrTrainingProgramsMapper, HrTrainingPrograms> implements IHrTrainingProgramsService
{

    @Resource
    private IHrTrainingAssignmentsService trainingAssignmentsService;

    @Resource
    private RemoteMessageService remoteMessageService;

    @Resource
    private HrEmployeesMapper hrEmployeesMapper;

    /**
     * Query Training Management
     *
     * @param programId  Training Management primary key
     * @return Training Management
     */
    @Override
    public HrTrainingPrograms selectHrTrainingProgramsByProgramId(Long programId)
    {
        HrTrainingPrograms hrTrainingPrograms = baseMapper.selectHrTrainingProgramsByProgramId(programId);
        QueryWrapper<HrTrainingAssignments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("program_id",programId);
        queryWrapper.select("employee_id");
        List<HrTrainingAssignments> list = trainingAssignmentsService.list(queryWrapper);
        hrTrainingPrograms.setEmployeeIds(list.stream()
                .map(HrTrainingAssignments::getEmployeeId)
                .map(String::valueOf)
                .toArray(String[]::new));
        return hrTrainingPrograms;
    }

    /**
     * Query Training Management list
     *
     * @param hrTrainingPrograms Training Management
     * @return Training Management
     */
    @Override
    public List<HrTrainingPrograms> selectHrTrainingProgramsList(HrTrainingPrograms hrTrainingPrograms)
    {
        return baseMapper.selectHrTrainingProgramsList(hrTrainingPrograms);
    }

    /**
     * Add Training Management
     *
     * @param hrTrainingPrograms Training Management
     * @return Result
     */
    @Override
    public int insertHrTrainingPrograms(HrTrainingPrograms hrTrainingPrograms)
    {
        hrTrainingPrograms.setCreateTime(DateUtils.getNowDate());
        hrTrainingPrograms.setAssignedCount(0);
        hrTrainingPrograms.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        hrTrainingPrograms.setStatus("DRAFT");
        return baseMapper.insert(hrTrainingPrograms);
    }

    /**
     * Update Training Management
     *
     * @param hrTrainingPrograms Training Management
     * @return Result
     */
    @Override
    public int checkTrainingPrograms(HrTrainingPrograms hrTrainingPrograms)
    {
        hrTrainingPrograms.setUpdateTime(DateUtils.getNowDate());
        String[] employeeIds = hrTrainingPrograms.getEmployeeIds();
        if (employeeIds!=null&&employeeIds.length>0){
            Date deadline = hrTrainingPrograms.getDeadline();
            LocalDate deadDate = deadline.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate today = LocalDate.now();
            long daysUntilDeadline  = ChronoUnit.DAYS.between(today, deadDate);
            if (daysUntilDeadline == 1 || daysUntilDeadline == 0) {
                for (String employeeId : employeeIds) {
                    HrTrainingAssignments trainingAssignments = new HrTrainingAssignments();
                    trainingAssignments.setProgramId(hrTrainingPrograms.getProgramId());
                    trainingAssignments.setEmployeeId(Long.valueOf(employeeId));
                    HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesById(Long.valueOf(employeeId));
                    if(ObjectUtils.isNotEmpty(hrEmployees)){
                            SysMessage sysMessage = new SysMessage();
                            sysMessage.setMessageRecipient(hrEmployees.getUserId());
                            sysMessage.setMessageStatus("0");
                            sysMessage.setMessageType(2);
                            sysMessage.setCreateTime(DateUtils.getNowDate());
                        setMessageMaps(sysMessage);
                    }
                }
            }
        }
        return 1;
    }

    @Override
    public int updateHrTrainingPrograms(HrTrainingPrograms hrTrainingPrograms) {
        hrTrainingPrograms.setUpdateTime(DateUtils.getNowDate());
        String[] employeeIds = hrTrainingPrograms.getEmployeeIds();

        if (employeeIds != null && employeeIds.length > 0) {
            removeExistingAssignments(hrTrainingPrograms.getProgramId());
            List<HrTrainingAssignments> assignments = createAndNotifyAssignments(
                    employeeIds,
                    hrTrainingPrograms
            );
            trainingAssignmentsService.saveBatch(assignments);
            hrTrainingPrograms.setAssignedCount(employeeIds.length);
        }

        hrTrainingPrograms.setStatus("ACTIVE");
        return baseMapper.updateById(hrTrainingPrograms);
    }

    /**
     * Remove existing training assignments for a program
     *
     * @param programId the training program ID
     */
    private void removeExistingAssignments(Long programId) {
        QueryWrapper<HrTrainingAssignments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("program_id", programId);
        trainingAssignmentsService.remove(queryWrapper);
    }

    /**
     * Create training assignments and send notifications to employees and managers
     *
     * @param employeeIds array of employee IDs to assign
     * @param program the training program details
     * @return list of created training assignments
     */
    private List<HrTrainingAssignments> createAndNotifyAssignments(
            String[] employeeIds,
            HrTrainingPrograms program) {
        List<HrTrainingAssignments> list = new ArrayList<>();

        for (String employeeId : employeeIds) {
            HrTrainingAssignments assignment = createAssignment(program.getProgramId(), employeeId);
            list.add(assignment);

            HrEmployees employee = hrEmployeesMapper.selectHrEmployeesById(Long.valueOf(employeeId));
            if (ObjectUtils.isNotEmpty(employee)) {
                sendNotifications(employee, program);
            }
        }

        return list;
    }

    /**
     * Create a single training assignment record
     *
     * @param programId the training program ID
     * @param employeeId the employee ID
     * @return the created training assignment
     */
    private HrTrainingAssignments createAssignment(Long programId, String employeeId) {
        HrTrainingAssignments assignment = new HrTrainingAssignments();
        assignment.setProgramId(programId);
        assignment.setEmployeeId(Long.valueOf(employeeId));
        return assignment;
    }

    /**
     * Send notifications to employee and their manager based on program settings
     *
     * @param employee the employee information
     * @param program the training program with notification settings
     */
    private void sendNotifications(HrEmployees employee, HrTrainingPrograms program) {
        // Notify employee
        if ("1".equals(program.getNotifyEmployee())) {
            SysMessage employeeMessage = buildMessage(employee.getUserId(), 1);
            setMessageMaps(employeeMessage);
        }

        // Notify manager
        if ("1".equals(program.getNotifyManager()) && ObjectUtils.isNotEmpty(employee.getLeaderId())) {
            sendManagerNotification(employee);
        }
    }

    /**
     * Send notification to employee's manager about training assignment
     *
     * @param employee the employee whose manager will be notified
     */
    private void sendManagerNotification(HrEmployees employee) {
        HrEmployees manager = hrEmployeesMapper.selectHrEmployeesById(employee.getLeaderId());

        SysMessage managerMessage = buildMessage(manager.getUserId(), 12);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("department", employee.getDeptName());
        map1.put("employeeName", employee.getFullName());

        managerMessage.setMap1(map1);
        managerMessage.setMap2(new HashMap<>());

        remoteMessageService.sendMessageByTemplate(managerMessage, SecurityConstants.INNER);
    }

    private void setMessageMaps(SysMessage sysMessage) {
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        sysMessage.setMap1(map1);
        sysMessage.setMap2(map2);
        remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
    }

    @NotNull
    private static SysMessage buildMessage(Long userId, Integer messageType) {
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMessageRecipient(userId);
        sysMessage.setMessageStatus("0");
        sysMessage.setMessageType(messageType);
        sysMessage.setCreateTime(DateUtils.getNowDate());
        return sysMessage;
    }

    /**
     * Batch delete  Training Management
     *
     * @param programIds  Training Management primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrTrainingProgramsByProgramIds(Long[] programIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(programIds));
    }

    /**
     * Delete Training Management information
     *
     * @param programId  Training Management primary key
     * @return Result
     */
    @Override
    public int deleteHrTrainingProgramsByProgramId(Long programId)
    {
        return baseMapper.deleteById(programId);
    }
}
