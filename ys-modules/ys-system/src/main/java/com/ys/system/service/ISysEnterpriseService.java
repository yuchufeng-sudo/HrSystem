package com.ys.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.system.api.domain.SysEnterprise;

import java.util.List;

/**
 *  Enterprise management Service Interface
 *
 * @author ys
 * @date 2025-05-15
 */
public interface ISysEnterpriseService extends IService<SysEnterprise>
{

    /**
     * Query Enterprise management   list
     *
     * @param sysEnterprise  Enterprise management
     * @return  Enterprise management Set
     */
    public List<SysEnterprise> selectSysEnterpriseList(SysEnterprise sysEnterprise);

    public SysEnterprise selectSysEnterpriseByInviteCode(String code);

    /**
     * Add Enterprise management
     *
     * @param sysEnterprise  Enterprise management
     * @return Result
     */
    public int insertSysEnterprise(SysEnterprise sysEnterprise);

    /**
     * Update Enterprise management
     *
     * @param sysEnterprise  Enterprise management
     * @return Result
     */
    public int updateSysEnterprise(SysEnterprise sysEnterprise);

    List<SysEnterprise> selectEnterpriseList(SysEnterprise sysEnterprise);
    int updateSysEnterpriseByFirst(SysEnterprise sysEnterprise);

    SysEnterprise selectSysEnterpriseById(String id);

    boolean disablesEnterprise(String id);
    boolean resumeEnterprise(String id);
}
