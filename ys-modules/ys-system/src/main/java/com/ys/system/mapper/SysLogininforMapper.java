package com.ys.system.mapper;

import com.ys.system.api.domain.SysLogininfor;

import java.util.List;

/**
 *  System access log information data layer
 *
 * @author ys
 */
public interface SysLogininforMapper
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

    Integer selectLogininforCount(SysLogininfor logininfor);

    /**
     * Batch Delete System Login Log
     *
     * @param infoIds Login Log ID to be DELETED
     * @return Result
     */
    public int deleteLogininforByIds(Long[] infoIds);

    /**
     * Clear  System Login Log
     *
     * @return Result
     */
    public int cleanLogininfor();
}
