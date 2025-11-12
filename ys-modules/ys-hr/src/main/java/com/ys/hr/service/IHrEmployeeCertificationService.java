package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmployeeCertification;

import java.util.List;

/**
 * Employee certification issuance Service Interface
 *
 * @author ys
 * @date 2025-06-24
 */
public interface IHrEmployeeCertificationService extends IService<HrEmployeeCertification>
{
    /**
     * Query Employee certification issuance
     *
     * @param id Employee certification issuance primary key
     * @return Employee certification issuance
     */
    public HrEmployeeCertification selectHrEmployeeCertificationById(String id);

    /**
     * Query Employee certification issuance list
     *
     * @param hrEmployeeCertification Employee certification issuance
     * @return Employee certification issuance collection
     */
    public List<HrEmployeeCertification> selectHrEmployeeCertificationList(HrEmployeeCertification hrEmployeeCertification);

    /**
     * Add Employee certification issuance
     *
     * @param hrEmployeeCertification Employee certification issuance
     * @return Result
     */
    public int insertHrEmployeeCertification(HrEmployeeCertification hrEmployeeCertification);

    /**
     * Update Employee certification issuance
     *
     * @param hrEmployeeCertification Employee certification issuance
     * @return Result
     */
    public int updateHrEmployeeCertification(HrEmployeeCertification hrEmployeeCertification);

    /**
     * Batch delete Employee certification issuance
     *
     * @param ids Employee certification issuance primary keys to be deleted
     * @return Result
     */
    public int deleteHrEmployeeCertificationByIds(String[] ids);

    /**
     * Delete Employee certification issuance information
     *
     * @param id Employee certification issuance primary key
     * @return Result
     */
    public int deleteHrEmployeeCertificationById(String id);
}
