package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrTargetNotes;

import java.util.List;

/**
 * Table storing notes associated with targets Mapper Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface HrTargetNotesMapper extends BaseMapper<HrTargetNotes>
{

    /**
     * Query Table storing notes associated with targets list
     *
     * @param hrTargetNotes Table storing notes associated with targets
     * @return Table storing notes associated with targets collection
     */
    public List<HrTargetNotes> selectHrTargetNotesList(HrTargetNotes hrTargetNotes);

}
