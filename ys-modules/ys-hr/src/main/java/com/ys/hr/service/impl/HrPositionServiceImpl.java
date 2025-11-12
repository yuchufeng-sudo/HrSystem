package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.hr.domain.HrQuota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrPositionMapper;
import com.ys.hr.domain.HrPosition;
import com.ys.hr.service.IHrPositionService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Position Management Service Implementation
 *
 * @author ys
 * @date 2025-06-23
 */
@Service
public class HrPositionServiceImpl extends ServiceImpl<HrPositionMapper, HrPosition> implements IHrPositionService
{

    @Autowired
    private  HrQuotaServiceImpl hrQuotaService;
    /**
     * Query Position Management
     *
     * @param id Position Management primary key
     * @return Position Management
     */
    @Override
    public HrPosition selectHrPositionById(Long id)
    {
        return baseMapper.selectHrPositionById(id);
    }

    /**
     * Query Position Management list
     *
     * @param hrPosition Position Management
     * @return Position Management
     */
    @Override
    public List<HrPosition> selectHrPositionList(HrPosition hrPosition)
    {
        return baseMapper.selectHrPositionList(hrPosition);
    }

    /**
     * Add Position Management
     *
     * @param hrPosition Position Management
     * @return Result
     */
    @Override
    public int insertHrPosition(HrPosition hrPosition)
    {
        hrPosition.setCreateTime(DateUtils.getNowDate());
        int insert = baseMapper.insert(hrPosition);
//        if(insert>0){
//            HrQuota hrQuota = new HrQuota();
//            hrQuota.setEnterpriseId(hrPosition.getEnterpriseId());
//            hrQuota.setPostId(hrPosition.getId());
//            hrQuota.setQuotaNumber(0L);
//            hrQuotaService.insertHrQuota(hrQuota);
//        }
        return insert;
    }

    /**
     * Update Position Management
     *
     * @param hrPosition Position Management
     * @return Result
     */
    @Override
    public int updateHrPosition(HrPosition hrPosition)
    {
        hrPosition.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrPosition);
    }
}
