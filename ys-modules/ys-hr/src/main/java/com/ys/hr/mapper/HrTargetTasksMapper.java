package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrTargetTasks;

import java.util.List;

/**
 * Manages task assignments and progress tracking for employee targets Mapper Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface HrTargetTasksMapper extends BaseMapper<HrTargetTasks>
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

}
