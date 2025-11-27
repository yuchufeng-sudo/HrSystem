package com.ys.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.DateUtils;
import com.ys.system.domain.SysMessageTemplate;
import com.ys.system.mapper.SysMessageTemplateMapper;
import com.ys.system.service.ISysMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  Message Template management Service Business Layer Processing
 *
 * @author ys
 * @date 2024-10-23
 */
@Service
public class SysMessageTemplateServiceImpl extends ServiceImpl<SysMessageTemplateMapper, SysMessageTemplate> implements ISysMessageTemplateService
{
    @Autowired
    private SysMessageTemplateMapper sysMessageTemplateMapper;

    /**
     * Query Message Template management
     *
     * @param id  Message Template management Primary Key
     * @return Message Template management
     */
    @Override
    public SysMessageTemplate selectSysMessageTemplateById(Integer id)
    {
        return sysMessageTemplateMapper.selectSysMessageTemplateById(id);
    }

    /**
     * Query Message Template management list
     *
     * @param sysMessageTemplate  Message Template management
     * @return Message Template management
     */
    @Override
    public List<SysMessageTemplate> selectSysMessageTemplateList(SysMessageTemplate sysMessageTemplate)
    {
        return sysMessageTemplateMapper.selectSysMessageTemplateList(sysMessageTemplate);
    }

    /**
     * Add Message Template management
     *
     * @param sysMessageTemplate  Message Template management
     * @return Result
     */
    @Override
    public int insertSysMessageTemplate(SysMessageTemplate sysMessageTemplate)
    {
        sysMessageTemplate.setCreateTime(DateUtils.getNowDate());
        return sysMessageTemplateMapper.insertSysMessageTemplate(sysMessageTemplate);
    }

    /**
     * Update Message Template management
     *
     * @param sysMessageTemplate  Message Template management
     * @return Result
     */
    @Override
    public int updateSysMessageTemplate(SysMessageTemplate sysMessageTemplate)
    {
        sysMessageTemplate.setUpdateTime(DateUtils.getNowDate());
        return sysMessageTemplateMapper.updateSysMessageTemplate(sysMessageTemplate);
    }

    /**
     * Batch Delete Message Template management
     *
     * @param ids The Message Template management Primary Key to be DELETED
     * @return Result
     */
    @Override
    public int deleteSysMessageTemplateByIds(Integer[] ids)
    {
        return sysMessageTemplateMapper.deleteSysMessageTemplateByIds(ids);
    }

    /**
     * Delete Message Template management   Information
     *
     * @param id  Message Template management Primary Key
     * @return Result
     */
    @Override
    public int deleteSysMessageTemplateById(Integer id)
    {
        return sysMessageTemplateMapper.deleteSysMessageTemplateById(id);
    }
}
