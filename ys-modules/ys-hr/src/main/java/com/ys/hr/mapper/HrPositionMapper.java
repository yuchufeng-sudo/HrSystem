package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrPosition;

import java.util.List;

/**
 * Position Management Mapper Interface
 *
 * @author ys
 * @date 2025-06-23
 */
public interface HrPositionMapper extends BaseMapper<HrPosition>
{
    /**
     * Query Position Management
     *
     * @param id Position Management primary key
     * @return Position Management
     */
    public HrPosition selectHrPositionById(Long id);

    /**
     * Query Position Management list
     *
     * @param hrPosition Position Management
     * @return Position Management collection
     */
    public List<HrPosition> selectHrPositionList(HrPosition hrPosition);

}
