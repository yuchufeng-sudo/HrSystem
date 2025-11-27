package com.ys.hr.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrJobListings;

/**
 * Job Listings Service Interface
 *
 * @author ys
 * @date 2025-09-25
 */
public interface IHrJobListingsService extends IService<HrJobListings>
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

    /**
     * Add Job Listings
     *
     * @param hrJobListings Job Listings
     * @return Result
     */
    public int insertHrJobListings(HrJobListings hrJobListings);

    /**
     * Update Job Listings
     *
     * @param hrJobListings Job Listings
     * @return Result
     */
    public int updateHrJobListings(HrJobListings hrJobListings);
}
