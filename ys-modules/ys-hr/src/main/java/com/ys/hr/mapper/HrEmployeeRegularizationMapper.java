package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrEmployeeRegularization;

/**
 * Employee Regularization Management Mapper Interface
 *
 * @author ys
 * @date 2025-06-23
 */
public interface HrEmployeeRegularizationMapper extends BaseMapper<HrEmployeeRegularization>
{
    /**
     * Query Employee Regularization Management
     *
     * @param id Employee Regularization Management primary key
     * @return Employee Regularization Management
     */
    public HrEmployeeRegularization selectHrEmployeeRegularizationById(Long id);

    /**
     * Query Employee Regularization Management list
     *
     * @param hrEmployeeRegularization Employee Regularization Management
     * @return Employee Regularization Management collection
     */
    public List<HrEmployeeRegularization> selectHrEmployeeRegularizationList(HrEmployeeRegularization hrEmployeeRegularization);

}
