package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrBenefitGroup;

import java.util.List;

/**
 * Table of benefit types Mapper Interface
 *
 * @author ys
 * @date 2025-07-10
 */
public interface HrBenefitGroupMapper extends BaseMapper<HrBenefitGroup>
{
    /**
     * Query Table of benefit types
     *
     * @param benefitGroupId Table of benefit types primary key
     * @return Table of benefit types
     */
    public HrBenefitGroup selectHrBenefitGroupByBenefitGroupId(String benefitGroupId);

    /**
     * Query Table of benefit types list
     *
     * @param hrBenefitGroup Table of benefit types
     * @return Table of benefit types collection
     */
    public List<HrBenefitGroup> selectHrBenefitGroupList(HrBenefitGroup hrBenefitGroup);

}
