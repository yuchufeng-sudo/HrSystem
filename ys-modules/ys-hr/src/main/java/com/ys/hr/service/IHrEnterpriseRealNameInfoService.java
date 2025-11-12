package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEnterpriseRealNameInfo;

import java.util.List;

/**
 *  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  Service Interface
 *
 * @author ys
 * @date 2025-06-05
 */
public interface IHrEnterpriseRealNameInfoService extends IService<HrEnterpriseRealNameInfo>
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

    /**
     * Add  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION
     *
     * @param hrEnterpriseRealNameInfo  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION
     * @return Result
     */
    public int insertHrEnterpriseRealNameInfo(HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo);

    /**
     * Update  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION
     *
     * @param hrEnterpriseRealNameInfo  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION
     * @return Result
     */
    public int updateHrEnterpriseRealNameInfo(HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo);

    /**
     * Batch delete  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION
     *
     * @param ids  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  primary keys to be deleted
     * @return Result
     */
    public int deleteHrEnterpriseRealNameInfoByIds(String[] ids);

    /**
     * Delete  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  information
     *
     * @param id  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  primary key
     * @return Result
     */
    public int deleteHrEnterpriseRealNameInfoById(String id);

    /**
     * According to USER idQUERY INFORMATION
     * @param userEnterpriseId
     * @return
     */
    HrEnterpriseRealNameInfo selectHrEnterpriseRealNameInfoByUserId(String userEnterpriseId);

    /**
     * According to  Serial NumberQUERY INFORMATION
     * @param serialNo
     * @return
     */
    HrEnterpriseRealNameInfo selectBySerialNo(String serialNo);
}
