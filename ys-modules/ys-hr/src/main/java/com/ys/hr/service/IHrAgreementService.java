package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrAgreement;

import java.util.List;

/**
 * agreement Service Interface
 *
 * @author ys
 * @date 2025-07-01
 */
public interface IHrAgreementService extends IService<HrAgreement>
{
    /**
     * Query agreement
     *
     * @param id agreement primary key
     * @return agreement
     */
    public HrAgreement selectHrAgreementById(Long id);

    /**
     * Query agreement list
     *
     * @param hrAgreement agreement
     * @return agreement collection
     */
    public List<HrAgreement> selectHrAgreementList(HrAgreement hrAgreement);

    /**
     * Add agreement
     *
     * @param hrAgreement agreement
     * @return Result
     */
    public int insertHrAgreement(HrAgreement hrAgreement);

    /**
     * Update agreement
     *
     * @param hrAgreement agreement
     * @return Result
     */
    public int updateHrAgreement(HrAgreement hrAgreement);

    /**
     * Batch delete agreement
     *
     * @param ids agreement primary keys to be deleted
     * @return Result
     */
    public int deleteHrAgreementByIds(Long[] ids);

    /**
     * Delete agreement information
     *
     * @param id agreement primary key
     * @return Result
     */
    public int deleteHrAgreementById(Long id);
}
