package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrTargetNotesMapper;
import com.ys.hr.domain.HrTargetNotes;
import com.ys.hr.service.IHrTargetNotesService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Table storing notes associated with targets Service Implementation
 *
 * @author ys
 * @date 2025-06-18
 */
@Service
public class HrTargetNotesServiceImpl extends ServiceImpl<HrTargetNotesMapper, HrTargetNotes> implements IHrTargetNotesService
{

    /**
     * Query Table storing notes associated with targets list
     *
     * @param hrTargetNotes Table storing notes associated with targets
     * @return Table storing notes associated with targets
     */
    @Override
    public List<HrTargetNotes> selectHrTargetNotesList(HrTargetNotes hrTargetNotes)
    {
        return baseMapper.selectHrTargetNotesList(hrTargetNotes);
    }

    /**
     * Add Table storing notes associated with targets
     *
     * @param hrTargetNotes Table storing notes associated with targets
     * @return Result
     */
    @Override
    public int insertHrTargetNotes(HrTargetNotes hrTargetNotes)
    {
        hrTargetNotes.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrTargetNotes);
    }

    /**
     * Update Table storing notes associated with targets
     *
     * @param hrTargetNotes Table storing notes associated with targets
     * @return Result
     */
    @Override
    public int updateHrTargetNotes(HrTargetNotes hrTargetNotes)
    {
        hrTargetNotes.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrTargetNotes);
    }
}
