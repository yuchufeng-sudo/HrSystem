package com.ys.system.service;

import com.ys.system.api.domain.SysOperLog;

import java.util.List;

/**
 * OPERATION LOG Service Layer
 *
 * @author ruoyi
 */
public interface ISysOperLogService
{
    /**
     * ADDOPERATION LOG
     *
     * @param operLog OPERATION LOG Object
     * @return Result
     */
    public int insertOperlog(SysOperLog operLog);

    /**
     * QUERY SystemOPERATION LOG Set
     *
     * @param operLog OPERATION LOG Object
     * @return OPERATION LOG Set
     */
    public List<SysOperLog> selectOperLogList(SysOperLog operLog);

    /**
     * Batch DELETE SystemOPERATION LOG
     *
     * @param operIds The OPERATION LOG ID to be DELETED
     * @return Result
     */
    public int deleteOperLogByIds(Long[] operIds);

    /**
     * QUERYOPERATION LOG DETAILEDLY
     *
     * @param operId OPERATIONID
     * @return OPERATION LOG Object
     */
    public SysOperLog selectOperLogById(Long operId);

    /**
     * CLEAR OPERATION LOG
     */
    public void cleanOperLog();
}
