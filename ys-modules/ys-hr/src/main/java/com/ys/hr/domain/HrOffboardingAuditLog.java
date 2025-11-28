package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * Offboarding Process Audit Log Entity
 */
@Data
@TableName("hr_offboarding_audit_log")
public class HrOffboardingAuditLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** Employee ID */
    private Long employeeId;

    /** Employee Name */
    private String employeeName;

    /** Operation Type */
    private String operationType;

    /** Operator ID */
    private Long operatorId;

    /** Operator Name */
    private String operatorName;

    /** Enterprise ID */
    private String enterpriseId;

    /** Operation Time */
    private Date operationTime;

    /** Operation Details */
    private String operationDetails;

    /** IP Address */
    private String ipAddress;
}
