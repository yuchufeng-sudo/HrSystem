package com.ys.hr.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.hr.domain.*;
import com.ys.hr.service.IHrEmployeeCertificationService;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrCertificationMapper;
import com.ys.hr.service.IHrCertificationService;
import com.ys.common.core.utils.DateUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Certification information Service Implementation
 *
 * @author ys
 * @date 2025-06-24
 */
@Service
public class HrCertificationServiceImpl extends ServiceImpl<HrCertificationMapper, HrCertification> implements IHrCertificationService
{

    @Resource
    private IHrEmployeeCertificationService hrEmployeeCertificationService;

    /**
     * Query Certification information
     *
     * @param id Certification information primary key
     * @return Certification information
     */
    @Override
    public HrCertification selectHrCertificationById(Long id)
    {
        HrCertification hrCertification = baseMapper.selectHrCertificationById(id);
        QueryWrapper<HrEmployeeCertification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("certification_id",id);
        queryWrapper.select("employee_id");
        List<HrEmployeeCertification> list = hrEmployeeCertificationService.list(queryWrapper);
        hrCertification.setEmployeeIds(list.stream()
                .map(HrEmployeeCertification::getEmployeeId)
                .toArray(Long[]::new));
        return hrCertification;
    }

    /**
     * Query Certification information list
     *
     * @param hrCertification Certification information
     * @return Certification information
     */
    @Override
    public List<HrCertification> selectHrCertificationList(HrCertification hrCertification)
    {
        return baseMapper.selectHrCertificationList(hrCertification);
    }

    /**
     * Add Certification information
     *
     * @param hrCertification Certification information
     * @return Result
     */
    @Override
    @Transactional
    public int insertHrCertification(HrCertification hrCertification)
    {
        hrCertification.setCreateTime(DateUtils.getNowDate());
        int insert = baseMapper.insert(hrCertification);
        batchSaveTargetEmployee(hrCertification);
        return insert;
    }

    /**
     * Update Certification information
     *
     * @param hrCertification Certification information
     * @return Result
     */
    @Override
    @Transactional
    public int updateHrCertification(HrCertification hrCertification)
    {
        hrCertification.setUpdateTime(DateUtils.getNowDate());
        int i = baseMapper.updateById(hrCertification);
        Long[] employeeIds = hrCertification.getEmployeeIds();
        if (employeeIds!=null&&employeeIds.length!=0) {
            QueryWrapper<HrEmployeeCertification> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("certification_id", hrCertification.getId());
            hrEmployeeCertificationService.remove(queryWrapper);
        }
        batchSaveTargetEmployee(hrCertification);
        return i;
    }

    private void batchSaveTargetEmployee(HrCertification hrCertification) {
        Long[] employeeIds = hrCertification.getEmployeeIds();
        if (employeeIds!=null&&employeeIds.length!=0) {
            List<HrEmployeeCertification> list = new ArrayList<>();
            for (Long employeeId : employeeIds) {
                HrEmployeeCertification hrEmployeeCertification = new HrEmployeeCertification();
                hrEmployeeCertification.setEmployeeId(employeeId);
                hrEmployeeCertification.setCertificationId(hrCertification.getId());
                hrEmployeeCertification.setCreateTime(DateUtils.getNowDate());
                list.add(hrEmployeeCertification);
            }
            hrEmployeeCertificationService.saveBatch(list);
        }
    }

    /**
     * Batch delete Certification information
     *
     * @param ids Certification information primary keys to be deleted
     * @return Result
     */
    @Override
    @Transactional
    public int deleteHrCertificationByIds(Long[] ids)
    {
        QueryWrapper<HrEmployeeCertification> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("certification_id", ids);
        hrEmployeeCertificationService.remove(queryWrapper);
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Certification information information
     *
     * @param id Certification information primary key
     * @return Result
     */
    @Override
    public int deleteHrCertificationById(Long id)
    {
        return baseMapper.deleteById(id);
    }
}
