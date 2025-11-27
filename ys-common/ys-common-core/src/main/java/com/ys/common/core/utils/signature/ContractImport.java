package com.ys.common.core.utils.signature;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * contract_import
 *
 * @author collection
 * @date 2023-12-23
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractImport extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)
    private String id;

    @Excel(name = "Company Name")
    private String suppliername;

    @Excel(name = "Company Address")
    private String supplieraddress;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Contract Date", width = 30, dateFormat = "yyyy-MM-dd")
    private String contractdate;

    @Excel(name = "Party A's CONTACT NUMBERR")
    private String supplierphone;

    @Excel(name = "Main Person in Charge")
    private String legalrepresentative;

    @Excel(name = "The surname of Party B")
    private String name;

    @Excel(name = "Household Registration Location")
    private String permanentaddress;

    @Excel(name = "ID Card of Party B")
    private String idnumber;

    @Excel(name = "Current Address")
    private String relationaddress;

    @Excel(name = "Contact Number of Party B")
    private String phone;
    /** Emergency Contact */
    @Excel(name = "Emergency Contact")
    private String phone1;
    /** Employment Form */
    @Excel(name = "Employment Form")
    private String workType;
    /** Automatic Renewal */
    @Excel(name = "Automatic Renewal")
    private String autocontinue;
    /** Automatic Renewal */
    @Excel(name = "Indefinite Term")
    private String fixationDate;
    /** Automatic Renewal */
    @Excel(name = "Complete the task")
    private String perform;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Contract Start time ", width = 30, dateFormat = "yyyy-MM-dd")
    private String contractstarttimestr;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Contract End time ", width = 30, dateFormat = "yyyy-MM-dd")
    private String contractendtimestr;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Probation Period Start time ", width = 30, dateFormat = "yyyy-MM-dd")
    private String trystartdatestring;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Probation Period End time ", width = 30, dateFormat = "yyyy-MM-dd")
    private String tryenddatestring;

    @Excel(name = " POSITION ")
    private String station;

    @Excel(name = "Work Location")
    private String workaddress;

    @Excel(name = "Work Mode")
    private String workway;

    @Excel(name = " salary ")
    private BigDecimal salary;

    @Excel(name = "Probation Period salary")
    private BigDecimal trysalary;

    @Excel(name = "Payday")
    private Integer paymentdate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Contract Signing Date", width = 30, dateFormat = "yyyy-MM-dd")
    private String signdate;


}
