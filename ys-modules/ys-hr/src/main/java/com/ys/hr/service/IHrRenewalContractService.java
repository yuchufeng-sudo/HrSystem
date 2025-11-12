package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrRenewalContract;

import java.util.List;

/**
 *  RENEWAL CONTRACT  Service Interface
 *
 * @author ys
 * @date 2025-06-20
 */
public interface IHrRenewalContractService extends IService<HrRenewalContract>
{
    /**
     * Query  RENEWAL CONTRACT
     *
     * @param id  RENEWAL CONTRACT  primary key
     * @return  RENEWAL CONTRACT
     */
    public HrRenewalContract selectHrRenewalContractById(String id);

    /**
     * Query  RENEWAL CONTRACT  list
     *
     * @param hrRenewalContract  RENEWAL CONTRACT
     * @return  RENEWAL CONTRACT  collection
     */
    public List<HrRenewalContract> selectHrRenewalContractList(HrRenewalContract hrRenewalContract);

    /**
     * Add  RENEWAL CONTRACT
     *
     * @param hrRenewalContract  RENEWAL CONTRACT
     * @return Result
     */
    public int insertHrRenewalContract(HrRenewalContract hrRenewalContract);

    /**
     * Update  RENEWAL CONTRACT
     *
     * @param hrRenewalContract  RENEWAL CONTRACT
     * @return Result
     */
    public int updateHrRenewalContract(HrRenewalContract hrRenewalContract);

    /**
     * Batch delete  RENEWAL CONTRACT
     *
     * @param ids  RENEWAL CONTRACT  primary keys to be deleted
     * @return Result
     */
    public int deleteHrRenewalContractByIds(String[] ids);

    /**
     * Delete  RENEWAL CONTRACT  information
     *
     * @param id  RENEWAL CONTRACT  primary key
     * @return Result
     */
    public int deleteHrRenewalContractById(String id);
}
