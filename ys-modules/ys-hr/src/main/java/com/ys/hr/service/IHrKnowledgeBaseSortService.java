package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrKnowledgeBaseSort;

import java.util.List;

/**
 * KNOWLEDGE BASE CLASSIFICATION Service Interface
 *
 * @author ys
 * @date 2025-06-04
 */
public interface IHrKnowledgeBaseSortService extends IService<HrKnowledgeBaseSort> {

    /**
     * Query KNOWLEDGE BASE CLASSIFICATION
     *
     * @param id KNOWLEDGE BASE CLASSIFICATION primary key
     * @return KNOWLEDGE BASE CLASSIFICATION
     */
    public HrKnowledgeBaseSort selectHrKnowledgeBaseSortById(Long id);

    /**
     * Query KNOWLEDGE BASE CLASSIFICATION list
     *
     * @param hrKnowledgeBaseSort KNOWLEDGE BASE CLASSIFICATION
     * @return KNOWLEDGE BASE CLASSIFICATION collection
     */
    public List<HrKnowledgeBaseSort> selectHrKnowledgeBaseSortList(HrKnowledgeBaseSort hrKnowledgeBaseSort);

    /**
     * Add KNOWLEDGE BASE CLASSIFICATION
     *
     * @param hrKnowledgeBaseSort KNOWLEDGE BASE CLASSIFICATION
     * @return Result
     */
    public int insertHrKnowledgeBaseSort(HrKnowledgeBaseSort hrKnowledgeBaseSort);

    /**
     * Update KNOWLEDGE BASE CLASSIFICATION
     *
     * @param hrKnowledgeBaseSort KNOWLEDGE BASE CLASSIFICATION
     * @return Result
     */
    public int updateHrKnowledgeBaseSort(HrKnowledgeBaseSort hrKnowledgeBaseSort);

    /**
     * Batch delete KNOWLEDGE BASE CLASSIFICATION
     *
     * @param ids KNOWLEDGE BASE CLASSIFICATION primary keys to be deleted
     * @return Result
     */
    public int deleteHrKnowledgeBaseSortByIds(Long[] ids);

    /**
     * Delete KNOWLEDGE BASE CLASSIFICATION information
     *
     * @param id KNOWLEDGE BASE CLASSIFICATION primary key
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
