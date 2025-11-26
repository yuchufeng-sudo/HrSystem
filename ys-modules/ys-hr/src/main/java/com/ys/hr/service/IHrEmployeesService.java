package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.excel.HrEmployeesExcel;
import com.ys.hr.domain.vo.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *  Employee Service Interface
 *
 * @author ys
 * @date 2025-05-21
 */
public interface IHrEmployeesService extends IService<HrEmployees>
{

    /**
     * Query THE Employee list.
     *
     * @param hrEmployees  Employee
     * @return Employee Set
     */
    List<HrEmployees> selectHrEmployeesList(HrEmployees hrEmployees);

    List<HrEmployeesExcel> selectHrEmployeesExcelList(HrEmployees hrEmployees);

    HrEmployees selectHrEmployeesByUserId(Long userId);

    HrEmployees selectHrEmployeesById(Long id);

    Long insertEmployees(HrEmployees hrEmployees);

    Long updateEmployees(HrEmployees hrEmployees);

    List<EmergencyContactsReportVo> selectEmergencyContactsReportVo(HrEmployees hrEmployees);

    List<BirthdayReportVo> selectBirthdayReportVoReportVo(HrEmployees hrEmployees);

    /**
     * Statistical data of EmployeeS under different Statuses.
     * @param userEnterpriseId
     * @return
     */
    Map<String, Object> getEmployeeStatusCount(String userEnterpriseId);

    /**
     * Query Employee QUANTITY
     * @param userEnterpriseId
     * @return
     */
    Map<String, Object> selectEmployeeCount(String userEnterpriseId);

    /**
     * Query Employee Birthdays
     * @param hrEmployees
     * @return
     */
    List<BirthdayReportVo> selectEmployeeBirthday(HrEmployees hrEmployees);

    /**
     * Celebration
     * @param userId
     * @return
     */
    List<CelebrationVo> selectCelebrationList(Long userId);

    int deleteById(Long employeeId);

    @Transactional
    int resignEmployee(Long employeeId);

    int resignEmployees(HrEmployees employees);

    int resignCancelEmployees(HrEmployees employees);

    int restoreEmployees(Long employeeId);

    int updateAvatarUrl(HrEmployees employees);

    void resignEmployeeOneWeekAgo(HrEmployees employees);

    void resignEmployeeOneDayAgo(HrEmployees employees);

    void resignComplete(Long employeeId, Long hrUserId);
}
