package com.ys.hr.task;

import com.ys.common.core.utils.DateUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.enums.EmployeeStatus;
import com.ys.hr.service.IHrEmployeesService;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/11/6
 */
@Service
public class OffboardingTask {

    @Resource
    private IHrEmployeesService hrEmployeesService;

//    @Bean
    @Scheduled(cron = "0 01 00 * * ?")
    public void offboarding() {
        HrEmployees hrEmployees = new HrEmployees();
        hrEmployees.setStatus(EmployeeStatus.OFFBOARDING.getCode());
        hrEmployees.setSystemAccess("1");
        hrEmployees.setResignationDate(DateUtils.getNowDate());
        List<HrEmployees> hrEmployees1 = hrEmployeesService.selectHrEmployeesList(hrEmployees);
        for (HrEmployees employees : hrEmployees1) {
            hrEmployeesService.resignEmployee(employees.getEmployeeId());
        }
        Instant nowInstant = Instant.now();
        Instant oneWeekAgoInstant = nowInstant.minus(Duration.ofDays(7));
        Date oneWeekAgo = Date.from(oneWeekAgoInstant);
        hrEmployees.setResignationDate(oneWeekAgo);
        List<HrEmployees> hrEmployees2 = hrEmployeesService.selectHrEmployeesList(hrEmployees);
        for (HrEmployees employees : hrEmployees2) {
            hrEmployeesService.resignEmployeeOneWeekAgo(employees);
        }
        Instant oneDayAgoInstant = nowInstant.minus(Duration.ofDays(1));
        Date oneDayAgo = Date.from(oneDayAgoInstant);
        hrEmployees.setResignationDate(oneDayAgo);
        List<HrEmployees> hrEmployees3 = hrEmployeesService.selectHrEmployeesList(hrEmployees);
        for (HrEmployees employees : hrEmployees3) {
            hrEmployeesService.resignEmployeeOneDayAgo(employees);
        }
    }
}
