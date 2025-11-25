package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.security.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrContractTemplateMapper;
import com.ys.hr.domain.HrContractTemplate;
import com.ys.hr.service.IHrContractTemplateService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * CONTRACT TEMPLATE Information Service Implementation
 *
 * @author ys
 * @date 2025-05-28
 */
@Service
public class HrContractTemplateServiceImpl extends ServiceImpl<HrContractTemplateMapper, HrContractTemplate> implements IHrContractTemplateService
{

    /**
     * Query CONTRACT TEMPLATE Information
     *
     * @param templateId CONTRACT TEMPLATE Information primary key
     * @return CONTRACT TEMPLATE Information
     */
    @Override
    public HrContractTemplate selectHrContractTemplateByTemplateId(Long templateId)
    {
        return baseMapper.selectHrContractTemplateByTemplateId(templateId);
    }

    /**
     * Query CONTRACT TEMPLATE Information list
     *
     * @param hrContractTemplate CONTRACT TEMPLATE Information
     * @return CONTRACT TEMPLATE Information
     */
    @Override
    public List<HrContractTemplate> selectHrContractTemplateList(HrContractTemplate hrContractTemplate)
    {
        return baseMapper.selectHrContractTemplateList(hrContractTemplate);
    }

    /**
     * Add CONTRACT TEMPLATE Information
     *
     * @param hrContractTemplate CONTRACT TEMPLATE Information
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
     * Update CONTRACT TEMPLATE Information
     *
     * @param hrContractTemplate CONTRACT TEMPLATE Information
     * @return Result
     */
    @Override
    public int updateHrContractTemplate(HrContractTemplate hrContractTemplate)
    {
        hrContractTemplate.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrContractTemplate);
    }

    /**
     * Batch delete CONTRACT TEMPLATE Information
     *
     * @param templateIds CONTRACT TEMPLATE Information primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrContractTemplateByTemplateIds(String[] templateIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(templateIds));
    }

    /**
     * Delete CONTRACT TEMPLATE Information information
     *
     * @param templateId CONTRACT TEMPLATE Information primary key
     * @return Result
     */
    @Override
    public int deleteHrContractTemplateByTemplateId(String templateId)
    {
        return baseMapper.deleteById(templateId);
    }
}
