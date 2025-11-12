package com.ys.system.service.impl;

import com.ys.system.api.domain.SysOperLog;
import com.ys.system.mapper.SysOperLogMapper;
import com.ys.system.service.ISysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * OPERATION LOG Service Layer Processing
 *
 * @author ruoyi
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService
{
    @Autowired
    private SysOperLogMapper operLogMapper;

    /**
     * ADDOPERATION LOG
     *
     * @param operLog OPERATION LOG Object
     * @return Result
     */
    @Override
    public int insertOperlog(SysOperLog operLog)
    {
        return operLogMapper.insertOperlog(operLog);
    }

    /**
     * QUERY SystemOPERATION LOG Set
     *
     * @param operLog OPERATION LOG Object
     * @return OPERATION LOG Set
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * Batch DELETE SystemOPERATION LOG
     *
     * @param operIds The OPERATION LOG ID to be DELETED
     * @return Result
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds)
    {
        return operLogMapper.deleteOperLogByIds(operIds);
    }

    /**
     * QUERYOPERATION LOG DETAILEDLY
     *
     * @param operId OPERATIONID
     * @return OPERATION LOG Object
     */
    @Override
    public SysOperLog selectOperLogById(Long operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }

    /**
     * CLEAR OPERATION LOG
     */
    @Override
    public void cleanOperLog()
    {
        operLogMapper.cleanOperLog();
    }
}
