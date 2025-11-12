package com.ys.hr.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrFundInformation;

/**
 * Comprehensive fund information Service Interface
 *
 * @author ys
 * @date 2025-10-17
 */
public interface IHrFundInformationService extends IService<HrFundInformation>
{
    /**
     * Query Comprehensive fund information
     *
     * @param id Comprehensive fund information primary key
     * @return Comprehensive fund information
     */
    public HrFundInformation selectHrFundInformationById(String id);

    /**
     * Query Comprehensive fund information list
     *
     * @param hrFundInformation Comprehensive fund information
     * @return Comprehensive fund information collection
     */
    public List<HrFundInformation> selectHrFundInformationList(HrFundInformation hrFundInformation);

    /**
     * Add Comprehensive fund information
     *
     * @param hrFundInformation Comprehensive fund information
     * @return Result
     */
    public int insertHrFundInformation(HrFundInformation hrFundInformation);

    /**
     * Update Comprehensive fund information
     *
     * @param hrFundInformation Comprehensive fund information
     * @return Result
     */
    public int updateHrFundInformation(HrFundInformation hrFundInformation);

    /**
     * Batch delete Comprehensive fund information
     *
     * @param ids Comprehensive fund information primary keys to be deleted
     * @return Result
     */
    public int deleteHrFundInformationByIds(String[] ids);

    /**
     * Delete Comprehensive fund information information
     *
     * @param id Comprehensive fund information primary key
     * @return Result
     */
    public int deleteHrFundInformationById(String id);
}