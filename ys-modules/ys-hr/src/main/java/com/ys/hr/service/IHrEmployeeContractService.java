package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmployeeContract;

import java.util.List;

/**
 * EMPLOYEE Contract Service Interface
 *
 * @author collection
 * @date 2023-02-26
 */
public interface IHrEmployeeContractService extends IService<HrEmployeeContract> {

    /**
     * QUERY EMPLOYEE Contract LIST
     *
     * @param hrEmployeeContract EMPLOYEE Contract
     * @return EMPLOYEE Contract Set
     */
    List<HrEmployeeContract> selectTbEmpContractList(HrEmployeeContract hrEmployeeContract);

    String replaceContent(HrEmployeeContract employee, String templateContent);

    /**
     * USER Sign Contract
     *
     * @param contractNo
     * @param status
     */
    public void userSign(String contractNo, String status);

    /**
     * Sign Contract Success
     *
     * @param contractNo
     * @param status
     */
    public void singSuccess(String contractNo, String status);

    /**
     * QUERY Contract Information
     *
     * @param employeeContract
     * @return
     */
    public HrEmployeeContract selectEmpContractInfo(HrEmployeeContract employeeContract);

    List<HrEmployeeContract> selectTbEmpContractList1(HrEmployeeContract employeeContract);

    /**
     * QUERY Contract LIST
     *
     * @param employeeContract
     * @return
     */
    List<HrEmployeeContract> selectTbEmpContracts(HrEmployeeContract employeeContract);

    /**
     * Electronic Signature Business
     *
     * @param empContract
     * @return
     */
    int eSign(HrEmployeeContract empContract);

    /**
     * QUERY Expiring Contracts
     *
     * @return
     */
    List<HrEmployeeContract> getExpiringContracts();

    /**
     * Send Message Notification
     *
     * @param employeeContract
     */
    void messageNotification(HrEmployeeContract employeeContract);

    /**
     * Process Contract Renewal
     *
     * @param empContract
     */
    void renewalContract(HrEmployeeContract empContract);
}
