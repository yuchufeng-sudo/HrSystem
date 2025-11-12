package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrBackgroundCheckInfo;

import java.util.List;

/**
 * Background check personnel information Service Interface
 *
 * @author ys
 * @date 2025-06-25
 */
public interface IHrBackgroundCheckInfoService extends IService<HrBackgroundCheckInfo>
{
    /**
     * Query Background check personnel information
     *
     * @param id Background check personnel information primary key
     * @return Background check personnel information
     */
    public HrBackgroundCheckInfo selectHrBackgroundCheckInfoById(Long id);

    /**
     * Query Background check personnel information list
     *
     * @param hrBackgroundCheckInfo Background check personnel information
     * @return Background check personnel information collection
     */
    public List<HrBackgroundCheckInfo> selectHrBackgroundCheckInfoList(HrBackgroundCheckInfo hrBackgroundCheckInfo);

    /**
     * Add Background check personnel information
     *
     * @param hrBackgroundCheckInfo Background check personnel information
     * @return Result
     */
    public int insertHrBackgroundCheckInfo(HrBackgroundCheckInfo hrBackgroundCheckInfo);

    /**
     * Update Background check personnel information
     *
     * @param hrBackgroundCheckInfo Background check personnel information
     * @return Result
     */
    public int updateHrBackgroundCheckInfo(HrBackgroundCheckInfo hrBackgroundCheckInfo);

    /**
     * Batch delete Background check personnel information
     *
     * @param ids Background check personnel information primary keys to be deleted
     * @return Result
     */
    public int deleteHrBackgroundCheckInfoByIds(String[] ids);

    /**
     * Delete Background check personnel information information
     *
     * @param id Background check personnel information primary key
     * @return Result
     */
    public int deleteHrBackgroundCheckInfoById(String id);

}
