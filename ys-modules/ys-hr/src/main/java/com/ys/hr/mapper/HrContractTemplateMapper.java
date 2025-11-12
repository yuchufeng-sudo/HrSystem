package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrContractTemplate;

/**
 * CONTRACT TEMPLATE INFORMATION Mapper Interface
 *
 * @author ys
 * @date 2025-05-28
 */
public interface HrContractTemplateMapper extends BaseMapper<HrContractTemplate>
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

}