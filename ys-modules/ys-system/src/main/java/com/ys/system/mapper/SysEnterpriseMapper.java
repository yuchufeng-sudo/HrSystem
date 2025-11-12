package com.ys.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.system.api.domain.SysEnterprise;

import java.util.List;

/**
 *  ENTERPRISE MANAGEMENT Mapper Interface
 *
 * @author ys
 * @date 2025-05-15
 */
public interface SysEnterpriseMapper extends BaseMapper<SysEnterprise>
{
    /**
     * QUERY ENTERPRISE MANAGEMENT   LIST
     *
     * @param sysEnterprise  ENTERPRISE MANAGEMENT
     * @return  ENTERPRISE MANAGEMENT Set
     */
    public List<SysEnterprise> selectSysEnterpriseList(SysEnterprise sysEnterprise);

    List<SysEnterprise> selectEnterpriseList(SysEnterprise sysEnterprise);

    SysEnterprise selectSysEnterpriseById(String id);

    SysEnterprise selectSysEnterpriseByInviteCode(String code);

    void updataByApplicationId(SysEnterprise sysEnterprise);
}
