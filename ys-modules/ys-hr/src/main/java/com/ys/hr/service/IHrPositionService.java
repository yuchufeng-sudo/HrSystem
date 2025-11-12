package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrPosition;

import java.util.List;

/**
 * Position Management Service Interface
 *
 * @author ys
 * @date 2025-06-23
 */
public interface IHrPositionService extends IService<HrPosition>
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

    /**
     * Add Position Management
     *
     * @param hrPosition Position Management
     * @return Result
     */
    public int insertHrPosition(HrPosition hrPosition);

    /**
     * Update Position Management
     *
     * @param hrPosition Position Management
     * @return Result
     */
    public int updateHrPosition(HrPosition hrPosition);
}
