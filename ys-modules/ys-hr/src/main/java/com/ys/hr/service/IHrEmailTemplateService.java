package com.ys.hr.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmailTemplate;

import java.util.List;

/**
 * Email Templates Service Interface
 *
 * @author ys
 * @date 2025-09-09
 */
public interface IHrEmailTemplateService extends IService<HrEmailTemplate>
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

    /**
     * Add Email Templates
     *
     * @param hrEmailTemplate Email Templates
     * @return Result
     */
    public int insertHrEmailTemplate(HrEmailTemplate hrEmailTemplate);

    /**
     * Update Email Templates
     *
     * @param hrEmailTemplate Email Templates
     * @return Result
     */
    public int updateHrEmailTemplate(HrEmailTemplate hrEmailTemplate);

    /**
     * Batch delete Email Templates
     *
     * @param templateIds Email Templates primary keys to be deleted
     * @return Result
     */
    public int deleteHrEmailTemplateByTemplateIds(String[] templateIds);

    /**
     * Delete Email Templates information
     *
     * @param templateId Email Templates primary key
     * @return Result
     */
    public int deleteHrEmailTemplateByTemplateId(String templateId);
}