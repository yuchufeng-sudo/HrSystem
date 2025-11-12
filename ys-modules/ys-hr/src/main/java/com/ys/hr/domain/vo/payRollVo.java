package com.ys.hr.domain.vo;

import com.ys.common.core.annotation.Excel;
import lombok.Data;

/**
 * @Author：marin
 * @Date ：2025/6/30 16:32
 */
@Data
public class payRollVo {
    @Excel(name = "bsb")
    private String bsb;
    @Excel(name = "account_num")
    private String bankNumber;
    @Excel(name = "name")
    private String fullName;
    @Excel(name = "amount")
    private Double afterTaxSalary;
    @Excel(name = "txn_reference")
    private String remark ;
}
