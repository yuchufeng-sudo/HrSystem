package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrTargetNotes;

import java.util.List;

/**
 * Table storing notes associated with targets Service Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface IHrTargetNotesService extends IService<HrTargetNotes>
{

    /**
     * Query Table storing notes associated with targets list
     *
     * @param hrTargetNotes Table storing notes associated with targets
     * @return Table storing notes associated with targets collection
     */
    public List<HrTargetNotes> selectHrTargetNotesList(HrTargetNotes hrTargetNotes);

    /**
     * Add Table storing notes associated with targets
     *
     * @param hrTargetNotes Table storing notes associated with targets
     * @return Result
     */
    public int insertHrTargetNotes(HrTargetNotes hrTargetNotes);

    /**
     * Update Table storing notes associated with targets
     *
     * @param hrTargetNotes Table storing notes associated with targets
     * @return Result
     */
    public int updateHrTargetNotes(HrTargetNotes hrTargetNotes);
}
