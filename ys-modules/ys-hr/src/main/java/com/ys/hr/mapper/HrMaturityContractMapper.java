package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrMaturityContract;

/**
 *  Contract expiration Mapper Interface
 *
 * @author ys
 * @date 2025-06-20
 */
public interface HrMaturityContractMapper extends BaseMapper<HrMaturityContract>
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

}
