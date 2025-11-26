package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrMaturityContractMapper;
import com.ys.hr.domain.HrMaturityContract;
import com.ys.hr.service.IHrMaturityContractService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 *  Contract expiration Service Implementation
 *
 * @author ys
 * @date 2025-06-20
 */
@Service
public class HrMaturityContractServiceImpl extends ServiceImpl<HrMaturityContractMapper, HrMaturityContract> implements IHrMaturityContractService
{

    /**
     * Query Contract expiration
     *
     * @param id  Contract expiration primary key
     * @return Contract expiration
     */
    @Override
    public HrMaturityContract selectHrMaturityContractById(Long id)
    {
        return baseMapper.selectHrMaturityContractById(id);
    }

    /**
     * Query Contract expiration list
     *
     * @param hrMaturityContract  Contract expiration
     * @return Contract expiration
     */
    @Override
    public List<HrMaturityContract> selectHrMaturityContractList(HrMaturityContract hrMaturityContract)
    {
        return baseMapper.selectHrMaturityContractList(hrMaturityContract);
    }

    /**
     * Add Contract expiration
     *
     * @param hrMaturityContract  Contract expiration
     * @return Result
     */
    @Override
    public int insertHrMaturityContract(HrMaturityContract hrMaturityContract)
    {
        hrMaturityContract.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrMaturityContract);
    }

    /**
     * Update Contract expiration
     *
     * @param hrMaturityContract  Contract expiration
     * @return Result
     */
    @Override
    public int updateHrMaturityContract(HrMaturityContract hrMaturityContract)
    {
        hrMaturityContract.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrMaturityContract);
    }

    /**
     * Batch delete  Contract expiration
     *
     * @param ids  Contract expiration primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrMaturityContractByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Contract expiration information
     *
     * @param id  Contract expiration primary key
     * @return Result
     */
    @Override
    public int deleteHrMaturityContractById(String id)
    {
        return baseMapper.deleteById(id);
    }
}
