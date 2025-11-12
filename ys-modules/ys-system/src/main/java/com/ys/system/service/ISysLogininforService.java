package com.ys.system.service;

import com.ys.system.api.domain.SysLogininfor;

import java.util.List;

/**
 *  System Access LOG Status INFORMATION Service Layer
 *
 * @author ruoyi
 */
public interface ISysLogininforService
{
    /**
     * ADD System LOGIN LOG
     *
     * @param logininfor Access LOG Object
     */
    public int insertLogininfor(SysLogininfor logininfor);

    /**
     * QUERY System LOGIN LOG Set
     *
     * @param logininfor Access LOG Object
     * @return Login RecordSet
     */
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);

    public Integer selectLogininforCount(SysLogininfor logininfor);

    /**
     * Batch DELETE System LOGIN LOG
     *
     * @param infoIds LOGIN LOG ID to be DELETED
     * @return Result
     */
    public int deleteLogininforByIds(Long[] infoIds);

    /**
     * CLEAR  System LOGIN LOG
     */
    public void cleanLogininfor();
}
