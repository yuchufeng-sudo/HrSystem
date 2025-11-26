package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmployeeContract;

import java.util.List;

/**
 * Employee Contract Service Interface
 *
 * @author collection
 * @date 2023-02-26
 */
public interface IHrEmployeeContractService extends IService<HrEmployeeContract> {

    /**
     * Query Employee Contract list
     *
     * @param hrEmployeeContract Employee Contract
     * @return Employee Contract Set
     */
    List<HrEmployeeContract> selectTbEmpContractList(HrEmployeeContract hrEmployeeContract);

    String replaceContent(HrEmployeeContract employee, String templateContent);

    /**
     * User Sign Contract
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
     * Query Contract Information
     *
     * @param employeeContract
     * @return
     */
    public HrEmployeeContract selectEmpContractInfo(HrEmployeeContract employeeContract);

    List<HrEmployeeContract> selectTbEmpContractList1(HrEmployeeContract employeeContract);

    /**
     * Query Contract list
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
     * Query Expiring Contracts
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

    boolean updateStatus(HrEmployeeContract employeeContract);
}
