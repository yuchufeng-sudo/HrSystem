package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrDept;
import com.ys.hr.domain.vo.DepartmentsVo;

import java.util.List;

/**
 * Dept Service Interface
 *
 * @author ys
 * @date 2025-06-04
 */
public interface IHrDeptService extends IService<HrDept>
{
    /**
     * Query Dept
     *
     * @param deptId Dept primary key
     * @return Dept
     */
    public HrDept selectHrDeptByDeptId(Long deptId);

    /**
     * Query Dept list
     *
     * @param hrDept Dept
     * @return Dept collection
     */
    public List<HrDept> selectHrDeptList(HrDept hrDept);

    /**
     * Add Dept
     *
     * @param hrDept Dept
     * @return Result
     */
    public int insertHrDept(HrDept hrDept);

    /**
     * Update Dept
     *
     * @param hrDept Dept
     * @return Result
     */
    public int updateHrDept(HrDept hrDept);

    /**
     * Batch delete Dept
     *
     * @param deptIds Dept primary keys to be deleted
     * @return Result
     */
    public int deleteHrDeptByDeptIds(Long[] deptIds);

    /**
     * Delete Dept information
     *
     * @param deptId Dept primary key
     * @return Result
     */
    public int deleteHrDeptByDeptId(Long deptId);

    /**
     * To query the joined DEPARTMENT.
  LIST
     * @param dept
     * @return
     */
    List<HrDept> getDeptList(HrDept dept);

    /**
     * Statistics of DEPARTMENT EMPLOYEE Quantity
     * @param userEnterpriseId
     * @return
     */
    List<DepartmentsVo> selectDepartments(String userEnterpriseId);
}
