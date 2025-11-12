package com.ys.hr.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEnterprise;
import com.ys.utils.email.EmailUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrApplicationMapper;
import com.ys.hr.domain.HrApplication;
import com.ys.hr.service.IHrApplicationService;
import com.ys.common.core.utils.DateUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * application Service Implementation
 *
 * @author ys
 * @date 2025-06-17
 */
@Service
public class HrApplicationServiceImpl extends ServiceImpl<HrApplicationMapper, HrApplication> implements IHrApplicationService
{

    @Resource
    private EmailUtils emailUtils;

    /**
     * Query application
     *
     * @param applicationId application primary key
     * @return application
     */
    @Override
    public HrApplication selectHrApplicationByApplicationId(String applicationId)
    {
        return baseMapper.selectHrApplicationByApplicationId(applicationId);
    }

    /**
     * Query application list
     *
     * @param hrApplication application
     * @return application
     */
    @Override
    public List<HrApplication> selectHrApplicationList(HrApplication hrApplication)
    {
        return baseMapper.selectHrApplicationList(hrApplication);
    }

    /**
     * Add application
     *
     * @param hrApplication application
     * @return Result
     */
    @Override
    public int insertHrApplication(HrApplication hrApplication)
    {
        hrApplication.setCreateTime(DateUtils.getNowDate());
        int insert = baseMapper.insert(hrApplication);
        if(insert>0){
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayStr = today.format(formatter);
            Map<String, Object> map = new HashMap<>();
            map.put("CompanyName",hrApplication.getBussinessName());
            map.put("FullName",hrApplication.getApplicationName());
            map.put("EmailAddress",hrApplication.getApplicationEmail());
            map.put("PhoneNumber",hrApplication.getBussinessPhone());
            map.put("SubmissionDate",todayStr);
            map.put("PendingApprovalsUrl",hrApplication.getUrl());
            Boolean email = emailUtils.sendEmailByTemplate(map, "cheffrey.catalan@teohcapital.com", "Application");
        }
        return insert;
    }

    /**
     * Update application
     *
     * @param hrApplication application
     * @return Result
     */
    @Override
    public int updateHrApplication(HrApplication hrApplication)
    {
        hrApplication.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrApplication);
    }

    /**
     * Batch delete application
     *
     * @param applicationIds application primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrApplicationByApplicationIds(String[] applicationIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(applicationIds));
    }

    /**
     * Delete application information
     *
     * @param applicationId application primary key
     * @return Result
     */
    @Override
    public int deleteHrApplicationByApplicationId(String applicationId)
    {
        return baseMapper.deleteById(applicationId);
    }
}
