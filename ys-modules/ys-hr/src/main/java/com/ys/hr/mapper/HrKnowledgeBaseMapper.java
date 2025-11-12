package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrKnowledgeBase;
import com.ys.hr.domain.HrKnowledgeBaseSort;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *  KNOWLEDGE BASE  Mapper Interface
 *
 * @author ys
 * @date 2025-06-04
 */
public interface HrKnowledgeBaseMapper extends BaseMapper<HrKnowledgeBase>
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
