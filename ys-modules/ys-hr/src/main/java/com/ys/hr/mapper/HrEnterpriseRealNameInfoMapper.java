package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrEnterpriseRealNameInfo;

/**
 *  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  Mapper Interface
 *
 * @author ys
 * @date 2025-06-05
 */
public interface HrEnterpriseRealNameInfoMapper extends BaseMapper<HrEnterpriseRealNameInfo>
{
    /**
     * Query  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION 
     *
     * @param id  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  primary key
     * @return  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION 
     */
    public HrEnterpriseRealNameInfo selectHrEnterpriseRealNameInfoById(Long id);
    
    /**
     * Query  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  list
     *
     * @param hrEnterpriseRealNameInfo  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION 
     * @return  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  collection
     */
    public List<HrEnterpriseRealNameInfo> selectHrEnterpriseRealNameInfoList(HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo);

    HrEnterpriseRealNameInfo selectHrEnterpriseRealNameInfoByUserId(String userEnterpriseId);

    HrEnterpriseRealNameInfo selectBySerialNo(String serialNo);
}