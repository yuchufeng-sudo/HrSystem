package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrBackgroundCheckInfo;

/**
 * Background check personnel information Mapper Interface
 *
 * @author ys
 * @date 2025-06-25
 */
public interface HrBackgroundCheckInfoMapper extends BaseMapper<HrBackgroundCheckInfo>
{
    /**
     * Query Background check personnel information
     *
     * @param id Background check personnel information primary key
     * @return Background check personnel information
     */
    public HrBackgroundCheckInfo selectHrBackgroundCheckInfoById(Long id);
    
    /**
     * Query Background check personnel information list
     *
     * @param hrBackgroundCheckInfo Background check personnel information
     * @return Background check personnel information collection
     */
    public List<HrBackgroundCheckInfo> selectHrBackgroundCheckInfoList(HrBackgroundCheckInfo hrBackgroundCheckInfo);

}