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
 *  THE CONTRACT EXPIRES  Service Implementation
 *
 * @author ys
 * @date 2025-06-20
 */
@Service
public class HrMaturityContractServiceImpl extends ServiceImpl<HrMaturityContractMapper, HrMaturityContract> implements IHrMaturityContractService
{

    /**
     * Query  THE CONTRACT EXPIRES 
     *
     * @param id  THE CONTRACT EXPIRES  primary key
     * @return  THE CONTRACT EXPIRES 
     */
    @Override
    public HrMaturityContract selectHrMaturityContractById(Long id)
    {
        return baseMapper.selectHrMaturityContractById(id);
    }

    /**
     * Query  THE CONTRACT EXPIRES  list
     *
     * @param hrMaturityContract  THE CONTRACT EXPIRES 
     * @return  THE CONTRACT EXPIRES 
     */
    @Override
    public List<HrMaturityContract> selectHrMaturityContractList(HrMaturityContract hrMaturityContract)
    {
        return baseMapper.selectHrMaturityContractList(hrMaturityContract);
    }

    /**
     * Add  THE CONTRACT EXPIRES 
     *
     * @param hrMaturityContract  THE CONTRACT EXPIRES 
     * @return Result
     */
    @Override
    public int insertHrMaturityContract(HrMaturityContract hrMaturityContract)
    {
        hrMaturityContract.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrMaturityContract);
    }

    /**
     * Update  THE CONTRACT EXPIRES 
     *
     * @param hrMaturityContract  THE CONTRACT EXPIRES 
     * @return Result
     */
    @Override
    public int updateHrMaturityContract(HrMaturityContract hrMaturityContract)
    {
        hrMaturityContract.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrMaturityContract);
    }

    /**
     * Batch delete  THE CONTRACT EXPIRES 
     *
     * @param ids  THE CONTRACT EXPIRES  primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrMaturityContractByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete  THE CONTRACT EXPIRES  information
     *
     * @param id  THE CONTRACT EXPIRES  primary key
     * @return Result
     */
    @Override
    public int deleteHrMaturityContractById(String id)
    {
        return baseMapper.deleteById(id);
    }
}
