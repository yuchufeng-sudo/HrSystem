package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrContractTemplate;

import java.util.List;

/**
 * CONTRACT TEMPLATE INFORMATION Service Interface
 *
 * @author ys
 * @date 2025-05-28
 */
public interface IHrContractTemplateService extends IService<HrContractTemplate>
{
    /**
     * Query CONTRACT TEMPLATE INFORMATION
     *
     * @param templateId CONTRACT TEMPLATE INFORMATION primary key
     * @return CONTRACT TEMPLATE INFORMATION
     */
    public HrContractTemplate selectHrContractTemplateByTemplateId(Long templateId);

    /**
     * Query CONTRACT TEMPLATE INFORMATION list
     *
     * @param hrContractTemplate CONTRACT TEMPLATE INFORMATION
     * @return CONTRACT TEMPLATE INFORMATION collection
     */
    public List<HrContractTemplate> selectHrContractTemplateList(HrContractTemplate hrContractTemplate);

    /**
     * Add CONTRACT TEMPLATE INFORMATION
     *
     * @param hrContractTemplate CONTRACT TEMPLATE INFORMATION
     * @return Result
     */
    public int insertHrContractTemplate(HrContractTemplate hrContractTemplate);

    /**
     * Update CONTRACT TEMPLATE INFORMATION
     *
     * @param hrContractTemplate CONTRACT TEMPLATE INFORMATION
     * @return Result
     */
    public int updateHrContractTemplate(HrContractTemplate hrContractTemplate);

    /**
     * Batch delete CONTRACT TEMPLATE INFORMATION
     *
     * @param templateIds CONTRACT TEMPLATE INFORMATION primary keys to be deleted
     * @return Result
     */
    public int deleteHrContractTemplateByTemplateIds(String[] templateIds);

    /**
     * Delete CONTRACT TEMPLATE INFORMATION information
     *
     * @param templateId CONTRACT TEMPLATE INFORMATION primary key
     * @return Result
     */
    public int deleteHrContractTemplateByTemplateId(String templateId);
}
