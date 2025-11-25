package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrTargetTasks;

import java.util.List;

/**
 * Targets Manages Mapper Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface HrTargetTasksMapper extends BaseMapper<HrTargetTasks>
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

}
