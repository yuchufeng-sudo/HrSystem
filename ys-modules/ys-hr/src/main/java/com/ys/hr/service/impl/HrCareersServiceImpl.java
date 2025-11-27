package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.model.LoginUser;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrCareersMapper;
import com.ys.hr.domain.HrCareers;
import com.ys.hr.service.IHrCareersService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Company careers information Service Implementation
 *
 * @author ys
 * @date 2025-09-27
 */
@Service
public class HrCareersServiceImpl extends ServiceImpl<HrCareersMapper, HrCareers> implements IHrCareersService
{

    /**
     * Query Company careers information
     *
     * @param id Company careers information primary key
     * @return Company careers information
     */
    @Override
    public HrCareers selectHrCareersById(String id)
    {
        return baseMapper.selectHrCareersById(id);
    }

    /**
     * Query Company careers information list
     *
     * @param hrCareers Company careers information
     * @return Company careers information
     */
    @Override
    public List<HrCareers> selectHrCareersList(HrCareers hrCareers)
    {
        return baseMapper.selectHrCareersList(hrCareers);
    }

    /**
     * Add Company careers information
     *
     * @param hrCareers Company careers information
     * @return Result
     */
    @Override
    public int insertHrCareers(HrCareers hrCareers)
    {
        hrCareers.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrCareers);
    }

    /**
     * Update Company careers information
     *
     * @param hrCareers Company careers information
     * @return Result
     */
    @Override
    public int updateHrCareers(HrCareers hrCareers)
    {
        hrCareers.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrCareers);
    }

    /**
     * Batch delete Company careers information
     *
     * @param ids Company careers information primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrCareersByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Company careers information information
     *
     * @param id Company careers information primary key
     * @return Result
     */
    @Override
    public int deleteHrCareersById(String id)
    {
        return baseMapper.deleteById(id);
    }

    @Override


    // In HrCareersServiceImpl
    public HrCareers getOrCreateEnterpriseCareer(String enterpriseId) {
        HrCareers query = new HrCareers();
        query.setEnterpriseId(enterpriseId);
        List<HrCareers> existing = selectHrCareersList(query);

        if (existing.isEmpty()) {
            LoginUser user = SecurityUtils.getLoginUser();
            HrCareers newCareer = HrCareers.builder()
                    .enterpriseId(enterpriseId)
                    .careersName(user.getEnterpriseName())
                    .logo(user.getEnterpriseLogo())
                    .build();
            insertHrCareers(newCareer);
            return newCareer;
        }
        return existing.get(0);
    }
}
