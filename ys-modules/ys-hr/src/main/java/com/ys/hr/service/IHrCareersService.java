package com.ys.hr.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrCareers;

/**
 * Company careers information Service Interface
 *
 * @author ys
 * @date 2025-09-27
 */
public interface IHrCareersService extends IService<HrCareers>
{
    /**
     * Query Company careers information
     *
     * @param id Company careers information primary key
     * @return Company careers information
     */
    public HrCareers selectHrCareersById(String id);

    /**
     * Query Company careers information list
     *
     * @param hrCareers Company careers information
     * @return Company careers information collection
     */
    public List<HrCareers> selectHrCareersList(HrCareers hrCareers);

    /**
     * Add Company careers information
     *
     * @param hrCareers Company careers information
     * @return Result
     */
    public int insertHrCareers(HrCareers hrCareers);

    /**
     * Update Company careers information
     *
     * @param hrCareers Company careers information
     * @return Result
     */
    public int updateHrCareers(HrCareers hrCareers);

    HrCareers getOrCreateEnterpriseCareer(String enterpriseId);
}
