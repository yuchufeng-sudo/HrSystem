package com.ys.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.system.api.domain.SysEnterprise;

import java.util.List;

/**
 *  ENTERPRISE MANAGEMENT Service Interface
 *
 * @author ys
 * @date 2025-05-15
 */
public interface ISysEnterpriseService extends IService<SysEnterprise>
{

    /**
     * QUERY ENTERPRISE MANAGEMENT   LIST
     *
     * @param sysEnterprise  ENTERPRISE MANAGEMENT
     * @return  ENTERPRISE MANAGEMENT Set
     */
    public List<SysEnterprise> selectSysEnterpriseList(SysEnterprise sysEnterprise);

    public SysEnterprise selectSysEnterpriseByInviteCode(String code);

    /**
     * ADD ENTERPRISE MANAGEMENT
     *
     * @param sysEnterprise  ENTERPRISE MANAGEMENT
     * @return Result
     */
    public int insertSysEnterprise(SysEnterprise sysEnterprise);

    /**
     * MODIFY ENTERPRISE MANAGEMENT
     *
     * @param sysEnterprise  ENTERPRISE MANAGEMENT
     * @return Result
     */
    public int updateSysEnterprise(SysEnterprise sysEnterprise);

    List<SysEnterprise> selectEnterpriseList(SysEnterprise sysEnterprise);
    int updateSysEnterpriseByFirst(SysEnterprise sysEnterprise);

    SysEnterprise selectSysEnterpriseById(String id);

    boolean disablesEnterprise(String id);
    boolean resumeEnterprise(String id);
}
