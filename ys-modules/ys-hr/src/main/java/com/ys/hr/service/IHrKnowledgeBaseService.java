package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrKnowledgeBase;

import java.util.List;

/**
 *  Knowledge base  Service Interface
 *
 * @author ys
 * @date 2025-06-04
 */
public interface IHrKnowledgeBaseService extends IService<HrKnowledgeBase>
{
    /**
     * Query Knowledge base
     *
     * @param id  Knowledge base  primary key
     * @return  Knowledge base
     */
    public HrKnowledgeBase selectHrKnowledgeBaseById(Long id);

    /**
     * Query Knowledge base  list
     *
     * @param hrKnowledgeBase  Knowledge base
     * @return  Knowledge base  collection
     */
    public List<HrKnowledgeBase> selectHrKnowledgeBaseList(HrKnowledgeBase hrKnowledgeBase);

    /**
     * Add Knowledge base
     *
     * @param hrKnowledgeBase  Knowledge base
     * @return Result
     */
    public int insertHrKnowledgeBase(HrKnowledgeBase hrKnowledgeBase);

    /**
     * Update Knowledge base
     *
     * @param hrKnowledgeBase  Knowledge base
     * @return Result
     */
    public int updateHrKnowledgeBase(HrKnowledgeBase hrKnowledgeBase);

    /**
     * Batch delete  Knowledge base
     *
     * @param ids  Knowledge base  primary keys to be deleted
     * @return Result
     */
    public int deleteHrKnowledgeBaseByIds(Long[] ids);

    /**
     * Delete Knowledge base  information
     *
     * @param id  Knowledge base  primary key
     * @return Result
     */
    public int deleteHrKnowledgeBaseById(Long id);

    List<HrKnowledgeBase> searchAllHrKnowledgeBase(String searchParam, String enterpriseId);
}
