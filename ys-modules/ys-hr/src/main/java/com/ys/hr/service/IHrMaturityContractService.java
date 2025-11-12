package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrMaturityContract;

import java.util.List;

/**
 *  THE CONTRACT EXPIRES  Service Interface
 *
 * @author ys
 * @date 2025-06-20
 */
public interface IHrMaturityContractService extends IService<HrMaturityContract>
{
    /**
     * Query  THE CONTRACT EXPIRES
     *
     * @param id  THE CONTRACT EXPIRES  primary key
     * @return  THE CONTRACT EXPIRES
     */
    public HrMaturityContract selectHrMaturityContractById(Long id);

    /**
     * Query  THE CONTRACT EXPIRES  list
     *
     * @param hrMaturityContract  THE CONTRACT EXPIRES
     * @return  THE CONTRACT EXPIRES  collection
     */
    public List<HrMaturityContract> selectHrMaturityContractList(HrMaturityContract hrMaturityContract);

    /**
     * Add  THE CONTRACT EXPIRES
     *
     * @param hrMaturityContract  THE CONTRACT EXPIRES
     * @return Result
     */
    public int insertHrMaturityContract(HrMaturityContract hrMaturityContract);

    /**
     * Update  THE CONTRACT EXPIRES
     *
     * @param hrMaturityContract  THE CONTRACT EXPIRES
     * @return Result
     */
    public int updateHrMaturityContract(HrMaturityContract hrMaturityContract);

    /**
     * Batch delete  THE CONTRACT EXPIRES
     *
     * @param ids  THE CONTRACT EXPIRES  primary keys to be deleted
     * @return Result
     */
    public int deleteHrMaturityContractByIds(String[] ids);

    /**
     * Delete  THE CONTRACT EXPIRES  information
     *
     * @param id  THE CONTRACT EXPIRES  primary key
     * @return Result
     */
    public int deleteHrMaturityContractById(String id);
}
