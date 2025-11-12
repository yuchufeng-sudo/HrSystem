package com.ys.system.service.impl;

import com.ys.system.api.domain.SysLogininfor;
import com.ys.system.mapper.SysLogininforMapper;
import com.ys.system.service.ISysLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  System Access LOG Situation INFORMATION Service Layer Processing
 *
 * @author ruoyi
 */
@Service
public class SysLogininforServiceImpl implements ISysLogininforService
{

    @Autowired
    private SysLogininforMapper logininforMapper;

    /**
     * ADD System LOGIN LOG
     *
     * @param logininfor Access LOG Object
     */
    @Override
    public int insertLogininfor(SysLogininfor logininfor)
    {
        return logininforMapper.insertLogininfor(logininfor);
    }

    /**
     * QUERY System LOGIN LOG Set
     *
     * @param logininfor Access LOG Object
     * @return Login RecordSet
     */
    @Override
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor)
    {
        return logininforMapper.selectLogininforList(logininfor);
    }

    @Override
    public Integer selectLogininforCount(SysLogininfor logininfor)
    {
        return logininforMapper.selectLogininforCount(logininfor);
    }

    /**
     * Batch DELETE System LOGIN LOG
     *
     * @param infoIds LOGIN LOG ID to be DELETED
     * @return Result
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds)
    {
        return logininforMapper.deleteLogininforByIds(infoIds);
    }

    /**
     * CLEAR  System LOGIN LOG
     */
    @Override
    public void cleanLogininfor()
    {
        logininforMapper.cleanLogininfor();
    }
}
