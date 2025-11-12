package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrMaturityContract;

/**
 *  THE CONTRACT EXPIRES  Mapper Interface
 *
 * @author ys
 * @date 2025-06-20
 */
public interface HrMaturityContractMapper extends BaseMapper<HrMaturityContract>
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

}