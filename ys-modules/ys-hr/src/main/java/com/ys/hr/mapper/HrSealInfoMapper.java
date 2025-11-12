package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrSealInfo;

import java.util.List;

/**
 *  SEAL INFORMATION  Mapper Interface
 *
 * @author ys
 * @date 2025-06-05
 */
public interface HrSealInfoMapper extends BaseMapper<HrSealInfo>
{
    /**
     * Query  SEAL INFORMATION
     *
     * @param id  SEAL INFORMATION  primary key
     * @return  SEAL INFORMATION
     */
    public HrSealInfo selectHrSealInfoById(Long id);

    /**
     * Query  SEAL INFORMATION  list
     *
     * @param hrSealInfo  SEAL INFORMATION
     * @return  SEAL INFORMATION  collection
     */
    public List<HrSealInfo> selectHrSealInfoList(HrSealInfo hrSealInfo);

}
