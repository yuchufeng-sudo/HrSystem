package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Dept entity hr_dept
 *
 * @author ys
 * @date 2025-06-04
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrDept extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**  department id */
    @TableId(value = "dept_id", type =  IdType.AUTO )
    private Long deptId;
    /**  department Name */
    @Excel(name = "Department Name")
    private String deptName;
    /** Display Order */
    private String orderNum;
    /** Person in Charge */

    @JsonSerialize(using = ToStringSerializer.class)
    private Long leader;

    private String status;

    private String delFlag;
    /** Enterprise Number */
    private String enterpriseId;

    @Excel(name = "Description")
    private String remark;

    @TableField(exist = false)
    private List<Long> employeeIds;

    @TableField(exist = false)
    private List<HrEmployees> employeesList;

    @TableField(exist = false)
    private String avatars;

    @TableField(exist = false)
    private Integer empNum;

    @TableField(exist = false)
    private String leaderAvatar;

    @TableField(exist = false)
    @Excel(name = "Department Head")
    private String leaderName;

    @TableField(exist = false)
    private String joinTime;

    @TableField(exist = false)
    private Long empId;
}

