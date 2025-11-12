package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pay Plan entity hr_payplan
 *
 * @author ys
 * @date 2025-06-17
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrPayplan extends BaseEntity {
    private static final long serialVersionUID = 1L;

/** Payment Configuration ID */
@TableId(value = "pay_id", type = IdType.ASSIGN_UUID)
private String payId;

/** Payment Plan Name (Basic, Pro) */
@Excel(name = "Payment Plan Name")
private String payName;

/** Whether it is a free plan (0/1) */
@Excel(name = "Whether it is a free plan (0/1)")
private Long isFreeTrial;

/** Monthly Payment Price */
@Excel(name = "Monthly Payment Price")
private Double payMonthPrice;

/** Annual Payment Price */
@Excel(name = "Annual Payment Price")
private Double payYearPrice;

/** Discount Percentage
 */
@Excel(name = "Discount Percentage ")
private Long discountPercent;

/** Valid Days for Monthly Payment */
@Excel(name = "Valid Days for Monthly Payment")
private Long validMonthDays;

/** Valid Days for Annual Payment */
@Excel(name = "Valid Days for Annual Payment")
private Long validYearDays;

/** Plan Description */
@Excel(name = "Plan Description")
private String payDetail;

/** Trial Days */
@Excel(name = "Trial Days")
private Long freeDays;

    private Long level;
}
