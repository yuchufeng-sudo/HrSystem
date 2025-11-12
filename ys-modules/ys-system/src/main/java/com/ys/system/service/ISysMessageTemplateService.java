package com.ys.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.system.domain.SysMessageTemplate;

import java.util.List;

/**
 *  
MESSAGE TEMPLATE MANAGEMENT Service Interface
 *
 * @author ruoyi
 * @date 2024-10-23
 */
public interface ISysMessageTemplateService extends IService<SysMessageTemplate>
{
    /**
     * QUERY MESSAGE TEMPLATE 
 MANAGEMENT  
     *
     * @param id  MESSAGE TEMPLATE MANAGEMENT Primary Key
     * @return  MESSAGE TEMPLATE 
 MANAGEMENT  
     */
    public SysMessageTemplate selectSysMessageTemplateById(Integer id);

    /**
     * QUERY MESSAGE TEMPLATE 
 MANAGEMENT    LIST
     *
     * @param SysMessageTemplate  MESSAGE TEMPLATE 
 MANAGEMENT  
     * @return  MESSAGE TEMPLATE 
 MANAGEMENT  Set
     */
    public List<SysMessageTemplate> selectSysMessageTemplateList(SysMessageTemplate SysMessageTemplate);

    /**
     * ADD MESSAGE TEMPLATE 
 MANAGEMENT  
     *
     * @param SysMessageTemplate  MESSAGE TEMPLATE 
 MANAGEMENT  
     * @return Result
     */
    public int insertSysMessageTemplate(SysMessageTemplate SysMessageTemplate);

    /**
     * MODIFY MESSAGE TEMPLATE 
 MANAGEMENT  
     *
     * @param SysMessageTemplate  MESSAGE TEMPLATE 
 MANAGEMENT  
     * @return Result
     */
    public int updateSysMessageTemplate(SysMessageTemplate SysMessageTemplate);

    /**
     * Batch DELETE MESSAGE TEMPLATE 
 MANAGEMENT  
     *
     * @param ids Primary Key Set of MESSAGE TEMPLATE MANAGEMENT to be DELETED
     * @return Result
     */
    public int deleteSysMessageTemplateByIds(Integer[] ids);

    /**
     * DELETE MESSAGE TEMPLATE 
 MANAGEMENT   INFORMATION
     *
     * @param id  MESSAGE TEMPLATE MANAGEMENT Primary Key
     * @return Result
     */
    public int deleteSysMessageTemplateById(Integer id);
}
