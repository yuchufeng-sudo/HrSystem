package com.ys.hr.service.impl;

import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrTargets;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrTargetTasksMapper;
import com.ys.hr.domain.HrTargetTasks;
import com.ys.hr.service.IHrTargetTasksService;
import com.ys.common.core.utils.DateUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Manages task assignments and progress tracking for employee targets Service Implementation
 *
 * @author ys
 * @date 2025-06-18
 */
@Service
public class HrTargetTasksServiceImpl extends ServiceImpl<HrTargetTasksMapper, HrTargetTasks> implements IHrTargetTasksService
{

    @Resource
    private HrEmployeesMapper hrEmployeesMapper;

    @Resource
    private RemoteMessageService remoteMessageService;

    @Resource
    private HrTargetsServiceImpl hrTargetsService;

    /**
     * Query Manages task assignments and progress tracking for employee targets
     *
     * @param id Manages task assignments and progress tracking for employee targets primary key
     * @return Manages task assignments and progress tracking for employee targets
     */
    @Override
    public HrTargetTasks selectHrTargetTasksById(Long id)
    {
        return baseMapper.selectHrTargetTasksById(id);
    }

    /**
     * Query Manages task assignments and progress tracking for employee targets list
     *
     * @param hrTargetTasks Manages task assignments and progress tracking for employee targets
     * @return Manages task assignments and progress tracking for employee targets
     */
    @Override
    public List<HrTargetTasks> selectHrTargetTasksList(HrTargetTasks hrTargetTasks)
    {
        return baseMapper.selectHrTargetTasksList(hrTargetTasks);
    }

    /**
     * Add Manages task assignments and progress tracking for employee targets
     *
     * @param hrTargetTasks Manages task assignments and progress tracking for employee targets
     * @return Result
     */
    @Override
    @Transactional
    public int insertHrTargetTasks(HrTargetTasks hrTargetTasks)
    {
        hrTargetTasks.setCreateBy(SecurityUtils.getUsername());
        hrTargetTasks.setCreateTime(DateUtils.getNowDate());
        int insert = baseMapper.insert(hrTargetTasks);
        HrTargets hrTargets = hrTargetsService.selectHrTargetsById(hrTargetTasks.getTargetId());
        if (insert > 0 && ObjectUtils.isNotEmpty(hrTargets)) {
            HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByUserId(SecurityUtils.getUserId());
            SysMessage sysMessage = new SysMessage();
            sysMessage.setMessageRecipient(hrTargets.getHead());
            sysMessage.setMessageStatus("0");
            sysMessage.setMessageType(16);
            sysMessage.setCreateTime(DateUtils.getNowDate());
            Map<String, Object> map1 = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            map2.put("id", hrTargetTasks.getTargetId());
            if(ObjectUtils.isNotEmpty(hrEmployees)){
                map1.put("EmpName", hrEmployees.getFullName());
            }
            map1.put("title", hrTargets.getName());
            sysMessage.setMap1(map1);
            sysMessage.setMap2(map2);
            remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
        }
        return insert;
    }

    /**
     * Update Manages task assignments and progress tracking for employee targets
     *
     * @param hrTargetTasks Manages task assignments and progress tracking for employee targets
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateHrTargetTasks(HrTargetTasks hrTargetTasks)
    {
        hrTargetTasks.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrTargetTasks);
    }
}
