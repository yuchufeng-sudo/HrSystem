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
 *  MESSAGE TEMPLATE MANAGEMENT Service Business Layer Processing
 *
 * @author ruoyi
 * @date 2024-10-23
 */
@Service
public class SysMessageTemplateServiceImpl extends ServiceImpl<SysMessageTemplateMapper, SysMessageTemplate> implements ISysMessageTemplateService
{
    @Autowired
    private SysMessageTemplateMapper sysMessageTemplateMapper;

    /**
     * QUERY MESSAGE TEMPLATE MANAGEMENT  
     *
     * @param id  MESSAGE TEMPLATE MANAGEMENT Primary Key
     * @return  MESSAGE TEMPLATE MANAGEMENT  
     */
    @Override
    public SysMessageTemplate selectSysMessageTemplateById(Integer id)
    {
        return sysMessageTemplateMapper.selectSysMessageTemplateById(id);
    }

    /**
     * QUERY MESSAGE TEMPLATE 
 MANAGEMENT    LIST
     *
     * @param sysMessageTemplate  MESSAGE TEMPLATE 
 MANAGEMENT  
     * @return  MESSAGE TEMPLATE 
 MANAGEMENT  
     */
    @Override
    public List<SysMessageTemplate> selectSysMessageTemplateList(SysMessageTemplate sysMessageTemplate)
    {
        return sysMessageTemplateMapper.selectSysMessageTemplateList(sysMessageTemplate);
    }

    /**
     * ADD MESSAGE TEMPLATE 
 MANAGEMENT  
     *
     * @param sysMessageTemplate  MESSAGE TEMPLATE 
 MANAGEMENT  
     * @return Result
     */
    @Override
    public int insertSysMessageTemplate(SysMessageTemplate sysMessageTemplate)
    {
        sysMessageTemplate.setCreateTime(DateUtils.getNowDate());
        return sysMessageTemplateMapper.insertSysMessageTemplate(sysMessageTemplate);
    }

    /**
     * MODIFY MESSAGE TEMPLATE 
 MANAGEMENT  
     *
     * @param sysMessageTemplate  MESSAGE TEMPLATE 
 MANAGEMENT  
     * @return Result
     */
    @Override
    public int updateSysMessageTemplate(SysMessageTemplate sysMessageTemplate)
    {
        sysMessageTemplate.setUpdateTime(DateUtils.getNowDate());
        return sysMessageTemplateMapper.updateSysMessageTemplate(sysMessageTemplate);
    }

    /**
     * Batch DELETE MESSAGE TEMPLATE 
 MANAGEMENT  
     *
     * @param ids The MESSAGE TEMPLATE MANAGEMENT Primary Key to be DELETED
     * @return Result
     */
    @Override
    public int deleteSysMessageTemplateByIds(Integer[] ids)
    {
        return sysMessageTemplateMapper.deleteSysMessageTemplateByIds(ids);
    }

    /**
     * DELETE MESSAGE TEMPLATE MANAGEMENT   INFORMATION
     *
     * @param id  MESSAGE TEMPLATE MANAGEMENT Primary Key
     * @return Result
     */
    @Override
    public int deleteSysMessageTemplateById(Integer id)
    {
        return sysMessageTemplateMapper.deleteSysMessageTemplateById(id);
    }
}
