package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrRenewalContract;

import java.util.List;

/**
 *  renewal contract Service Interface
 *
 * @author ys
 * @date 2025-06-20
 */
public interface IHrRenewalContractService extends IService<HrRenewalContract>
{
    /**
     * Query renewal contract
     *
     * @param id  renewal contract primary key
     * @return  renewal contract
     */
    public HrRenewalContract selectHrRenewalContractById(String id);

    /**
     * Query renewal contract list
     *
     * @param hrRenewalContract  renewal contract
     * @return  renewal contract collection
     */
    public List<HrRenewalContract> selectHrRenewalContractList(HrRenewalContract hrRenewalContract);

    /**
     * Add renewal contract
     *
     * @param hrRenewalContract  renewal contract
     * @return Result
     */
    public int insertHrRenewalContract(HrRenewalContract hrRenewalContract);

    /**
     * Update renewal contract
     *
     * @param hrRenewalContract  renewal contract
     * @return Result
     */
    public int updateHrRenewalContract(HrRenewalContract hrRenewalContract);

    /**
     * Batch delete  renewal contract
     *
     * @param ids  renewal contract primary keys to be deleted
     * @return Result
     */
    public int deleteHrRenewalContractByIds(String[] ids);

    /**
     * Delete renewal contract information
     *
     * @param id  renewal contract primary key
     * @return Result
     */
    public int deleteHrRenewalContractById(String id);
}
