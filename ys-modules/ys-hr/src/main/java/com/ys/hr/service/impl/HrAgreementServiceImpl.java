package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrAgreementMapper;
import com.ys.hr.domain.HrAgreement;
import com.ys.hr.service.IHrAgreementService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * agreement Service Implementation
 *
 * @author ys
 * @date 2025-07-01
 */
@Service
public class HrAgreementServiceImpl extends ServiceImpl<HrAgreementMapper, HrAgreement> implements IHrAgreementService
{

    /**
     * Query agreement
     *
     * @param id agreement primary key
     * @return agreement
     */
    @Override
    public HrAgreement selectHrAgreementById(Long id)
    {
        return baseMapper.selectHrAgreementById(id);
    }

    /**
     * Query agreement list
     *
     * @param hrAgreement agreement
     * @return agreement
     */
    @Override
    public List<HrAgreement> selectHrAgreementList(HrAgreement hrAgreement)
    {
        return baseMapper.selectHrAgreementList(hrAgreement);
    }

    /**
     * Add agreement
     *
     * @param hrAgreement agreement
     * @return Result
     */
    @Override
    public int insertHrAgreement(HrAgreement hrAgreement)
    {
        hrAgreement.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrAgreement);
    }

    /**
     * Update agreement
     *
     * @param hrAgreement agreement
     * @return Result
     */
    @Override
    public int updateHrAgreement(HrAgreement hrAgreement)
    {
        hrAgreement.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrAgreement);
    }

    /**
     * Batch delete agreement
     *
     * @param ids agreement primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrAgreementByIds(Long[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete agreement information
     *
     * @param id agreement primary key
     * @return Result
     */
    @Override
    public int deleteHrAgreementById(Long id)
    {
        return baseMapper.deleteById(id);
    }
}
