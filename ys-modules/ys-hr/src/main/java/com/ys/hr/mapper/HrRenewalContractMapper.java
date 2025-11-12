package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrRenewalContract;

import java.util.List;

/**
 *  RENEWAL CONTRACT  Mapper Interface
 *
 * @author ys
 * @date 2025-06-20
 */
public interface HrRenewalContractMapper extends BaseMapper<HrRenewalContract>
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

}
