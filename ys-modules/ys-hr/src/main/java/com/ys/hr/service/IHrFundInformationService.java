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
     */
    public void insertHrFundInformation(HrFundInformation hrFundInformation);

    /**
     * Update Comprehensive fund information
     *
     * @param hrFundInformation Comprehensive fund information
     * @return Result
     */
    public int updateHrFundInformation(HrFundInformation hrFundInformation);
}
