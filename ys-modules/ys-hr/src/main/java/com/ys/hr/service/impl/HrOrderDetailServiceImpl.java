package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrOrderDetailMapper;
import com.ys.hr.domain.HrOrderDetail;
import com.ys.hr.service.IHrOrderDetailService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Order Detail Service Implementation
 *
 * @author ys
 * @date 2025-06-18
 */
@Service
public class HrOrderDetailServiceImpl extends ServiceImpl<HrOrderDetailMapper, HrOrderDetail> implements IHrOrderDetailService
{

    /**
     * Query Order Detail
     *
     * @param orderId Order Detail primary key
     * @return Order Detail
     */
    @Override
    public HrOrderDetail selectHrOrderDetailByOrderId(String orderId)
    {
        return baseMapper.selectHrOrderDetailByOrderId(orderId);
    }

    /**
     * Query Order Detail list
     *
     * @param hrOrderDetail Order Detail
     * @return Order Detail
     */
    @Override
    public List<HrOrderDetail> selectHrOrderDetailList(HrOrderDetail hrOrderDetail)
    {
        return baseMapper.selectHrOrderDetailList(hrOrderDetail);
    }

    /**
     * Add Order Detail
     *
     * @param hrOrderDetail Order Detail
     * @return Result
     */
    @Override
    public int insertHrOrderDetail(HrOrderDetail hrOrderDetail)
    {
        hrOrderDetail.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrOrderDetail);
    }

    /**
     * Update Order Detail
     *
     * @param hrOrderDetail Order Detail
     * @return Result
     */
    @Override
    public int updateHrOrderDetail(HrOrderDetail hrOrderDetail)
    {
        return baseMapper.updateById(hrOrderDetail);
    }

    /**
     * Batch delete Order Detail
     *
     * @param orderIds Order Detail primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrOrderDetailByOrderIds(String[] orderIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(orderIds));
    }

    /**
     * Delete Order Detail information
     *
     * @param orderId Order Detail primary key
     * @return Result
     */
    @Override
    public int deleteHrOrderDetailByOrderId(String orderId)
    {
        return baseMapper.deleteById(orderId);
    }

    @Override
    public HrOrderDetail selectHrOrderDetailByPlanOrderId(String planOrderId) {
        return baseMapper.selectHrOrderDetailByPlanOrderId(planOrderId);
    }

}
