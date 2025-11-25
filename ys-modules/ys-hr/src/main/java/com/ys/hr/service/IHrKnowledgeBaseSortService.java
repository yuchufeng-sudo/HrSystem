package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrKnowledgeBaseSort;

import java.util.List;

/**
 * Knowledge base CLASSIFICATION Service Interface
 *
 * @author ys
 * @date 2025-06-04
 */
public interface IHrKnowledgeBaseSortService extends IService<HrKnowledgeBaseSort> {

    /**
     * Query Knowledge base CLASSIFICATION
     *
     * @param id Knowledge base CLASSIFICATION primary key
     * @return Knowledge base CLASSIFICATION
     */
    public HrKnowledgeBaseSort selectHrKnowledgeBaseSortById(Long id);

    /**
     * Query Knowledge base CLASSIFICATION list
     *
     * @param hrKnowledgeBaseSort Knowledge base CLASSIFICATION
     * @return Knowledge base CLASSIFICATION collection
     */
    public List<HrKnowledgeBaseSort> selectHrKnowledgeBaseSortList(HrKnowledgeBaseSort hrKnowledgeBaseSort);

    /**
     * Add Knowledge base CLASSIFICATION
     *
     * @param hrKnowledgeBaseSort Knowledge base CLASSIFICATION
     * @return Result
     */
    public int insertHrKnowledgeBaseSort(HrKnowledgeBaseSort hrKnowledgeBaseSort);

    /**
     * Update Knowledge base CLASSIFICATION
     *
     * @param hrKnowledgeBaseSort Knowledge base CLASSIFICATION
     * @return Result
     */
    public int updateHrKnowledgeBaseSort(HrKnowledgeBaseSort hrKnowledgeBaseSort);

    /**
     * Batch delete Knowledge base CLASSIFICATION
     *
     * @param ids Knowledge base CLASSIFICATION primary keys to be deleted
     * @return Result
     */
    public int deleteHrKnowledgeBaseSortByIds(Long[] ids);

    /**
     * Delete Knowledge base CLASSIFICATION information
     *
     * @param id Knowledge base CLASSIFICATION primary key
     * @return Result
     */
    public int deleteHrKnowledgeBaseSortById(Long id);

    /**
     * Query child classifications based on the parent ID
     *
     * @param parentId Parent ID
     * @return List of child classifications
     */
    List<HrKnowledgeBaseSort> selectChildrenByParentId(Long parentId, String enterpriseId);

    /**
     * Build a tree structure
     *
     * @param parentId Top-level parent ID
     * @return Tree-structured data
     */
    List<HrKnowledgeBaseSort> buildTree(Long parentId, String enterpriseId);

    List<HrKnowledgeBaseSort> searchTree(String searchKey, String enterpriseId);

    List<HrKnowledgeBaseSort> searchTree2(Long sortId, String enterpriseId);
}
