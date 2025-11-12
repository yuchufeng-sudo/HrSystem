package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrDept;
import com.ys.hr.domain.HrDeptEmployees;
import com.ys.hr.domain.vo.DepartmentsVo;

import java.util.List;

/**
 * Dept Mapper Interface
 *
 * @author ys
 * @date 2025-06-04
 */
public interface HrDeptMapper extends BaseMapper<HrDept>
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
     * Delete Dept Employee information by Dept primary key
     *
     * @param deptId Dept ID
     * @return Result
     */
    public int deleteHrDeptEmployeesByDeptId(Long deptId);

    public int deleteHrDeptEmployeesByEmployeesId(Long employeeId);

    /**
     * Batch delete Dept Employee
     *
     * @param deptIds Primary keys of data to be deleted
     * @return Result
     */
    public int deleteHrDeptEmployeesByDeptIds(Long[] deptIds);

    public List<String> selectHrDeptEmployeesCountByEmployeeIds(Long[] employeeIds);

    /**
     * Batch insert Dept Employee
     *
     * @param hrDeptEmployeesList Dept Employee list
     * @return Result
     */
    public int batchHrDeptEmployees(List<HrDeptEmployees> hrDeptEmployeesList);

    /**
     * To query the joined DEPARTMENT.
  LIST
     * @param dept
     * @return
     */
    List<HrDept> getDeptList(HrDept dept);

    /**
     * Statistics of DEPARTMENT Personnel
     * @param userEnterpriseId
     * @return
     */
    List<DepartmentsVo> selectDepartments(String userEnterpriseId);
}
