package com.ys.hr.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.hr.domain.HrKnowledgeBase;
import com.ys.hr.mapper.HrKnowledgeBaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrKnowledgeBaseSortMapper;
import com.ys.hr.domain.HrKnowledgeBaseSort;
import com.ys.hr.service.IHrKnowledgeBaseSortService;
import com.ys.common.core.utils.DateUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  KNOWLEDGE BASE  CLASSIFICATION  Service Implementation
 *
 * @author ys
 * @date 2025-06-04
 */
@Service
public class HrKnowledgeBaseSortServiceImpl extends ServiceImpl<HrKnowledgeBaseSortMapper, HrKnowledgeBaseSort> implements IHrKnowledgeBaseSortService
{
    @Resource
    private HrKnowledgeBaseMapper hrKnowledgeBaseMapper;


    @Resource
    private HrKnowledgeBaseSortMapper knowledgeBaseSortMapper;


    /**
     * Query  KNOWLEDGE BASE  CLASSIFICATION 
     *
     * @param id  KNOWLEDGE BASE  CLASSIFICATION  primary key
     * @return  KNOWLEDGE BASE  CLASSIFICATION 
     */
    @Override
    public HrKnowledgeBaseSort selectHrKnowledgeBaseSortById(Long id)
    {
        return baseMapper.selectHrKnowledgeBaseSortById(id);
    }

    /**
     * Query  KNOWLEDGE BASE  CLASSIFICATION  list
     *
     * @param hrKnowledgeBaseSort  KNOWLEDGE BASE  CLASSIFICATION 
     * @return  KNOWLEDGE BASE  CLASSIFICATION 
     */
    @Override
    public List<HrKnowledgeBaseSort> selectHrKnowledgeBaseSortList(HrKnowledgeBaseSort hrKnowledgeBaseSort)
    {
        return baseMapper.selectHrKnowledgeBaseSortList(hrKnowledgeBaseSort);
    }

    /**
     * Add  KNOWLEDGE BASE  CLASSIFICATION 
     *
     * @param hrKnowledgeBaseSort  KNOWLEDGE BASE  CLASSIFICATION 
     * @return Result
     */
    @Override
    public int insertHrKnowledgeBaseSort(HrKnowledgeBaseSort hrKnowledgeBaseSort)
    {
        hrKnowledgeBaseSort.setCreateTime(DateUtils.getNowDate());
        setLevel(hrKnowledgeBaseSort);
        return baseMapper.insert(hrKnowledgeBaseSort);
    }

    private void setLevel(HrKnowledgeBaseSort hrKnowledgeBaseSort) {
        Long parentId = hrKnowledgeBaseSort.getParentId();
        if (parentId==0L){
            hrKnowledgeBaseSort.setLevel(0);
            hrKnowledgeBaseSort.setAncestors(String.valueOf(parentId));
        }
        else {
            HrKnowledgeBaseSort classify = baseMapper.selectHrKnowledgeBaseSortById(parentId);

            hrKnowledgeBaseSort.setLevel(classify.getLevel()+1);
            hrKnowledgeBaseSort.setAncestors(classify.getAncestors() + "," + hrKnowledgeBaseSort.getParentId());
        }
    }

    /**
     * Update  KNOWLEDGE BASE  CLASSIFICATION 
     *
     * @param hrKnowledgeBaseSort  KNOWLEDGE BASE  CLASSIFICATION 
     * @return Result
     */
    @Override
    public int updateHrKnowledgeBaseSort(HrKnowledgeBaseSort hrKnowledgeBaseSort)
    {
        hrKnowledgeBaseSort.setUpdateTime(DateUtils.getNowDate());
        setLevel(hrKnowledgeBaseSort);
        return baseMapper.updateById(hrKnowledgeBaseSort);
    }

    /**
     * Batch delete  KNOWLEDGE BASE  CLASSIFICATION 
     *
     * @param ids  KNOWLEDGE BASE  CLASSIFICATION  primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrKnowledgeBaseSortByIds(Long[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete  KNOWLEDGE BASE  CLASSIFICATION  information
     *
     * @param id  KNOWLEDGE BASE  CLASSIFICATION  primary key
     * @return Result
     */
    @Override
    public int deleteHrKnowledgeBaseSortById(Long id)
    {
        return baseMapper.deleteById(id);
    }



    @Override
    public List<HrKnowledgeBaseSort> selectChildrenByParentId(Long parentId, String enterpriseId) {
        return baseMapper.selectChildrenByParentId(parentId, enterpriseId);
    }


    @Override
    public List<HrKnowledgeBaseSort> buildTree(Long parentId, String enterpriseId) {
        List<HrKnowledgeBaseSort> tree = new ArrayList<>();
        List<HrKnowledgeBaseSort> nodes = this.selectChildrenByParentId(parentId, enterpriseId);

        for (HrKnowledgeBaseSort node : nodes) {
            node.setChildren(this.buildTree(node.getId(), enterpriseId));

            // 确保获取知识内容时也使用企业ID过滤
            List<HrKnowledgeBaseSort.KnowledgeTitle> titles = hrKnowledgeBaseMapper.selectByParentIdAndEnterpriseId(node.getId(), enterpriseId);
            node.setKnowledgeTitles(titles);

            tree.add(node);
        }

        return tree;
    }

    @Override
    public List<HrKnowledgeBaseSort> searchTree(String searchKey, String enterpriseId) {
        return knowledgeBaseSortMapper.searchTree(searchKey, enterpriseId);
    }

    @Override
    public List<HrKnowledgeBaseSort> searchTree2(Long sortId, String enterpriseId) {
        return knowledgeBaseSortMapper.searchTree2(sortId, enterpriseId);
    }
}