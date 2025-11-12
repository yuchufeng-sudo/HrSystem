package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrAgreement;

import java.util.List;

/**
 * agreement Mapper Interface
 *
 * @author ys
 * @date 2025-07-01
 */
public interface HrAgreementMapper extends BaseMapper<HrAgreement>
{
    /**
     * Query agreement
     *
     * @param id agreement primary key
     * @return agreement
     */
    public HrAgreement selectHrAgreementById(Long id);

    /**
     * Query agreement list
     *
     * @param hrAgreement agreement
     * @return agreement collection
     */
    public List<HrAgreement> selectHrAgreementList(HrAgreement hrAgreement);

}
