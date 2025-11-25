package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEnterpriseRealNameInfo;

import java.util.List;

/**
 *  Enterprise Real-name authentication Information  Service Interface
 *
 * @author ys
 * @date 2025-06-05
 */
public interface IHrEnterpriseRealNameInfoService extends IService<HrEnterpriseRealNameInfo>
{
    /**
     * Query Enterprise Real-name authentication Information
     *
     * @param id  Enterprise Real-name authentication Information  primary key
     * @return  Enterprise Real-name authentication Information
     */
    public HrEnterpriseRealNameInfo selectHrEnterpriseRealNameInfoById(Long id);

    /**
     * Query Enterprise Real-name authentication Information list
     *
     * @param hrEnterpriseRealNameInfo  Enterprise Real-name authentication Information
     * @return  Enterprise Real-name authentication Information  collection
     */
    public List<HrEnterpriseRealNameInfo> selectHrEnterpriseRealNameInfoList(HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo);

    /**
     * Add Enterprise Real-name authentication Information
     *
     * @param hrEnterpriseRealNameInfo  Enterprise Real-name authentication Information
     * @return Result
     */
    public int insertHrEnterpriseRealNameInfo(HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo);

    /**
     * Update Enterprise Real-name authentication Information
     *
     * @param hrEnterpriseRealNameInfo  Enterprise Real-name authentication Information
     * @return Result
     */
    public int updateHrEnterpriseRealNameInfo(HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo);

    /**
     * Batch delete  Enterprise Real-name authentication Information
     *
     * @param ids  Enterprise Real-name authentication Information  primary keys to be deleted
     * @return Result
     */
    public int deleteHrEnterpriseRealNameInfoByIds(String[] ids);

    /**
     * Delete Enterprise Real-name authentication Information  information
     *
     * @param id  Enterprise Real-name authentication Information  primary key
     * @return Result
     */
    public int deleteHrEnterpriseRealNameInfoById(String id);

    /**
     * According to User idQUERY Information
     * @param userEnterpriseId
     * @return
     */
    HrEnterpriseRealNameInfo selectHrEnterpriseRealNameInfoByUserId(String userEnterpriseId);

    /**
     * According to  Serial NumberQUERY Information
     * @param serialNo
     * @return
     */
    HrEnterpriseRealNameInfo selectBySerialNo(String serialNo);
}
