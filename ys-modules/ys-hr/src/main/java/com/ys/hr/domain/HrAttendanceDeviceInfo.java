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
 * Attendance Device Information  entity hr_attendance_device_info
 *
 * @author ys
 * @date 2025-06-23
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrAttendanceDeviceInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary Key */
    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;
    /** Device Name */
    @Excel(name = "Device Name")
    private String deviceName;
    /** SN code */
    @Excel(name = "SN code")
    private String deviceSn;

}
