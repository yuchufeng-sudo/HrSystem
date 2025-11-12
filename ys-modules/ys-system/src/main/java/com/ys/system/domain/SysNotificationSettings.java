package com.ys.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Notification Settings entity sys_notification_settings
 *
 * @author ys
 * @date 2025-05-29
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysNotificationSettings extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;
    /** User ID */
    private Long userId;
    /** Account emails level */
    private String accountEmailLevel;
    /** Leave Request Notification */
    private String leaveRequestNotification;
    /** Performance Evaluation Alerts */
    private String performanceEvaluationAlerts;
    /** Shift Schedule Update */
    private String shiftScheduleUpdate;
    /** Salary and Benefits Notifications */
    private String salaryAndBenefitsNotifications;
    /** Contract Period Alerts */
    private String contractPeriodAlerts;
    /** SMS Account information */
    private String smsAccountInfo;
    /** SMS Offers and promotions */
    private String smsOffersPromotions;

    @Excel(name="User Name")
    @TableField(exist = false)
    private String userName;

    @Excel(name="User Email")
    @TableField(exist = false)
    private String userEmail;

    /** New features and updates */
    @Excel(name = "New features and updates", readConverterExp = "0=off,1=on")
    private String newFeaturesUpdates;
    /** Offers and promotions */
    @Excel(name = "Offers and promotions", readConverterExp = "0=off,1=on")
    private String offersPromotions;
    /** Feedback and surveys */
    @Excel(name = "Feedback and surveys", readConverterExp = "0=off,1=on")
    private String feedbackSurveys;
    /** Unsubscribe from all marketing emails */
    @Excel(name = "Unsubscribe from all marketing emails", readConverterExp = "0=off,1=on")
    private String unsubscribeAllMarketing;
}
