package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrEnterpriseRealNameInfo;

/**
 *  Enterprise Real-name authentication Information  Mapper Interface
 *
 * @author ys
 * @date 2025-06-05
 */
public interface HrEnterpriseRealNameInfoMapper extends BaseMapper<HrEnterpriseRealNameInfo>
{
    /**
     * Query Enterprise Real-name authentication Information 
     *
     * @param id  Enterprise Real-name authentication Information  primary key
     * @return  Enterprise Real-name authentication Information 
     */
    public HrEnterpriseRealNameInfo selectHrEnterpriseRealNameInfoById(Long id);
    
    /**
     * Query Enterprise Real-name authentication Information list
     *
     * @param hrEnterpriseRealNameInfo  Enterprise Real-name authentication Information 
     * @return  Enterprise Real-name authentication Information  collection
     */
    public List<HrEnterpriseRealNameInfo> selectHrEnterpriseRealNameInfoList(HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo);

    HrEnterpriseRealNameInfo selectHrEnterpriseRealNameInfoByUserId(String userEnterpriseId);

    HrEnterpriseRealNameInfo selectBySerialNo(String serialNo);
}
