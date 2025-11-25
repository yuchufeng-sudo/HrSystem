package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrContractTemplate;

/**
 * CONTRACT TEMPLATE Information Mapper Interface
 *
 * @author ys
 * @date 2025-05-28
 */
public interface HrContractTemplateMapper extends BaseMapper<HrContractTemplate>
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

}
