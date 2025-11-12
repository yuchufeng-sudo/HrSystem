package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.DateUtils;
import com.ys.hr.domain.HrEmailTemplate;
import com.ys.hr.mapper.HrEmailTemplateMapper;
import com.ys.hr.service.IHrEmailTemplateService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Email Templates Service Implementation
 *
 * @author ys
 * @date 2025-09-09
 */
@Service
public class HrEmailTemplateServiceImpl extends ServiceImpl<HrEmailTemplateMapper, HrEmailTemplate> implements IHrEmailTemplateService
{

    /**
     * Query Email Templates
     *
     * @param templateId Email Templates primary key
     * @return Email Templates
     */
    @Override
    public HrEmailTemplate selectHrEmailTemplateByTemplateId(Long templateId)
    {
        return baseMapper.selectHrEmailTemplateByTemplateId(templateId);
    }

    /**
     * Query Email Templates list
     *
     * @param hrEmailTemplate Email Templates
     * @return Email Templates
     */
    @Override
    public List<HrEmailTemplate> selectHrEmailTemplateList(HrEmailTemplate hrEmailTemplate)
    {
        return baseMapper.selectHrEmailTemplateList(hrEmailTemplate);
    }

    /**
     * Add Email Templates
     *
     * @param hrEmailTemplate Email Templates
     * @return Result
     */
    @Override
    public int insertHrEmailTemplate(HrEmailTemplate hrEmailTemplate)
    {
        hrEmailTemplate.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrEmailTemplate);
    }

    /**
     * Update Email Templates
     *
     * @param hrEmailTemplate Email Templates
     * @return Result
     */
    @Override
    public int updateHrEmailTemplate(HrEmailTemplate hrEmailTemplate)
    {
        hrEmailTemplate.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrEmailTemplate);
    }

    /**
     * Batch delete Email Templates
     *
     * @param templateIds Email Templates primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrEmailTemplateByTemplateIds(String[] templateIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(templateIds));
    }

    /**
     * Delete Email Templates information
     *
     * @param templateId Email Templates primary key
     * @return Result
     */
    @Override
    public int deleteHrEmailTemplateByTemplateId(String templateId)
    {
        return baseMapper.deleteById(templateId);
    }
}
