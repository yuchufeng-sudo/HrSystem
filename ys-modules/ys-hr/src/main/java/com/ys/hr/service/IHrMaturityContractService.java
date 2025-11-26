package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrMaturityContract;

import java.util.List;

/**
 *  Contract expiration Service Interface
 *
 * @author ys
 * @date 2025-06-20
 */
public interface IHrMaturityContractService extends IService<HrMaturityContract>
{
    /**
     * Query Contract expiration
     *
     * @param id  Contract expiration primary key
     * @return Contract expiration
     */
    public HrMaturityContract selectHrMaturityContractById(Long id);

    /**
     * Query Contract expiration list
     *
     * @param hrMaturityContract  Contract expiration
     * @return Contract expiration collection
     */
    public List<HrMaturityContract> selectHrMaturityContractList(HrMaturityContract hrMaturityContract);

    /**
     * Add Contract expiration
     *
     * @param hrMaturityContract  Contract expiration
     * @return Result
     */
    public int insertHrMaturityContract(HrMaturityContract hrMaturityContract);

    /**
     * Update Contract expiration
     *
     * @param hrMaturityContract  Contract expiration
     * @return Result
     */
    public int updateHrMaturityContract(HrMaturityContract hrMaturityContract);

    /**
     * Batch delete  Contract expiration
     *
     * @param ids  Contract expiration primary keys to be deleted
     * @return Result
     */
    public int deleteHrMaturityContractByIds(String[] ids);

    /**
     * Delete Contract expiration information
     *
     * @param id  Contract expiration primary key
     * @return Result
     */
    public int deleteHrMaturityContractById(String id);
}
