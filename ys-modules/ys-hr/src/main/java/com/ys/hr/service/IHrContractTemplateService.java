package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrContractTemplate;

import java.util.List;

/**
 * CONTRACT TEMPLATE Information Service Interface
 *
 * @author ys
 * @date 2025-05-28
 */
public interface IHrContractTemplateService extends IService<HrContractTemplate>
{
    /**
     * Query CONTRACT TEMPLATE Information
     *
     * @param templateId CONTRACT TEMPLATE Information primary key
     * @return CONTRACT TEMPLATE Information
     */
    public HrContractTemplate selectHrContractTemplateByTemplateId(Long templateId);

    /**
     * Query CONTRACT TEMPLATE Information list
     *
     * @param hrContractTemplate CONTRACT TEMPLATE Information
     * @return CONTRACT TEMPLATE Information collection
     */
    public List<HrContractTemplate> selectHrContractTemplateList(HrContractTemplate hrContractTemplate);

    /**
     * Add CONTRACT TEMPLATE Information
     *
     * @param hrContractTemplate CONTRACT TEMPLATE Information
     * @return Result
     */
    public int insertHrContractTemplate(HrContractTemplate hrContractTemplate);

    /**
     * Update CONTRACT TEMPLATE Information
     *
     * @param hrContractTemplate CONTRACT TEMPLATE Information
     * @return Result
     */
    public int updateHrContractTemplate(HrContractTemplate hrContractTemplate);

    /**
     * Batch delete CONTRACT TEMPLATE Information
     *
     * @param templateIds CONTRACT TEMPLATE Information primary keys to be deleted
     * @return Result
     */
    public int deleteHrContractTemplateByTemplateIds(String[] templateIds);

    /**
     * Delete CONTRACT TEMPLATE Information information
     *
     * @param templateId CONTRACT TEMPLATE Information primary key
     * @return Result
     */
    public int deleteHrContractTemplateByTemplateId(String templateId);
}
