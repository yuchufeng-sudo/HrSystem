package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrPayplan;

import java.util.List;

/**
 * Pay Plan Mapper Interface
 *
 * @author ys
 * @date 2025-06-17
 */
public interface HrPayplanMapper extends BaseMapper<HrPayplan>
{
    /**
     * Query Pay Plan
     *
     * @param payId Pay Plan primary key
     * @return Pay Plan
     */
    public HrPayplan selectHrPayplanByPayId(String payId);

    /**
     * Query Pay Plan list
     *
     * @param hrPayplan Pay Plan
     * @return Pay Plan collection
     */
    public List<HrPayplan> selectHrPayplanList(HrPayplan hrPayplan);

}
