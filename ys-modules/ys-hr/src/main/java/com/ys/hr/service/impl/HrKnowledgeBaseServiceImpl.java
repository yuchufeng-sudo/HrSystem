package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.hr.domain.HrKnowledgeBaseSort;
import com.ys.hr.mapper.HrKnowledgeBaseSortMapper;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrKnowledgeBaseMapper;
import com.ys.hr.domain.HrKnowledgeBase;
import com.ys.hr.service.IHrKnowledgeBaseService;
import com.ys.common.core.utils.DateUtils;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * KNOWLEDGE BASE Service Implementation
 *
 * @author ys
 * @date 2025-06-04
 */
@Service
public class HrKnowledgeBaseServiceImpl extends ServiceImpl<HrKnowledgeBaseMapper, HrKnowledgeBase>
        implements IHrKnowledgeBaseService {
    @Resource
    private HrKnowledgeBaseSortMapper hrKnowledgeBaseSortService;

    /**
     * Query KNOWLEDGE BASE
     *
     * @param id KNOWLEDGE BASE primary key
     * @return KNOWLEDGE BASE
     */
    @Override
    public HrKnowledgeBase selectHrKnowledgeBaseById(Long id) {
        return baseMapper.selectHrKnowledgeBaseById(id);
    }

    /**
     * Query KNOWLEDGE BASE list
     *
     * @param hrKnowledgeBase KNOWLEDGE BASE
     * @return KNOWLEDGE BASE
     */
    @Override
    public List<HrKnowledgeBase> selectHrKnowledgeBaseList(HrKnowledgeBase hrKnowledgeBase) {
        return baseMapper.selectHrKnowledgeBaseList(hrKnowledgeBase);
    }

    /**
     * Add KNOWLEDGE BASE
     *
     * @param hrKnowledgeBase KNOWLEDGE BASE
     * @return Result
     */
    // @Override
    // public int insertHrKnowledgeBase(HrKnowledgeBase hrKnowledgeBase)
    // {
    // hrKnowledgeBase.setCreateTime(DateUtils.getNowDate());
    // return baseMapper.insert(hrKnowledgeBase);
    // }

    /**
     * Update KNOWLEDGE BASE
     *
     * @param hrKnowledgeBase KNOWLEDGE BASE
     * @return Result
     */
    // @Override
    // public int updateHrKnowledgeBase(HrKnowledgeBase hrKnowledgeBase)
    // {
    // hrKnowledgeBase.setUpdateTime(DateUtils.getNowDate());
    // return baseMapper.updateById(hrKnowledgeBase);
    // }
    @Override
    public int insertHrKnowledgeBase(HrKnowledgeBase hrKnowledgeBase) {
        hrKnowledgeBase.setCreateTime(DateUtils.getNowDate());
        // Set the ancestors field
        Long parentId = hrKnowledgeBase.getParentId();
        if (parentId != null && parentId != 0L) {
            HrKnowledgeBaseSort sort = hrKnowledgeBaseSortService.selectHrKnowledgeBaseSortById(parentId);
            if (sort != null) {
                hrKnowledgeBase.setAncestors(sort.getAncestors() + "," + parentId);
            } else {
                hrKnowledgeBase.setAncestors(parentId.toString());
            }
        } else {
            hrKnowledgeBase.setAncestors("0");
        }
        return baseMapper.insert(hrKnowledgeBase);
    }

    @Override
    public int updateHrKnowledgeBase(HrKnowledgeBase hrKnowledgeBase) {
        hrKnowledgeBase.setUpdateTime(DateUtils.getNowDate());
        // Set the ancestors field
        Long parentId = hrKnowledgeBase.getParentId();
        if (parentId != null && parentId != 0L) {
            HrKnowledgeBaseSort sort = hrKnowledgeBaseSortService.selectHrKnowledgeBaseSortById(parentId);
            if (sort != null) {
                hrKnowledgeBase.setAncestors(sort.getAncestors() + "," + parentId);
            } else {
                hrKnowledgeBase.setAncestors(parentId.toString());
            }
        } else {
            hrKnowledgeBase.setAncestors("0");
        }
        return baseMapper.updateById(hrKnowledgeBase);
    }

    /**
     * Batch delete KNOWLEDGE BASE
     *
     * @param ids KNOWLEDGE BASE primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrKnowledgeBaseByIds(Long[] ids) {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete KNOWLEDGE BASE information
     *
     * @param id KNOWLEDGE BASE primary key
     * @return Result
     */
    @Override
    public int deleteHrKnowledgeBaseById(Long id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public List<HrKnowledgeBase> searchAllHrKnowledgeBase(String searchParam,String enterpriseId) {
        return baseMapper.searchAllHrKnowledgeBase(searchParam,enterpriseId);
    }
}