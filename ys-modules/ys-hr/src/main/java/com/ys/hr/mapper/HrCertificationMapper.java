package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrCertification;

import java.util.List;

/**
 * Certification information Mapper Interface
 *
 * @author ys
 * @date 2025-06-24
 */
public interface HrCertificationMapper extends BaseMapper<HrCertification>
{
    /**
     * Query Certification information
     *
     * @param id Certification information primary key
     * @return Certification information
     */
    public HrCertification selectHrCertificationById(Long id);

    /**
     * Query Certification information list
     *
     * @param hrCertification Certification information
     * @return Certification information collection
     */
    public List<HrCertification> selectHrCertificationList(HrCertification hrCertification);

}
