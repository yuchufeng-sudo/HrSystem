package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrKnowledgeBase;

import java.util.List;

/**
 *  KNOWLEDGE BASE  Service Interface
 *
 * @author ys
 * @date 2025-06-04
 */
public interface IHrKnowledgeBaseService extends IService<HrKnowledgeBase>
{
    /**
     * Query  KNOWLEDGE BASE
     *
     * @param id  KNOWLEDGE BASE  primary key
     * @return  KNOWLEDGE BASE
     */
    public HrKnowledgeBase selectHrKnowledgeBaseById(Long id);

    /**
     * Query  KNOWLEDGE BASE  list
     *
     * @param hrKnowledgeBase  KNOWLEDGE BASE
     * @return  KNOWLEDGE BASE  collection
     */
    public List<HrKnowledgeBase> selectHrKnowledgeBaseList(HrKnowledgeBase hrKnowledgeBase);

    /**
     * Add  KNOWLEDGE BASE
     *
     * @param hrKnowledgeBase  KNOWLEDGE BASE
     * @return Result
     */
    public int insertHrKnowledgeBase(HrKnowledgeBase hrKnowledgeBase);

    /**
     * Update  KNOWLEDGE BASE
     *
     * @param hrKnowledgeBase  KNOWLEDGE BASE
     * @return Result
     */
    public int updateHrKnowledgeBase(HrKnowledgeBase hrKnowledgeBase);

    /**
     * Batch delete  KNOWLEDGE BASE
     *
     * @param ids  KNOWLEDGE BASE  primary keys to be deleted
     * @return Result
     */
    public int deleteHrKnowledgeBaseByIds(Long[] ids);

    /**
     * Delete  KNOWLEDGE BASE  information
     *
     * @param id  KNOWLEDGE BASE  primary key
     * @return Result
     */
    public int deleteHrKnowledgeBaseById(Long id);

    List<HrKnowledgeBase> searchAllHrKnowledgeBase(String searchParam, String enterpriseId);
}
