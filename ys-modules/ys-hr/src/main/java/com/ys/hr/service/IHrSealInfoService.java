package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrSealInfo;

import java.util.List;

/**
 *  seal Information  Service Interface
 *
 * @author ys
 * @date 2025-06-05
 */
public interface IHrSealInfoService extends IService<HrSealInfo>
{
    /**
     * Query seal Information
     *
     * @param id  seal Information  primary key
     * @return seal Information
     */
    public HrSealInfo selectHrSealInfoById(Long id);

    /**
     * Query seal Information list
     *
     * @param hrSealInfo  seal Information
     * @return seal Information  collection
     */
    public List<HrSealInfo> selectHrSealInfoList(HrSealInfo hrSealInfo);

    /**
     * Add seal Information
     *
     * @param hrSealInfo  seal Information
     * @return Result
     */
    public int insertHrSealInfo(HrSealInfo hrSealInfo);

    /**
     * Update seal Information
     *
     * @param hrSealInfo  seal Information
     * @return Result
     */
    public int updateHrSealInfo(HrSealInfo hrSealInfo);

    /**
     * Batch delete  seal Information
     *
     * @param ids  seal Information  primary keys to be deleted
     * @return Result
     */
    public int deleteHrSealInfoByIds(String[] ids);

    /**
     * Delete seal Information  information
     *
     * @param id  seal Information  primary key
     * @return Result
     */
    public int deleteHrSealInfoById(String id);
}
