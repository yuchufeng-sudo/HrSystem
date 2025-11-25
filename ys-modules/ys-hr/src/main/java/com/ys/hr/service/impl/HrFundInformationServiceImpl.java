package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrFundInformationMapper;
import com.ys.hr.domain.HrFundInformation;
import com.ys.hr.service.IHrFundInformationService;
import com.ys.common.core.utils.DateUtils;

/**
 * Comprehensive fund information Service Implementation
 *
 * @author ys
 * @date 2025-10-17
 */
@Service
public class HrFundInformationServiceImpl extends ServiceImpl<HrFundInformationMapper, HrFundInformation> implements IHrFundInformationService
{

    /**
     * Query Comprehensive fund information list
     *
     * @param hrFundInformation Comprehensive fund information
     * @return Comprehensive fund information
     */
    @Override
    public List<HrFundInformation> selectHrFundInformationList(HrFundInformation hrFundInformation)
    {
        return baseMapper.selectHrFundInformationList(hrFundInformation);
    }

    /**
     * Add Comprehensive fund information
     *
     * @param hrFundInformation Comprehensive fund information
     */
    @Override
    public void insertHrFundInformation(HrFundInformation hrFundInformation)
    {
        hrFundInformation.setCreateTime(DateUtils.getNowDate());
        baseMapper.insert(hrFundInformation);
    }

    /**
     * Update Comprehensive fund information
     *
     * @param hrFundInformation1 Comprehensive fund information
     * @return Result
     */
    @Override
    public int updateHrFundInformation(HrFundInformation hrFundInformation)
    {
        String fundType = hrFundInformation.getFundType();
        if ("1".equals(fundType)) {
            hrFundInformation.setEsa("");
            hrFundInformation.setSmsfName("");
            hrFundInformation.setSmsfAbn("");
            hrFundInformation.setSmsfAccountName("");
            hrFundInformation.setSmsfMemberNumber("");
            hrFundInformation.setFundContactEmail("");
            hrFundInformation.setBankAccountBsb("");
            hrFundInformation.setBankAccountName("");
            hrFundInformation.setBankAccountNumber("");
        }else if ("2".equals(fundType)) {
            hrFundInformation.setFundId("");
            hrFundInformation.setApraAccountName("");
            hrFundInformation.setMemberAccountNumber("");
            hrFundInformation.setFundId("");
        }
        hrFundInformation.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrFundInformation);
    }
}
