package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrTargetLinks;

import java.util.List;

/**
 * Table storing relationships between targets Mapper Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface HrTargetLinksMapper extends BaseMapper<HrTargetLinks>
{

    /**
     * Query Table storing relationships between targets list
     *
     * @param hrTargetLinks Table storing relationships between targets
     * @return Table storing relationships between targets collection
     */
    public List<HrTargetLinks> selectHrTargetLinksList(HrTargetLinks hrTargetLinks);

}
