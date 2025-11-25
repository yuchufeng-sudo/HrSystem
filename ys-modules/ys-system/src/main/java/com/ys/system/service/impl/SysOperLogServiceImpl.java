package com.ys.system.service.impl;

import com.ys.system.api.domain.SysOperLog;
import com.ys.system.mapper.SysOperLogMapper;
import com.ys.system.service.ISysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Operation Log Service Layer Processing
 *
 * @author ys
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService
{
    @Autowired
    private SysOperLogMapper operLogMapper;

    /**
     * Add Operation Log
     *
     * @param operLog Operation Log Object
     * @return Result
     */
    @Override
    public int insertOperlog(SysOperLog operLog)
    {
        return operLogMapper.insertOperlog(operLog);
    }

    /**
     * Query System Operation Log Set
     *
     * @param operLog Operation Log Object
     * @return Operation Log Set
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * Batch Delete System Operation Log
     *
     * @param operIds The Operation Log ID to be DELETED
     * @return Result
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds)
    {
        return operLogMapper.deleteOperLogByIds(operIds);
    }

    /**
     * Query Operation Log Details
     *
     * @param operId Operation ID
     * @return Operation Log Object
     */
    @Override
    public SysOperLog selectOperLogById(Long operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }

    /**
     * Clear Operation Log
     */
    @Override
    public void cleanOperLog()
    {
        operLogMapper.cleanOperLog();
    }
}
