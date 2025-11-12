package com.ys.hr.service.impl;

import java.util.HashMap;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrTargets;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrTargetFeedbackMapper;
import com.ys.hr.domain.HrTargetFeedback;
import com.ys.hr.service.IHrTargetFeedbackService;
import com.ys.common.core.utils.DateUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Target Feedback Service Implementation
 *
 * @author ys
 * @date 2025-06-30
 */
@Service
public class HrTargetFeedbackServiceImpl extends ServiceImpl<HrTargetFeedbackMapper, HrTargetFeedback> implements IHrTargetFeedbackService
{

    @Resource
    private RemoteMessageService remoteMessageService;

    @Resource
    private HrEmployeesMapper hrEmployeesMapper;

    @Resource
    private HrTargetsServiceImpl targetsService;
    /**
     * Query Target Feedback
     *
     * @param id Target Feedback primary key
     * @return Target Feedback
     */
    @Override
    public HrTargetFeedback selectHrTargetFeedbackById(Long id)
    {
        return baseMapper.selectHrTargetFeedbackById(id);
    }

    /**
     * Query Target Feedback list
     *
     * @param hrTargetFeedback Target Feedback
     * @return Target Feedback
     */
    @Override
    public List<HrTargetFeedback> selectHrTargetFeedbackList(HrTargetFeedback hrTargetFeedback)
    {
        return baseMapper.selectHrTargetFeedbackList(hrTargetFeedback);
    }

    /**
     * Add Target Feedback
     *
     * @param hrTargetFeedback Target Feedback
     * @return Result
     */
    @Override
    public int insertHrTargetFeedback(HrTargetFeedback hrTargetFeedback)
    {
        hrTargetFeedback.setCreateTime(DateUtils.getNowDate());
        int insert = baseMapper.insert(hrTargetFeedback);
        if(insert>0){
            Long empId = hrTargetFeedback.getFeedbackTo();
            HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesById(empId);
            HrEmployees hrEmployeeByHr = hrEmployeesMapper.selectHrEmployeesByUserId(SecurityUtils.getUserId());
            HrTargets hrTargets = targetsService.selectHrTargetsById(hrTargetFeedback.getTargetId());
            AjaxResult info = remoteMessageService.getInfo(SecurityUtils.getUserId(),SecurityConstants.INNER);
            Map<String,String> setting = (Map<String, String>) info.get("data");
            String performanceEvaluationAlerts = setting.get("performanceEvaluationAlerts");
            if(ObjectUtils.isNotEmpty(hrEmployees) && ObjectUtils.isNotEmpty(hrEmployeeByHr) && "1".equals(performanceEvaluationAlerts)){
                SysMessage sysMessage = new SysMessage();
                sysMessage.setMessageRecipient(hrEmployees.getUserId());
                sysMessage.setMessageStatus("0");
                sysMessage.setMessageType(13);
                sysMessage.setCreateTime(DateUtils.getNowDate());
                Map<String, Object> map1 = new HashMap<>();
                Map<String, Object> map2 = new HashMap<>();
                map1.put("name", hrEmployeeByHr.getFullName());
                map1.put("targetName", hrTargets.getName());
                map1.put("rating", hrTargetFeedback.getRating());
                map1.put("categoryList", hrTargetFeedback.getCategory());
                map1.put("details", hrTargetFeedback.getFeedbackContent());
                sysMessage.setMap1(map1);
                sysMessage.setMap2(map2);
                remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
            }
        }
        return insert;
    }

    /**
     * Update Target Feedback
     *
     * @param hrTargetFeedback Target Feedback
     * @return Result
     */
    @Override
    public int updateHrTargetFeedback(HrTargetFeedback hrTargetFeedback)
    {
        hrTargetFeedback.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrTargetFeedback);
    }
}
