package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrRenewalContract;

import java.util.List;

/**
 *  renewal contract Mapper Interface
 *
 * @author ys
 * @date 2025-06-20
 */
public interface HrRenewalContractMapper extends BaseMapper<HrRenewalContract>
{
    /**
     * Query renewal contract
     *
     * @param id  renewal contract primary key
     * @return renewal contract
     */
    public HrRenewalContract selectHrRenewalContractById(String id);

    /**
     * Query renewal contract list
     *
     * @param hrRenewalContract  renewal contract
     * @return renewal contract collection
     */
    public List<HrRenewalContract> selectHrRenewalContractList(HrRenewalContract hrRenewalContract);

}
