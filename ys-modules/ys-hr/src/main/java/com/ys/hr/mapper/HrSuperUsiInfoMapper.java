package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrSuperUsiInfo;

/**
 * USI and product information table Mapper Interface
 *
 * @author ys
 * @date 2025-10-23
 */
public interface HrSuperUsiInfoMapper extends BaseMapper<HrSuperUsiInfo>
{

    /**
     * Query USI and product information table list
     *
     * @param hrSuperUsiInfo USI and product information table
     * @return USI and product information table collection
     */
    public List<HrSuperUsiInfo> selectHrSuperUsiInfoList(HrSuperUsiInfo hrSuperUsiInfo);

}
