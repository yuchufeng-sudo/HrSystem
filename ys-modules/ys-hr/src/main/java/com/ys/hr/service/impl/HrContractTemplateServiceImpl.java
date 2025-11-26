package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.service.IHrContractTemplateService;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrContractTemplateMapper;
import com.ys.hr.domain.HrContractTemplate;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Contract template Information Service Implementation
 *
 * @author ys
 * @date 2025-05-28
 */
@Service
public class HrContractTemplateServiceImpl extends ServiceImpl<HrContractTemplateMapper, HrContractTemplate> implements IHrContractTemplateService
{

    /**
     * Query Contract template Information
     *
     * @param templateId Contract template Information primary key
     * @return Contract template Information
     */
    @Override
    public HrContractTemplate selectHrContractTemplateByTemplateId(Long templateId)
    {
        return baseMapper.selectHrContractTemplateByTemplateId(templateId);
    }

    /**
     * Query Contract template Information list
     *
     * @param hrContractTemplate Contract template Information
     * @return Contract template Information
     */
    @Override
    public List<HrContractTemplate> selectHrContractTemplateList(HrContractTemplate hrContractTemplate)
    {
        return baseMapper.selectHrContractTemplateList(hrContractTemplate);
    }

    /**
     * Add Contract template Information
     *
     * @param hrContractTemplate Contract template Information
     * @return Result
     */
    @Override
    public int insertHrContractTemplate(HrContractTemplate hrContractTemplate)
    {
        hrContractTemplate.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        hrContractTemplate.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrContractTemplate);
    }

    /**
     * Update Contract template Information
     *
     * @param hrContractTemplate Contract template Information
     * @return Result
     */
    @Override
    public int updateHrContractTemplate(HrContractTemplate hrContractTemplate)
    {
        hrContractTemplate.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrContractTemplate);
    }

    /**
     * Batch delete Contract template Information
     *
     * @param templateIds Contract template Information primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrContractTemplateByTemplateIds(String[] templateIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(templateIds));
    }

    /**
     * Delete Contract template Information information
     *
     * @param templateId Contract template Information primary key
     * @return Result
     */
    @Override
    public int deleteHrContractTemplateByTemplateId(String templateId)
    {
        return baseMapper.deleteById(templateId);
    }
}
