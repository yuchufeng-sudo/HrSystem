package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrTargetLinks;

import java.util.List;

/**
 * Table storing relationships between targets Service Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface IHrTargetLinksService extends IService<HrTargetLinks>
{

    /**
     * Query Table storing relationships between targets list
     *
     * @param hrTargetLinks Table storing relationships between targets
     * @return Table storing relationships between targets collection
     */
    public List<HrTargetLinks> selectHrTargetLinksList(HrTargetLinks hrTargetLinks);

    /**
     * Add Table storing relationships between targets
     *
     * @param hrTargetLinks Table storing relationships between targets
     * @return Result
     */
    public int insertHrTargetLinks(HrTargetLinks hrTargetLinks);

    /**
     * Update Table storing relationships between targets
     *
     * @param hrTargetLinks Table storing relationships between targets
     * @return Result
     */
    public int updateHrTargetLinks(HrTargetLinks hrTargetLinks);
}
