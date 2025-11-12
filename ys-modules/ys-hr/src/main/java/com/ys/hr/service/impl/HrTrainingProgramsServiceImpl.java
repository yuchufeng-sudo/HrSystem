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
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrTrainingProgramsMapper;
import com.ys.hr.domain.HrTrainingPrograms;
import com.ys.hr.service.IHrTrainingProgramsService;
import com.ys.common.core.utils.DateUtils;

import javax.annotation.Resource;

/**
 *  TRAINING PROGRAM SUPERVISOR  Service Implementation
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
     * Query  TRAINING PROGRAM SUPERVISOR
     *
     * @param programId  TRAINING PROGRAM SUPERVISOR  primary key
     * @return  TRAINING PROGRAM SUPERVISOR
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
     * Query  TRAINING PROGRAM SUPERVISOR  list
     *
     * @param hrTrainingPrograms  TRAINING PROGRAM SUPERVISOR
     * @return  TRAINING PROGRAM SUPERVISOR
     */
    @Override
    public List<HrTrainingPrograms> selectHrTrainingProgramsList(HrTrainingPrograms hrTrainingPrograms)
    {
        return baseMapper.selectHrTrainingProgramsList(hrTrainingPrograms);
    }

    /**
     * Add  TRAINING PROGRAM SUPERVISOR
     *
     * @param hrTrainingPrograms  TRAINING PROGRAM SUPERVISOR
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
     * Update  TRAINING PROGRAM SUPERVISOR
     *
     * @param hrTrainingPrograms  TRAINING PROGRAM SUPERVISOR
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
                            Map<String, Object> map1 = new HashMap<>();
                            Map<String, Object> map2 = new HashMap<>();
                            sysMessage.setMap1(map1);
                            sysMessage.setMap2(map2);
                            remoteMessageService.sendMessageByTemplate(sysMessage,SecurityConstants.INNER);
                    }
                }
            }
        }
        return 1;
    }

    @Override
    public int updateHrTrainingPrograms(HrTrainingPrograms hrTrainingPrograms)
    {
        hrTrainingPrograms.setUpdateTime(DateUtils.getNowDate());
        String[] employeeIds = hrTrainingPrograms.getEmployeeIds();
        if (employeeIds!=null&&employeeIds.length>0){
            QueryWrapper<HrTrainingAssignments> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("program_id",hrTrainingPrograms.getProgramId());
            trainingAssignmentsService.remove(queryWrapper);
            List<HrTrainingAssignments> list = new ArrayList<>();
            for (String employeeId : employeeIds) {
                HrTrainingAssignments trainingAssignments = new HrTrainingAssignments();
                trainingAssignments.setProgramId(hrTrainingPrograms.getProgramId());
                trainingAssignments.setEmployeeId(Long.valueOf(employeeId));
                list.add(trainingAssignments);
                HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesById(Long.valueOf(employeeId));
                if(ObjectUtils.isNotEmpty(hrEmployees)){
                    if("1".equals(hrTrainingPrograms.getNotifyEmployee())){
                        SysMessage sysMessage = new SysMessage();
                        sysMessage.setMessageRecipient(hrEmployees.getUserId());
                        sysMessage.setMessageStatus("0");
                        sysMessage.setMessageType(1);
                        sysMessage.setCreateTime(DateUtils.getNowDate());
                        Map<String, Object> map1 = new HashMap<>();
                        Map<String, Object> map2 = new HashMap<>();
                        sysMessage.setMap1(map1);
                        sysMessage.setMap2(map2);
                        remoteMessageService.sendMessageByTemplate(sysMessage,SecurityConstants.INNER);
                    }
                    if("1".equals(hrTrainingPrograms.getNotifyManager())){
                        if(ObjectUtils.isNotEmpty(hrEmployees.getLeaderId())){
                            HrEmployees hrEmployees2 = hrEmployeesMapper.selectHrEmployeesById(hrEmployees.getLeaderId());
                            SysMessage sysMessage = new SysMessage();
                            sysMessage.setMessageRecipient(hrEmployees2.getUserId());
                            sysMessage.setMessageStatus("0");
                            sysMessage.setMessageType(12);
                            sysMessage.setCreateTime(DateUtils.getNowDate());
                            Map<String, Object> map1 = new HashMap<>();
                            map1.put("department",hrEmployees.getDeptName());
                            map1.put("employeeName",hrEmployees.getFullName());
                            Map<String, Object> map2 = new HashMap<>();
                            sysMessage.setMap1(map1);
                            sysMessage.setMap2(map2);
                            remoteMessageService.sendMessageByTemplate(sysMessage,SecurityConstants.INNER);
                        }
                    }
                }
            }
            trainingAssignmentsService.saveBatch(list);
            hrTrainingPrograms.setAssignedCount(employeeIds.length);
        }
        hrTrainingPrograms.setStatus("ACTIVE");
        return baseMapper.updateById(hrTrainingPrograms);
    }

    /**
     * Batch delete  TRAINING PROGRAM SUPERVISOR
     *
     * @param programIds  TRAINING PROGRAM SUPERVISOR  primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrTrainingProgramsByProgramIds(Long[] programIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(programIds));
    }

    /**
     * Delete  TRAINING PROGRAM SUPERVISOR  information
     *
     * @param programId  TRAINING PROGRAM SUPERVISOR  primary key
     * @return Result
     */
    @Override
    public int deleteHrTrainingProgramsByProgramId(Long programId)
    {
        return baseMapper.deleteById(programId);
    }
}
