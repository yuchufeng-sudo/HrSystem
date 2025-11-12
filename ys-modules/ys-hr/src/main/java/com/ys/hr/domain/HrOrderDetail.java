package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Order Detail entity hr_order_detail
 *
 * @author ys
 * @date 2025-06-18
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrOrderDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** orderId */
    @TableId(value = "order_id", type =  IdType.ASSIGN_UUID )
    private String orderId;
    /** planId */
    @Excel(name = "planId")
    private String planId;
    /** userId */
    @Excel(name = "userId")
    private Long userId;
    @Excel(name = "enterpriseId")
    private String enterpriseId;
    /** orderType */
    @Excel(name = "orderType")
    private Long orderType;
    /** orderState */
    @Excel(name = "orderState")
    private Long orderState;
    /** orderNum */
    @Excel(name = "orderNum")
    private Long orderNum;
    /** orderPrice */
    @Excel(name = "orderPrice")
    private Double orderPrice;
    /** purchaseTime */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date purchaseTime;
    /** isFreeOrder */
    @Excel(name = "isFreeOrder")
    private Long isFreeOrder;

    private String paypalOrderId;

    private String paypalOrderStatus;

    @TableField(exist = false)
    private String approveUrl;

    private Long planTime;

    private Long isRenew;

    @TableField(exist = false)
    private String planOrderId;


}
