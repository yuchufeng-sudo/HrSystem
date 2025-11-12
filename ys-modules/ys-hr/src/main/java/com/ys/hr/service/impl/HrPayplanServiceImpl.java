package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrPayplanMapper;
import com.ys.hr.domain.HrPayplan;
import com.ys.hr.service.IHrPayplanService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Pay Plan Service Implementation
 *
 * @author ys
 * @date 2025-06-17
 */
@Service
public class HrPayplanServiceImpl extends ServiceImpl<HrPayplanMapper, HrPayplan> implements IHrPayplanService
{

    /**
     * Query Pay Plan
     *
     * @param payId Pay Plan primary key
     * @return Pay Plan
     */
    @Override
    public HrPayplan selectHrPayplanByPayId(String payId)
    {
        return baseMapper.selectHrPayplanByPayId(payId);
    }

    /**
     * Query Pay Plan list
     *
     * @param hrPayplan Pay Plan
     * @return Pay Plan
     */
    @Override
    public List<HrPayplan> selectHrPayplanList(HrPayplan hrPayplan)
    {
        return baseMapper.selectHrPayplanList(hrPayplan);
    }

    /**
     * Add Pay Plan
     *
     * @param hrPayplan Pay Plan
     * @return Result
     */
    @Override
    public int insertHrPayplan(HrPayplan hrPayplan)
    {
        double monthPrice = hrPayplan.getPayMonthPrice();
        double yearPrice = hrPayplan.getPayYearPrice();
        if (monthPrice*12 > yearPrice){
            double discountPercent = (100 - (yearPrice / (monthPrice * 12)) * 100);
            hrPayplan.setDiscountPercent((long) (Math.round(discountPercent * 100.0) / 100.0));
        }
        return baseMapper.insert(hrPayplan);
    }

    /**
     * Update Pay Plan
     *
     * @param hrPayplan Pay Plan
     * @return Result
     */
    @Override
    public int updateHrPayplan(HrPayplan hrPayplan)
    {
        return baseMapper.updateById(hrPayplan);
    }

    /**
     * Batch delete Pay Plan
     *
     * @param payIds Pay Plan primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrPayplanByPayIds(String[] payIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(payIds));
    }

    /**
     * Delete Pay Plan information
     *
     * @param payId Pay Plan primary key
     * @return Result
     */
    @Override
    public int deleteHrPayplanByPayId(String payId)
    {
        return baseMapper.deleteById(payId);
    }
}
