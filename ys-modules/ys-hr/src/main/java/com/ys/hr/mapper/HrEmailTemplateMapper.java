package com.ys.hr.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrEmailTemplate;

import java.util.List;

/**
 * Email Templates Mapper Interface
 *
 * @author ys
 * @date 2025-09-09
 */
public interface HrEmailTemplateMapper extends BaseMapper<HrEmailTemplate>
{
    /**
     * Query Email Templates
     *
     * @param templateId Email Templates primary key
     * @return Email Templates
     */
    public HrEmailTemplate selectHrEmailTemplateByTemplateId(Long templateId);
    
    /**
     * Query Email Templates list
     *
     * @param hrEmailTemplate Email Templates
     * @return Email Templates collection
     */
    public List<HrEmailTemplate> selectHrEmailTemplateList(HrEmailTemplate hrEmailTemplate);

}