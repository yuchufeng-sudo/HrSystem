package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrOrderDetail;

import java.util.List;

/**
 * Order Detail Service Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface IHrOrderDetailService extends IService<HrOrderDetail>
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

    /**
     * Add Order Detail
     *
     * @param hrOrderDetail Order Detail
     * @return Result
     */
    public int insertHrOrderDetail(HrOrderDetail hrOrderDetail);

    /**
     * Update Order Detail
     *
     * @param hrOrderDetail Order Detail
     * @return Result
     */
    public int updateHrOrderDetail(HrOrderDetail hrOrderDetail);

    /**
     * Batch delete Order Detail
     *
     * @param orderIds Order Detail primary keys to be deleted
     * @return Result
     */
    public int deleteHrOrderDetailByOrderIds(String[] orderIds);

    /**
     * Delete Order Detail information
     *
     * @param orderId Order Detail primary key
     * @return Result
     */
    public int deleteHrOrderDetailByOrderId(String orderId);

    HrOrderDetail selectHrOrderDetailByPlanOrderId(String planOrderId);
}
