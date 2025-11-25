package com.ys.system.service;

import com.ys.system.api.domain.SysOperLog;

import java.util.List;

/**
 * Operation Log Service Layer
 *
 * @author ys
 */
public interface ISysOperLogService
{
    /**
     * Add Operation Log
     *
     * @param operLog Operation Log Object
     * @return Result
     */
    public int insertOperlog(SysOperLog operLog);

    /**
     * Query System Operation Log Set
     *
     * @param operLog Operation Log Object
     * @return Operation Log Set
     */
    public List<SysOperLog> selectOperLogList(SysOperLog operLog);

    /**
     * Batch Delete System Operation Log
     *
     * @param operIds The Operation Log ID to be DELETED
     * @return Result
     */
    public int deleteOperLogByIds(Long[] operIds);

    /**
     * Query Operation Log Details
     *
     * @param operId Operation ID
     * @return Operation Log Object
     */
    public SysOperLog selectOperLogById(Long operId);

    /**
     * Clear Operation Log
     */
    public void cleanOperLog();
}
