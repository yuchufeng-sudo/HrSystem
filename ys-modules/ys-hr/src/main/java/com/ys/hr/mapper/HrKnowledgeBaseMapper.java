package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrKnowledgeBase;
import com.ys.hr.domain.HrKnowledgeBaseSort;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *  Knowledge base  Mapper Interface
 *
 * @author ys
 * @date 2025-06-04
 */
public interface HrKnowledgeBaseMapper extends BaseMapper<HrKnowledgeBase>
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

    List<HrKnowledgeBase> searchAllHrKnowledgeBase(String searchParam,String enterpriseId);

    @Select("SELECT id AS title_id, knowledge_contents,knowledge_title FROM hr_knowledge_base WHERE parent_id = #{parentId}")
    List<HrKnowledgeBaseSort.KnowledgeTitle> selectByParentId(@Param("parentId") Long parentId);

    List<HrKnowledgeBase> selectKnowledgeBasesBySearchKey(String searchKey);

    @Select("SELECT id AS title_id, knowledge_contents,knowledge_title FROM hr_knowledge_base " +
            "WHERE parent_id = #{parentId} AND enterprise_id = #{enterpriseId}")
    List<HrKnowledgeBaseSort.KnowledgeTitle> selectByParentIdAndEnterpriseId(
            @Param("parentId") Long parentId,
            @Param("enterpriseId") String enterpriseId);
}
