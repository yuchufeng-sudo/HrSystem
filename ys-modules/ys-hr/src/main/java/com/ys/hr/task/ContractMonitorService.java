package com.ys.hr.task;

import com.ys.hr.domain.HrEmployeeContract;
import com.ys.hr.domain.HrTrainingPrograms;
import com.ys.hr.service.IHrEmployeeContractService;
import com.ys.hr.service.impl.HrTrainingProgramsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * THE CONTRACT EXPIRES Detection
 */
@Service
public class ContractMonitorService {

    @Autowired
    private IHrEmployeeContractService employeeContractService;

    @Autowired
    private HrTrainingProgramsServiceImpl trainingProgramsService;

    // Check once at midnight every day (do not translate the content in parentheses, do not change the code, only translate the Chinese content)
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkContractExpiry() {
        List<HrEmployeeContract> expiringContracts = employeeContractService.getExpiringContracts();
        expiringContracts.forEach(employeeContract -> {
            employeeContractService.messageNotification(employeeContract);
        });
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkTrainingPrograms() {
        HrTrainingPrograms hrTrainingPrograms = new HrTrainingPrograms();
        hrTrainingPrograms.setSendReminder("1");
        List<HrTrainingPrograms> trainingPrograms = trainingProgramsService.selectHrTrainingProgramsList(hrTrainingPrograms);
        if(!trainingPrograms.isEmpty()){
            trainingPrograms.forEach(trainingProgram -> {
                trainingProgramsService.updateHrTrainingPrograms(trainingProgram);
            });
        }

    }

}
