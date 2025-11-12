package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrTargetTasks;

import java.util.List;

/**
 * Manages task assignments and progress tracking for employee targets Service Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface IHrTargetTasksService extends IService<HrTargetTasks>
{
    /**
     * Query Manages task assignments and progress tracking for employee targets
     *
     * @param id Manages task assignments and progress tracking for employee targets primary key
     * @return Manages task assignments and progress tracking for employee targets
     */
    public HrTargetTasks selectHrTargetTasksById(Long id);

    /**
     * Query Manages task assignments and progress tracking for employee targets list
     *
     * @param hrTargetTasks Manages task assignments and progress tracking for employee targets
     * @return Manages task assignments and progress tracking for employee targets collection
     */
    public List<HrTargetTasks> selectHrTargetTasksList(HrTargetTasks hrTargetTasks);

    /**
     * Add Manages task assignments and progress tracking for employee targets
     *
     * @param hrTargetTasks Manages task assignments and progress tracking for employee targets
     * @return Result
     */
    public int insertHrTargetTasks(HrTargetTasks hrTargetTasks);

    /**
     * Update Manages task assignments and progress tracking for employee targets
     *
     * @param hrTargetTasks Manages task assignments and progress tracking for employee targets
     * @return Result
     */
    public int updateHrTargetTasks(HrTargetTasks hrTargetTasks);
}
