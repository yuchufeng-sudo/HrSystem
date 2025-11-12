package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrTargetEmployee;
import com.ys.hr.domain.HrTargets;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Target allocation of employees Mapper Interface
 *
 * @author ys
 * @date 2025-06-23
 */
public interface HrTargetEmployeeMapper extends BaseMapper<HrTargetEmployee>
{
    /**
     * Query Target allocation of employees
     *
     * @param id Target allocation of employees primary key
     * @return Target allocation of employees
     */
    public HrTargetEmployee selectHrTargetEmployeeById(Long id);

    /**
     * Query Target allocation of employees list
     *
     * @param hrTargetEmployee Target allocation of employees
     * @return Target allocation of employees collection
     */
    public List<HrTargetEmployee> selectHrTargetEmployeeList(HrTargetEmployee hrTargetEmployee);

    HrTargetEmployee selectByUserIdAndTargetId(@Param("employeeId") Long employeeId,
                                               @Param("targetId") Long targetId);

    void updateByEmployeeId(Long employeeId);

    HrTargetEmployee selectExecutionTarget(Long employeeId);

    int stopPause(Long id);

    int finish(HrTargetEmployee hrTargetEmployee);

    List<HrTargetEmployee> selectEmployeeTargets(HrTargets hrTargets);

    List<HrTargetEmployee> selectEmployeeTargetsByAdmin(HrTargets hrTargets);

    void saveBatchEmployees(List<HrTargetEmployee> list);
}
