package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.DateUtils;
import com.ys.hr.domain.HrEnterpriseRealNameInfo;
import com.ys.hr.mapper.HrEnterpriseRealNameInfoMapper;
import com.ys.hr.service.IHrEnterpriseRealNameInfoService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 *  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  Service Implementation
 *
 * @author ys
 * @date 2025-06-05
 */
@Service
public class HrEnterpriseRealNameInfoServiceImpl extends ServiceImpl<HrEnterpriseRealNameInfoMapper, HrEnterpriseRealNameInfo> implements IHrEnterpriseRealNameInfoService
{

    /**
     * Query  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION
     *
     * @param id  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  primary key
     * @return  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION
     */
    @Override
    public HrEnterpriseRealNameInfo selectHrEnterpriseRealNameInfoById(Long id)
    {
        return baseMapper.selectHrEnterpriseRealNameInfoById(id);
    }

    /**
     * Query  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  list
     *
     * @param hrEnterpriseRealNameInfo  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION
     * @return  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION
     */
    @Override
    public List<HrEnterpriseRealNameInfo> selectHrEnterpriseRealNameInfoList(HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo)
    {
        return baseMapper.selectHrEnterpriseRealNameInfoList(hrEnterpriseRealNameInfo);
    }

    /**
     * Add  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION
     *
     * @param hrEnterpriseRealNameInfo  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION
     * @return Result
     */
    @Override
    public int insertHrEnterpriseRealNameInfo(HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo)
    {
        hrEnterpriseRealNameInfo.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrEnterpriseRealNameInfo);
    }

    /**
     * Update  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION
     *
     * @param hrEnterpriseRealNameInfo  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION
     * @return Result
     */
    @Override
    public int updateHrEnterpriseRealNameInfo(HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo)
    {
        hrEnterpriseRealNameInfo.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrEnterpriseRealNameInfo);
    }

    /**
     * Batch delete  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION
     *
     * @param ids  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrEnterpriseRealNameInfoByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  information
     *
     * @param id  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  primary key
     * @return Result
     */
    @Override
    public int deleteHrEnterpriseRealNameInfoById(String id)
    {
        return baseMapper.deleteById(id);
    }

    /**
     * According to USER idQUERY INFORMATION
     * @param userEnterpriseId
     * @return
     */
    @Override
    public HrEnterpriseRealNameInfo selectHrEnterpriseRealNameInfoByUserId(String userEnterpriseId) {
        return baseMapper.selectHrEnterpriseRealNameInfoByUserId(userEnterpriseId);
    }

    /**
     * According to  Serial NumberQUERY INFORMATION
     * @param serialNo
     * @return
     */
    @Override
    public HrEnterpriseRealNameInfo selectBySerialNo(String serialNo) {
        return baseMapper.selectBySerialNo(serialNo);
    }
}
