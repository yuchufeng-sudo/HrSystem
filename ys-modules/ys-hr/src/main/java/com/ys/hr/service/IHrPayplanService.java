package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrPayplan;

import java.util.List;

/**
 * Pay Plan Service Interface
 *
 * @author ys
 * @date 2025-06-17
 */
public interface IHrPayplanService extends IService<HrPayplan>
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

    /**
     * Add Pay Plan
     *
     * @param hrPayplan Pay Plan
     * @return Result
     */
    public int insertHrPayplan(HrPayplan hrPayplan);

    /**
     * Update Pay Plan
     *
     * @param hrPayplan Pay Plan
     * @return Result
     */
    public int updateHrPayplan(HrPayplan hrPayplan);

    /**
     * Batch delete Pay Plan
     *
     * @param payIds Pay Plan primary keys to be deleted
     * @return Result
     */
    public int deleteHrPayplanByPayIds(String[] payIds);

    /**
     * Delete Pay Plan information
     *
     * @param payId Pay Plan primary key
     * @return Result
     */
    public int deleteHrPayplanByPayId(String payId);
}
