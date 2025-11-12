package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dept EmployeeObject hr_dept_employees
 *
 * @author ys
 * @date 2025-06-04
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrDeptEmployees {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;
    /** dept ID */
    private Long deptId;
    /** employee ID */
    private Long employeeId;
}
