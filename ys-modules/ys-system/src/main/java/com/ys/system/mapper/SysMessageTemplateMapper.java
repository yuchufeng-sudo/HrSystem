package com.ys.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.system.domain.SysMessageTemplate;

import java.util.List;

/**
 *  Message Template management Mapper Interface
 *
 * @author ys
 * @date 2024-10-23
 */
public interface SysMessageTemplateMapper extends BaseMapper<SysMessageTemplate>
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
     * @param sysMessageTemplate  Message Template management
     * @return  Message Template management Set
     */
    public List<SysMessageTemplate> selectSysMessageTemplateList(SysMessageTemplate sysMessageTemplate);

    /**
     * Add Message Template management
     *
     * @param sysMessageTemplate  Message Template management
     * @return Result
     */
    public int insertSysMessageTemplate(SysMessageTemplate sysMessageTemplate);

    /**
     * Update Message Template management
     *
     * @param sysMessageTemplate  Message Template management
     * @return Result
     */
    public int updateSysMessageTemplate(SysMessageTemplate sysMessageTemplate);

    /**
     * Delete Message Template management
     *
     * @param id  Message Template management Primary Key
     * @return Result
     */
    public int deleteSysMessageTemplateById(Integer id);

    /**
     * Batch Delete Message Template management
     *
     * @param The primary key set of data that needs to be deleted for ids.
     * @return Result
     */
    public int deleteSysMessageTemplateByIds(Integer[] ids);
}
