package com.ys.hr.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.StringUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.vo.DepartmentsVo;
import com.ys.hr.mapper.HrEmployeesMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ys.hr.domain.HrDeptEmployees;
import com.ys.hr.mapper.HrDeptMapper;
import com.ys.hr.domain.HrDept;
import com.ys.hr.service.IHrDeptService;
import com.ys.common.core.utils.DateUtils;

import javax.annotation.Resource;

/**
 * Dept Service Implementation
 *
 * @author ys
 * @date 2025-06-04
 */
@Service
public class HrDeptServiceImpl extends ServiceImpl<HrDeptMapper, HrDept> implements IHrDeptService
{

    @Resource
    private HrEmployeesMapper hrEmployeesMapper;
    /**
     * Query Dept
     *
     * @param deptId Dept primary key
     * @return Dept
     */
    @Override
    public HrDept selectHrDeptByDeptId(Long deptId)
    {
        HrDept hrDept = baseMapper.selectHrDeptByDeptId(deptId);
        HrEmployees hrEmployees = hrEmployeesMapper.selectById(hrDept.getLeader());
        hrDept.setLeaderName(hrEmployees.getFullName());
        hrDept.setLeaderAvatar(hrEmployees.getAvatarUrl());
        HrEmployees hrEmployees1 = new HrEmployees();
        hrEmployees1.setDeptId(deptId);
        List<HrEmployees> hrEmployees2 = hrEmployeesMapper.selectHrEmployeesList(hrEmployees1);
        hrDept.setEmployeesList(hrEmployees2);
        return hrDept;
    }

    /**
     * Query Dept list
     *
     * @param hrDept Dept
     * @return Dept
     */
    @Override
    public List<HrDept> selectHrDeptList(HrDept hrDept)
    {
        return baseMapper.selectHrDeptList(hrDept);
    }

    /**
     * Add Dept
     *
     * @param hrDept Dept
     * @return Result
     */
    @Transactional
    @Override
    public int insertHrDept(HrDept hrDept)
    {
        hrDept.setCreateTime(DateUtils.getNowDate());
        int rows = baseMapper.insert(hrDept);
        insertHrDeptEmployees(hrDept);
        return rows;
    }

    /**
     * Update Dept
     *
     * @param hrDept Dept
     * @return Result
     */
    @Transactional
    @Override
    public int updateHrDept(HrDept hrDept)
    {
        hrDept.setUpdateTime(DateUtils.getNowDate());
        baseMapper.deleteHrDeptEmployeesByDeptId(hrDept.getDeptId());
        insertHrDeptEmployees(hrDept);
        return baseMapper.updateById(hrDept);
    }

    /**
     * Batch delete Dept
     *
     * @param deptIds Dept primary keys to be deleted
     * @return Result
     */
    @Transactional
    @Override
    public int deleteHrDeptByDeptIds(Long[] deptIds)
    {
        baseMapper.deleteHrDeptEmployeesByDeptIds(deptIds);
        return baseMapper.deleteBatchIds(Arrays.asList(deptIds));
    }

    /**
     * Delete Dept information
     *
     * @param deptId Dept primary key
     * @return Result
     */
    @Override
    public int deleteHrDeptByDeptId(Long deptId)
    {
        baseMapper.deleteHrDeptEmployeesByDeptId(deptId);
        return baseMapper.deleteById(deptId);
    }

    /**
     * To query the joined DEPARTMENT. list
     * @param hrDept
     * @return
     */
    @Override
    public List<HrDept> getDeptList(HrDept hrDept) {
        List<HrDept> deptList = baseMapper.getDeptList(hrDept);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
        deptList.forEach(dept -> {
            // Formatting
            String formatted = sdf.format(dept.getCreateTime());
            dept.setJoinTime(formatted);
        });
        return deptList;
    }

    /**
     * **Statistics of DEPARTMENT Employee Numbers**
     * @param userEnterpriseId
     * @return
     */
    @Override
    public List<DepartmentsVo> selectDepartments(String userEnterpriseId) {
        return baseMapper.selectDepartments(userEnterpriseId);
    }

    /**
     * Add Dept Employee information
     *
     * @param hrDept Dept object
     */
    public void insertHrDeptEmployees(HrDept hrDept)
    {
        List<Long> employeeIds = hrDept.getEmployeeIds();
        Long deptId = hrDept.getDeptId();
        if (StringUtils.isNotNull(employeeIds))
        {
            List<String> employeeNames = baseMapper.selectHrDeptEmployeesCountByEmployeeIds(employeeIds.toArray(new Long[0]));
            if (!employeeNames.isEmpty()){
                StringBuilder err = new StringBuilder("The following employees have been transferred to another department and cannot be added.\n");
                for (String employeeName : employeeNames) {
                    err.append(employeeName).append(" ");
                }
                throw new ServiceException(err.toString());
            }
            List<HrDeptEmployees> list = new ArrayList<>();
            for (Long employeeId : employeeIds)
            {
                HrDeptEmployees hrDeptEmployees = new HrDeptEmployees();
                hrDeptEmployees.setEmployeeId(employeeId);
                hrDeptEmployees.setDeptId(deptId);
                list.add(hrDeptEmployees);
            }
            if (!list.isEmpty())
            {
                baseMapper.batchHrDeptEmployees(list);
            }
        }
    }
}
