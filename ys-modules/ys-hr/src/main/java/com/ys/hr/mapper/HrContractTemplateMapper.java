package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrContractTemplate;

/**
 * Contract template Information Mapper Interface
 *
 * @author ys
 * @date 2025-05-28
 */
public interface HrContractTemplateMapper extends BaseMapper<HrContractTemplate>
{
    /**
     * Query Contract template Information
     *
     * @param templateId Contract template Information primary key
     * @return Contract template Information
     */
    public HrContractTemplate selectHrContractTemplateByTemplateId(Long templateId);
    
    /**
     * Query Contract template Information list
     *
     * @param hrContractTemplate Contract template Information
     * @return Contract template Information collection
     */
    public List<HrContractTemplate> selectHrContractTemplateList(HrContractTemplate hrContractTemplate);

}
