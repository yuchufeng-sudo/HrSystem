package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class HrEmpPayrollDetail {
    // USER ID
    private Long userId;

    // USER Name
    private String nickName;

    // USER avatar
    private String avatar;

    // Company ID
    private String enterpriseId;

    // Days Worked This Month
    private Long mouthDays;

    // Attendance Time This Month
    private Double presentHours;

    // Paid Modify Time
    private Double holidaysHours;

    // Leave Application Time This Month
    private Double leaveHours;

    // Late Time
    private Double lateHours;

    // Early Leave Time
    private Double earlyHours;

    // Overtime
    private Double overHours;

    // Absent Time
    private Double absentHours;

    // Working Hours This Month
    private Double workHours;

    // Month
    private String payrollData;

    /** Basic Salary */
    private Long baseSalary;

    private Character payrollStatus;

    @TableField(exist = false)
    private Double paidHours;

}
