package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrCertification;

import java.util.List;

/**
 * Certification information Service Interface
 *
 * @author ys
 * @date 2025-06-24
 */
public interface IHrCertificationService extends IService<HrCertification>
{
    /**
     * Query Certification information
     *
     * @param id Certification information primary key
     * @return Certification information
     */
    public HrCertification selectHrCertificationById(Long id);

    /**
     * Query Certification information list
     *
     * @param hrCertification Certification information
     * @return Certification information collection
     */
    public List<HrCertification> selectHrCertificationList(HrCertification hrCertification);

    /**
     * Add Certification information
     *
     * @param hrCertification Certification information
     * @return Result
     */
    public int insertHrCertification(HrCertification hrCertification);

    /**
     * Update Certification information
     *
     * @param hrCertification Certification information
     * @return Result
     */
    public int updateHrCertification(HrCertification hrCertification);

    /**
     * Batch delete Certification information
     *
     * @param ids Certification information primary keys to be deleted
     * @return Result
     */
    public int deleteHrCertificationByIds(Long[] ids);

    /**
     * Delete Certification information information
     *
     * @param id Certification information primary key
     * @return Result
     */
    public int deleteHrCertificationById(Long id);
}
