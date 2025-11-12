package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrJobListings;

/**
 * Job Listings Mapper Interface
 *
 * @author ys
 * @date 2025-09-25
 */
public interface HrJobListingsMapper extends BaseMapper<HrJobListings>
{
    /**
     * Query Job Listings
     *
     * @param id Job Listings primary key
     * @return Job Listings
     */
    public HrJobListings selectHrJobListingsById(String id);

    /**
     * Query Job Listings list
     *
     * @param hrJobListings Job Listings
     * @return Job Listings collection
     */
    public List<HrJobListings> selectHrJobListingsList(HrJobListings hrJobListings);

}
