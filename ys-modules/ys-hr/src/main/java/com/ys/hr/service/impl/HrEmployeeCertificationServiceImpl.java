package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.DateUtils;
import com.ys.hr.domain.HrEmployeeCertification;
import com.ys.hr.mapper.HrEmployeeCertificationMapper;
import com.ys.hr.service.IHrEmployeeCertificationService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Employee certification issuance Service Implementation
 *
 * @author ys
 * @date 2025-06-24
 */
@Service
public class HrEmployeeCertificationServiceImpl extends ServiceImpl<HrEmployeeCertificationMapper, HrEmployeeCertification> implements IHrEmployeeCertificationService
{

    /**
     * Query Employee certification issuance
     *
     * @param id Employee certification issuance primary key
     * @return Employee certification issuance
     */
    @Override
    public HrEmployeeCertification selectHrEmployeeCertificationById(String id)
    {
        return baseMapper.selectHrEmployeeCertificationById(id);
    }

    /**
     * Query Employee certification issuance list
     *
     * @param hrEmployeeCertification Employee certification issuance
     * @return Employee certification issuance
     */
    @Override
    public List<HrEmployeeCertification> selectHrEmployeeCertificationList(HrEmployeeCertification hrEmployeeCertification)
    {
        return baseMapper.selectHrEmployeeCertificationList(hrEmployeeCertification);
    }

    /**
     * Add Employee certification issuance
     *
     * @param hrEmployeeCertification Employee certification issuance
     * @return Result
     */
    @Override
    public int insertHrEmployeeCertification(HrEmployeeCertification hrEmployeeCertification)
    {
        hrEmployeeCertification.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrEmployeeCertification);
    }

    /**
     * Update Employee certification issuance
     *
     * @param hrEmployeeCertification Employee certification issuance
     * @return Result
     */
    @Override
    public int updateHrEmployeeCertification(HrEmployeeCertification hrEmployeeCertification)
    {
        hrEmployeeCertification.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrEmployeeCertification);
    }

    /**
     * Batch delete Employee certification issuance
     *
     * @param ids Employee certification issuance primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrEmployeeCertificationByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Employee certification issuance information
     *
     * @param id Employee certification issuance primary key
     * @return Result
     */
    @Override
    public int deleteHrEmployeeCertificationById(String id)
    {
        return baseMapper.deleteById(id);
    }
}
