package com.ys.system.service;

import com.ys.system.api.domain.SysLogininfor;

import java.util.List;

/**
 *  System Access LOG Status Information Service Layer
 *
 * @author ys
 */
public interface ISysLogininforService
{
    /**
     * Add System Login Log
     *
     * @param logininfor Access LOG Object
     */
    public int insertLogininfor(SysLogininfor logininfor);

    /**
     * Query System Login Log Set
     *
     * @param logininfor Access LOG Object
     * @return Login RecordSet
     */
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);

    public Integer selectLogininforCount(SysLogininfor logininfor);

    /**
     * Batch Delete System Login Log
     *
     * @param infoIds Login Log ID to be DELETED
     * @return Result
     */
    public int deleteLogininforByIds(Long[] infoIds);

    /**
     * Clear  System Login Log
     */
    public void cleanLogininfor();
}
