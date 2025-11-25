package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrTargetTasks;

import java.util.List;

/**
 * Targets Manages Service Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface IHrTargetTasksService extends IService<HrTargetTasks>
{
    /**
     * Query Targets Manages
     *
     * @param id Targets Manages primary key
     * @return Targets Manages
     */
    public HrTargetTasks selectHrTargetTasksById(Long id);

    /**
     * Query Targets Manages list
     *
     * @param hrTargetTasks Targets Manages
     * @return Targets Manages collection
     */
    public List<HrTargetTasks> selectHrTargetTasksList(HrTargetTasks hrTargetTasks);

    /**
     * Add Targets Manages
     *
     * @param hrTargetTasks Targets Manages
     * @return Result
     */
    public int insertHrTargetTasks(HrTargetTasks hrTargetTasks);

    /**
     * Update Targets Manages
     *
     * @param hrTargetTasks Targets Manages
     * @return Result
     */
    public int updateHrTargetTasks(HrTargetTasks hrTargetTasks);
}
