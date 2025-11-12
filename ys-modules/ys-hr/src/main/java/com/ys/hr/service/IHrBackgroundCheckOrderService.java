package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrBackgroundCheckOrder;

import java.util.List;

/**
 * Background Check Order Service Interface
 *
 * @author ys
 * @date 2025-06-25
 */
public interface IHrBackgroundCheckOrderService extends IService<HrBackgroundCheckOrder>
{
    /**
     * Query Background Check Order
     *
     * @param id Background Check Order primary key
     * @return Background Check Order
     */
    public HrBackgroundCheckOrder selectHrBackgroundCheckOrderById(Long id);

    /**
     * Query Background Check Order list
     *
     * @param hrBackgroundCheckOrder Background Check Order
     * @return Background Check Order collection
     */
    public List<HrBackgroundCheckOrder> selectHrBackgroundCheckOrderList(HrBackgroundCheckOrder hrBackgroundCheckOrder);

    /**
     * Add Background Check Order
     *
     * @param hrBackgroundCheckOrder Background Check Order
     * @return Result
     */
    public int insertHrBackgroundCheckOrder(HrBackgroundCheckOrder hrBackgroundCheckOrder);

    /**
     * Update Background Check Order
     *
     * @param hrBackgroundCheckOrder Background Check Order
     * @return Result
     */
    public int updateHrBackgroundCheckOrder(HrBackgroundCheckOrder hrBackgroundCheckOrder);

    /**
     * Batch delete Background Check Order
     *
     * @param ids Background Check Order primary keys to be deleted
     * @return Result
     */
    public int deleteHrBackgroundCheckOrderByIds(String[] ids);

    /**
     * Delete Background Check Order information
     *
     * @param id Background Check Order primary key
     * @return Result
     */
    public int deleteHrBackgroundCheckOrderById(String id);

    int refreshStatus(HrBackgroundCheckOrder hrBackgroundCheckOrder);
}
