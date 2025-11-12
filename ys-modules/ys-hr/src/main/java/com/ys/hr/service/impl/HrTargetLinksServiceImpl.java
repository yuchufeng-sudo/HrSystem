package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrTargetLinksMapper;
import com.ys.hr.domain.HrTargetLinks;
import com.ys.hr.service.IHrTargetLinksService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Table storing relationships between targets Service Implementation
 *
 * @author ys
 * @date 2025-06-18
 */
@Service
public class HrTargetLinksServiceImpl extends ServiceImpl<HrTargetLinksMapper, HrTargetLinks> implements IHrTargetLinksService
{
    /**
     * Query Table storing relationships between targets list
     *
     * @param hrTargetLinks Table storing relationships between targets
     * @return Table storing relationships between targets
     */
    @Override
    public List<HrTargetLinks> selectHrTargetLinksList(HrTargetLinks hrTargetLinks)
    {
        return baseMapper.selectHrTargetLinksList(hrTargetLinks);
    }

    /**
     * Add Table storing relationships between targets
     *
     * @param hrTargetLinks Table storing relationships between targets
     * @return Result
     */
    @Override
    public int insertHrTargetLinks(HrTargetLinks hrTargetLinks)
    {
        hrTargetLinks.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrTargetLinks);
    }

    /**
     * Update Table storing relationships between targets
     *
     * @param hrTargetLinks Table storing relationships between targets
     * @return Result
     */
    @Override
    public int updateHrTargetLinks(HrTargetLinks hrTargetLinks)
    {
        hrTargetLinks.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrTargetLinks);
    }
}
