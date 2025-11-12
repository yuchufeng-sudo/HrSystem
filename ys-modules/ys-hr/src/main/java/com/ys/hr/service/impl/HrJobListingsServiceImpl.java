package com.ys.hr.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.hr.domain.HrJobListingsQuestion;
import com.ys.hr.service.IHrJobListingsQuestionService;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrJobListingsMapper;
import com.ys.hr.domain.HrJobListings;
import com.ys.hr.service.IHrJobListingsService;
import com.ys.common.core.utils.DateUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Job Listings Service Implementation
 *
 * @author ys
 * @date 2025-09-25
 */
@Service
public class HrJobListingsServiceImpl extends ServiceImpl<HrJobListingsMapper, HrJobListings> implements IHrJobListingsService
{

    @Resource
    private IHrJobListingsQuestionService hrJobListingsQuestionService;

    /**
     * Query Job Listings
     *
     * @param id Job Listings primary key
     * @return Job Listings
     */
    @Override
    public HrJobListings selectHrJobListingsById(String id)
    {
        HrJobListings hrJobListings = baseMapper.selectHrJobListingsById(id);
        HrJobListingsQuestion question = new HrJobListingsQuestion();
        question.setJobListingsId(id);
        List<HrJobListingsQuestion> hrJobListingsQuestions = hrJobListingsQuestionService.selectHrJobListingsQuestionList(question);
        hrJobListings.setQuestions(hrJobListingsQuestions);
        return hrJobListings;
    }

    /**
     * Query Job Listings list
     *
     * @param hrJobListings Job Listings
     * @return Job Listings
     */
    @Override
    public List<HrJobListings> selectHrJobListingsList(HrJobListings hrJobListings)
    {
        return baseMapper.selectHrJobListingsList(hrJobListings);
    }

    /**
     * Add Job Listings
     *
     * @param hrJobListings Job Listings
     * @return Result
     */
    @Override
    @Transactional
    public int insertHrJobListings(HrJobListings hrJobListings)
    {
        hrJobListings.setCreateTime(DateUtils.getNowDate());
        int insert = baseMapper.insert(hrJobListings);
        if (hrJobListings.getQuestions().isEmpty()) {
            batchQuestion(hrJobListings);
        }
        return insert;
    }

    /**
     * Update Job Listings
     *
     * @param hrJobListings Job Listings
     * @return Result
     */
    @Override
    @Transactional
    public int updateHrJobListings(HrJobListings hrJobListings)
    {
        hrJobListings.setUpdateTime(DateUtils.getNowDate());
        QueryWrapper<HrJobListingsQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_listings_id",hrJobListings.getId());
        hrJobListingsQuestionService.remove(queryWrapper);
        batchQuestion(hrJobListings);
        return baseMapper.updateById(hrJobListings);
    }

    public void batchQuestion(HrJobListings hrJobListings){
        List<HrJobListingsQuestion> questions = hrJobListings.getQuestions();
        int i = 0;
        for (HrJobListingsQuestion question : questions) {
            question.setEnterpriseId(hrJobListings.getEnterpriseId());
            question.setJobListingsId(hrJobListings.getId());
            question.setCreateTime(DateUtils.getNowDate());
            question.setQuestionIndex(i);
            i++;
        }
        hrJobListingsQuestionService.saveBatch(questions);
    }

    /**
     * Batch delete Job Listings
     *
     * @param ids Job Listings primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrJobListingsByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Job Listings information
     *
     * @param id Job Listings primary key
     * @return Result
     */
    @Override
    public int deleteHrJobListingsById(String id)
    {
        return baseMapper.deleteById(id);
    }
}
