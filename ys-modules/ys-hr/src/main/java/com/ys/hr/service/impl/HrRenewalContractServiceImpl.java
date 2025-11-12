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
 *  RENEWAL CONTRACT  Service Implementation
 *
 * @author ys
 * @date 2025-06-20
 */
@Service
public class HrRenewalContractServiceImpl extends ServiceImpl<HrRenewalContractMapper, HrRenewalContract> implements IHrRenewalContractService
{

    /**
     * Query  RENEWAL CONTRACT 
     *
     * @param id  RENEWAL CONTRACT  primary key
     * @return  RENEWAL CONTRACT 
     */
    @Override
    public HrRenewalContract selectHrRenewalContractById(String id)
    {
        return baseMapper.selectHrRenewalContractById(id);
    }

    /**
     * Query  RENEWAL CONTRACT  list
     *
     * @param hrRenewalContract  RENEWAL CONTRACT 
     * @return  RENEWAL CONTRACT 
     */
    @Override
    public List<HrRenewalContract> selectHrRenewalContractList(HrRenewalContract hrRenewalContract)
    {
        return baseMapper.selectHrRenewalContractList(hrRenewalContract);
    }

    /**
     * Add  RENEWAL CONTRACT 
     *
     * @param hrRenewalContract  RENEWAL CONTRACT 
     * @return Result
     */
    @Override
    public int insertHrRenewalContract(HrRenewalContract hrRenewalContract)
    {
        hrRenewalContract.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrRenewalContract);
    }

    /**
     * Update  RENEWAL CONTRACT 
     *
     * @param hrRenewalContract  RENEWAL CONTRACT 
     * @return Result
     */
    @Override
    public int updateHrRenewalContract(HrRenewalContract hrRenewalContract)
    {
        hrRenewalContract.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrRenewalContract);
    }

    /**
     * Batch delete  RENEWAL CONTRACT 
     *
     * @param ids  RENEWAL CONTRACT  primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrRenewalContractByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete  RENEWAL CONTRACT  information
     *
     * @param id  RENEWAL CONTRACT  primary key
     * @return Result
     */
    @Override
    public int deleteHrRenewalContractById(String id)
    {
        return baseMapper.deleteById(id);
    }
}
