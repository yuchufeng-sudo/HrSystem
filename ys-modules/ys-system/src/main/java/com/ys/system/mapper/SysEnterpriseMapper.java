package com.ys.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.system.api.domain.SysEnterprise;

import java.util.List;

/**
 *  Enterprise management Mapper Interface
 *
 * @author ys
 * @date 2025-05-15
 */
public interface SysEnterpriseMapper extends BaseMapper<SysEnterprise>
{
    /**
     * Query Enterprise management   list
     *
     * @param sysEnterprise  Enterprise management
     * @return Enterprise management Set
     */
    public List<SysEnterprise> selectSysEnterpriseList(SysEnterprise sysEnterprise);

    List<SysEnterprise> selectEnterpriseList(SysEnterprise sysEnterprise);

    SysEnterprise selectSysEnterpriseById(String id);

    SysEnterprise selectSysEnterpriseByInviteCode(String code);

    void updataByApplicationId(SysEnterprise sysEnterprise);
}
