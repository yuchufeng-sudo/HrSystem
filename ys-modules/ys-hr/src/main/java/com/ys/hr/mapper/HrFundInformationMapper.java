package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrFundInformation;

/**
 * Comprehensive fund information Mapper Interface
 *
 * @author ys
 * @date 2025-10-17
 */
public interface HrFundInformationMapper extends BaseMapper<HrFundInformation>
{

    /**
     * Query Comprehensive fund information list
     *
     * @param hrFundInformation Comprehensive fund information
     * @return Comprehensive fund information collection
     */
    public List<HrFundInformation> selectHrFundInformationList(HrFundInformation hrFundInformation);

}
