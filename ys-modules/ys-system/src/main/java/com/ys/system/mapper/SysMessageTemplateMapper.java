package com.ys.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.system.domain.SysMessageTemplate;

import java.util.List;

/**
 *  MESSAGE TEMPLATE 
 MANAGEMENT  Mapper Interface
 *
 * @author ruoyi
 * @date 2024-10-23
 */
public interface SysMessageTemplateMapper extends BaseMapper<SysMessageTemplate>
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
     * @param sysMessageTemplate  MESSAGE TEMPLATE 
 MANAGEMENT  
     * @return  MESSAGE TEMPLATE 
 MANAGEMENT  Set
     */
    public List<SysMessageTemplate> selectSysMessageTemplateList(SysMessageTemplate sysMessageTemplate);

    /**
     * ADD MESSAGE TEMPLATE 
 MANAGEMENT  
     *
     * @param sysMessageTemplate  MESSAGE TEMPLATE 
 MANAGEMENT  
     * @return Result
     */
    public int insertSysMessageTemplate(SysMessageTemplate sysMessageTemplate);

    /**
     * MODIFY MESSAGE TEMPLATE 
 MANAGEMENT  
     *
     * @param sysMessageTemplate  MESSAGE TEMPLATE 
 MANAGEMENT  
     * @return Result
     */
    public int updateSysMessageTemplate(SysMessageTemplate sysMessageTemplate);

    /**
     * DELETE MESSAGE TEMPLATE 
 MANAGEMENT  
     *
     * @param id  MESSAGE TEMPLATE MANAGEMENT Primary Key
     * @return Result
     */
    public int deleteSysMessageTemplateById(Integer id);

    /**
     * Batch DELETE MESSAGE TEMPLATE 
 MANAGEMENT  
     *
     * @param The primary key set of data that needs to be deleted for ids.
     * @return Result
     */
    public int deleteSysMessageTemplateByIds(Integer[] ids);
}
