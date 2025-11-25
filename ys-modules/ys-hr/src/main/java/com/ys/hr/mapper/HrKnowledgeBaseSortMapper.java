package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrKnowledgeBaseSort;
import org.apache.ibatis.annotations.*;

/**
 *  Knowledge base classification Mapper Interface
 *
 * @author ys
 * @date 2025-06-04
 */
public interface HrKnowledgeBaseSortMapper extends BaseMapper<HrKnowledgeBaseSort>
{
    /**
     * Query Knowledge base classification
     *
     * @param id  Knowledge base classification primary key
     * @return  Knowledge base classification
     */
    public HrKnowledgeBaseSort selectHrKnowledgeBaseSortById(Long id);

    /**
     * Query Knowledge base classification list
     *
     * @param hrKnowledgeBaseSort  Knowledge base classification
     * @return  Knowledge base classification collection
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
