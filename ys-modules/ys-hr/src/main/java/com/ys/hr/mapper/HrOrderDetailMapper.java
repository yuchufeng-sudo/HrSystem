package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrOrderDetail;

import java.util.List;

/**
 * Order Detail Mapper Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface HrOrderDetailMapper extends BaseMapper<HrOrderDetail>
{
    /**
     * Query Order Detail
     *
     * @param orderId Order Detail primary key
     * @return Order Detail
     */
    public HrOrderDetail selectHrOrderDetailByOrderId(String orderId);

    /**
     * Query Order Detail list
     *
     * @param hrOrderDetail Order Detail
     * @return Order Detail collection
     */
    public List<HrOrderDetail> selectHrOrderDetailList(HrOrderDetail hrOrderDetail);

    HrOrderDetail selectHrOrderDetailByPlanOrderId(String planOrderId);
}
