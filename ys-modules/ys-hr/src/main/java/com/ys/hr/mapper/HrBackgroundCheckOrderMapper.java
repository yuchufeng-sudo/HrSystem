package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrBackgroundCheckOrder;

/**
 * Background Check Order Mapper Interface
 *
 * @author ys
 * @date 2025-06-25
 */
public interface HrBackgroundCheckOrderMapper extends BaseMapper<HrBackgroundCheckOrder>
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

}