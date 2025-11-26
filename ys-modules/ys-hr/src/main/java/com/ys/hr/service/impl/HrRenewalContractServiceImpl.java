package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrRenewalContractMapper;
import com.ys.hr.domain.HrRenewalContract;
import com.ys.hr.service.IHrRenewalContractService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 *  renewal contract Service Implementation
 *
 * @author ys
 * @date 2025-06-20
 */
@Service
public class HrRenewalContractServiceImpl extends ServiceImpl<HrRenewalContractMapper, HrRenewalContract> implements IHrRenewalContractService
{

    /**
     * Query renewal contract
     *
     * @param id  renewal contract primary key
     * @return renewal contract
     */
    @Override
    public HrRenewalContract selectHrRenewalContractById(String id)
    {
        return baseMapper.selectHrRenewalContractById(id);
    }

    /**
     * Query renewal contract list
     *
     * @param hrRenewalContract  renewal contract
     * @return renewal contract
     */
    @Override
    public List<HrRenewalContract> selectHrRenewalContractList(HrRenewalContract hrRenewalContract)
    {
        return baseMapper.selectHrRenewalContractList(hrRenewalContract);
    }

    /**
     * Add renewal contract
     *
     * @param hrRenewalContract  renewal contract
     * @return Result
     */
    @Override
    public int insertHrRenewalContract(HrRenewalContract hrRenewalContract)
    {
        hrRenewalContract.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrRenewalContract);
    }

    /**
     * Update renewal contract
     *
     * @param hrRenewalContract  renewal contract
     * @return Result
     */
    @Override
    public int updateHrRenewalContract(HrRenewalContract hrRenewalContract)
    {
        hrRenewalContract.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrRenewalContract);
    }

    /**
     * Batch delete  renewal contract
     *
     * @param ids  renewal contract primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrRenewalContractByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete renewal contract information
     *
     * @param id  renewal contract primary key
     * @return Result
     */
    @Override
    public int deleteHrRenewalContractById(String id)
    {
        return baseMapper.deleteById(id);
    }
}
