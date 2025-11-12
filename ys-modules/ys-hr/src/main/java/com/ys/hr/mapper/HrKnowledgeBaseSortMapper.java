package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrKnowledgeBaseSort;
import org.apache.ibatis.annotations.*;

/**
 *  KNOWLEDGE BASE  CLASSIFICATION  Mapper Interface
 *
 * @author ys
 * @date 2025-06-04
 */
public interface HrKnowledgeBaseSortMapper extends BaseMapper<HrKnowledgeBaseSort>
{
    /**
     * Query  KNOWLEDGE BASE  CLASSIFICATION 
     *
     * @param id  KNOWLEDGE BASE  CLASSIFICATION  primary key
     * @return  KNOWLEDGE BASE  CLASSIFICATION 
     */
    public HrKnowledgeBaseSort selectHrKnowledgeBaseSortById(Long id);

    /**
     * Query  KNOWLEDGE BASE  CLASSIFICATION  list
     *
     * @param hrKnowledgeBaseSort  KNOWLEDGE BASE  CLASSIFICATION 
     * @return  KNOWLEDGE BASE  CLASSIFICATION  collection
     */
    public List<HrKnowledgeBaseSort> selectHrKnowledgeBaseSortList(HrKnowledgeBaseSort hrKnowledgeBaseSort);


//    List<HrKnowledgeBaseSort> selectChildrenByParentId(@Param("parentId") Long parentId);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "knowledgeTitles", column = "id",
                    many = @Many(select = "com.ys.hr.mapper.HrKnowledgeBaseMapper.selectByParentId"))
    })
    List<HrKnowledgeBaseSort> selectChildrenByParentId(@Param("parentId") Long parentId, @Param("enterpriseId") String enterpriseId);

    List<HrKnowledgeBaseSort> searchTree(@Param("searchKey") String searchKey, @Param("enterpriseId") String enterpriseId);


    List<HrKnowledgeBaseSort> searchTree2(@Param("sortId") Long sortId, @Param("enterpriseId") String enterpriseId);


}