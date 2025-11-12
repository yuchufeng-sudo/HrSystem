package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrTargets;

import java.util.List;

/**
 * Main table storing all target information Mapper Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface HrTargetsMapper extends BaseMapper<HrTargets>
{
    /**
     * Query Main table storing all target information
     *
     * @param id Main table storing all target information primary key
     * @return Main table storing all target information
     */
    public HrTargets selectHrTargetsById(Long id);

    /**
     * Query Main table storing all target information list
     *
     * @param hrTargets Main table storing all target information
     * @return Main table storing all target information collection
     */
    public List<HrTargets> selectHrTargetsList(HrTargets hrTargets);

    String selectUserRole(Long userId);
}
