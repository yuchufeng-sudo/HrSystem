package com.ys.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.system.domain.SysMessageTemplate;

import java.util.List;

/**
 *
Message Template management Service Interface
 *
 * @author ys
 * @date 2024-10-23
 */
public interface ISysMessageTemplateService extends IService<SysMessageTemplate>
{
    /**
     * Query Message Template management
     *
     * @param id  Message Template management Primary Key
     * @return  Message Template management
     */
    public SysMessageTemplate selectSysMessageTemplateById(Integer id);

    /**
     * Query Message Template management list
     *
     * @param SysMessageTemplate  Message Template management
     * @return  Message Template management Set
     */
    public List<SysMessageTemplate> selectSysMessageTemplateList(SysMessageTemplate SysMessageTemplate);

    /**
     * Add Message Template management
     *
     * @param SysMessageTemplate  Message Template management
     * @return Result
     */
    public int insertSysMessageTemplate(SysMessageTemplate SysMessageTemplate);

    /**
     * Update Message Template management
     *
     * @param SysMessageTemplate  Message Template management
     * @return Result
     */
    public int updateSysMessageTemplate(SysMessageTemplate SysMessageTemplate);

    /**
     * Batch Delete Message Template management
     *
     * @param ids Primary Key Set of Message Template management to be DELETED
     * @return Result
     */
    public int deleteSysMessageTemplateByIds(Integer[] ids);

    /**
     * Delete Message Template management  Information
     *
     * @param id  Message Template management Primary Key
     * @return Result
     */
    public int deleteSysMessageTemplateById(Integer id);
}
