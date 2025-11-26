package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.vo.*;
import com.ys.system.api.domain.SysRole;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.domain.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Employee Mapper Interface
 *
 * @author ys
 * @date 2025-05-21
 */
public interface HrEmployeesMapper extends BaseMapper<HrEmployees> {
    /**
     * Query THE Employee list.
     *
     * @param hrEmployees Employee
     * @return Employee Set
     */
    public List<HrEmployees> selectHrEmployeesList(HrEmployees hrEmployees);

    List<SysUser> selectSysUserCountByUsername(@Param("username") String username);

    List<SysUser> selectSysUserCountByEmail(@Param("email") String email);

    int selectEmployeeCountByWorkNo(@Param("jobnumber") String jobnumber, @Param("enterpriseId") String enterpriseId);

    HrEmployees selectHrEmployeesByUserId(Long userId);

    HrEmployees selectHrEmployeesById(Long id);

    Long selectRoleByKey(@Param("roleKey") String roleKey, @Param("enterpriseId") String enterpriseId);

    void insertSysUser(SysUser sysUser);

    void updateSysUser(SysUser sysUser);

    void deleteSysUserById(Long userId);

    SysRole selectSysRoleById(Long roleId);

    void insertUserRole(SysUserRole sysUser);

    int deleteUserRoleByUserId(Long userId);

    List<HrEmployees> selectHrEmployeesListByEid(String enterpriseId);

    List<EmergencyContactsReportVo> selectEmergencyContactsReportVo(HrEmployees hrEmployees);

    List<BirthdayReportVo> selectBirthdayReportVoReportVo(HrEmployees hrEmployees);

    /**
     * Query data of different Employee Status
     *
     * @param userEnterpriseId
     * @return
     */
    EmployeementStatusVo selectEmployeeStatusCount(String userEnterpriseId);

    /**
     * Query Employee QUANTITY
     *
     * @param userEnterpriseId
     * @return
     */
    EmployeeCountVo selectEmployeeCount(String userEnterpriseId);

    /**
     * Query Employee Birthdays
     *
     * @param hrEmployees
     * @return
     */
    List<BirthdayReportVo> selectEmployeeBirthday(HrEmployees hrEmployees);

    /**
     * Query the employee list based on the enterprise ID.
     *
     * @param userEnterpriseId
     * @return
     */
    List<HrEmployees> selectEmployeesListByEid(String userEnterpriseId);

    /**
     * Query employees who were hired before today.
     *
     * @param userEnterpriseId
     * @return
     */
    List<HrEmployees> selectHrEmployeesListByBeforeHireDate(String userEnterpriseId);

    /**
     * Query employee information based on the employee ID.
     *
     * @param jobnumber
     * @return
     */
    HrEmployees selectEmployeeByJobnumber(String jobnumber);

    /**
     * et a list of employees who are not included in the candidate list
     *
     * @param userEnterpriseId
     * @return
     */
    List<HrEmployees> selectEmployeeList(String userEnterpriseId);

    List<AddressReportVo> selectAddressReportVo(HrEmployees hrEmployees);

    HrEmployees selectEmployeeByWorkNo(@Param("jobnumber") String jobnumber,
                                       @Param("userEnterpriseId") String userEnterpriseId);

    String seleEid(@Param("userEnterpriseId") String userEnterpriseId);
}
