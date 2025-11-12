package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrSealInfo;

import java.util.List;

/**
 *  SEAL INFORMATION  Service Interface
 *
 * @author ys
 * @date 2025-06-05
 */
public interface IHrSealInfoService extends IService<HrSealInfo>
{
    /**
     * Query  SEAL INFORMATION
     *
     * @param id  SEAL INFORMATION  primary key
     * @return  SEAL INFORMATION
     */
    public HrSealInfo selectHrSealInfoById(Long id);

    /**
     * Query  SEAL INFORMATION  list
     *
     * @param hrSealInfo  SEAL INFORMATION
     * @return  SEAL INFORMATION  collection
     */
    public List<HrSealInfo> selectHrSealInfoList(HrSealInfo hrSealInfo);

    /**
     * Add  SEAL INFORMATION
     *
     * @param hrSealInfo  SEAL INFORMATION
     * @return Result
     */
    public int insertHrSealInfo(HrSealInfo hrSealInfo);

    /**
     * Update  SEAL INFORMATION
     *
     * @param hrSealInfo  SEAL INFORMATION
     * @return Result
     */
    public int updateHrSealInfo(HrSealInfo hrSealInfo);

    /**
     * Batch delete  SEAL INFORMATION
     *
     * @param ids  SEAL INFORMATION  primary keys to be deleted
     * @return Result
     */
    public int deleteHrSealInfoByIds(String[] ids);

    /**
     * Delete  SEAL INFORMATION  information
     *
     * @param id  SEAL INFORMATION  primary key
     * @return Result
     */
    public int deleteHrSealInfoById(String id);
}
