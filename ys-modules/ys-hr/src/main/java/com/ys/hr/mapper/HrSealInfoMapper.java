package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrSealInfo;

import java.util.List;

/**
 *  seal Information  Mapper Interface
 *
 * @author ys
 * @date 2025-06-05
 */
public interface HrSealInfoMapper extends BaseMapper<HrSealInfo>
{
    /**
     * Query seal Information
     *
     * @param id  seal Information  primary key
     * @return seal Information
     */
    public HrSealInfo selectHrSealInfoById(Long id);

    /**
     * Query seal Information list
     *
     * @param hrSealInfo  seal Information
     * @return seal Information  collection
     */
    public List<HrSealInfo> selectHrSealInfoList(HrSealInfo hrSealInfo);

}
