package com.ys.system.service.impl;

import com.ys.system.api.domain.SysLogininfor;
import com.ys.system.mapper.SysLogininforMapper;
import com.ys.system.service.ISysLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  System Access LOG Situation Information Service Layer Processing
 *
 * @author ys
 */
@Service
public class SysLogininforServiceImpl implements ISysLogininforService
{

    @Autowired
    private SysLogininforMapper logininforMapper;

    /**
     * Add System Login Log
     *
     * @param logininfor Access LOG Object
     */
    @Override
    public int insertLogininfor(SysLogininfor logininfor)
    {
        return logininforMapper.insertLogininfor(logininfor);
    }

    /**
     * Query System Login Log Set
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
     * Batch Delete System Login Log
     *
     * @param infoIds Login Log ID to be DELETED
     * @return Result
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds)
    {
        return logininforMapper.deleteLogininforByIds(infoIds);
    }

    /**
     * Clear  System Login Log
     */
    @Override
    public void cleanLogininfor()
    {
        logininforMapper.cleanLogininfor();
    }
}
