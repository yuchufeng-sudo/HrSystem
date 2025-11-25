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
 * Knowledge base Service Implementation
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
     * Query Knowledge base
     *
     * @param id Knowledge base primary key
     * @return Knowledge base
     */
    @Override
    public HrKnowledgeBase selectHrKnowledgeBaseById(Long id) {
        return baseMapper.selectHrKnowledgeBaseById(id);
    }

    /**
     * Query Knowledge base list
     *
     * @param hrKnowledgeBase Knowledge base
     * @return Knowledge base
     */
    @Override
    public List<HrKnowledgeBase> selectHrKnowledgeBaseList(HrKnowledgeBase hrKnowledgeBase) {
        return baseMapper.selectHrKnowledgeBaseList(hrKnowledgeBase);
    }

    /**
     * Add Knowledge base
     *
     * @param hrKnowledgeBase Knowledge base
     * @return Result
     */
    // @Override
    // public int insertHrKnowledgeBase(HrKnowledgeBase hrKnowledgeBase)
    // {
    // hrKnowledgeBase.setCreateTime(DateUtils.getNowDate());
    // return baseMapper.insert(hrKnowledgeBase);
    // }

    /**
     * Update Knowledge base
     *
     * @param hrKnowledgeBase Knowledge base
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
     * Batch delete Knowledge base
     *
     * @param ids Knowledge base primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrKnowledgeBaseByIds(Long[] ids) {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Knowledge base information
     *
     * @param id Knowledge base primary key
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
