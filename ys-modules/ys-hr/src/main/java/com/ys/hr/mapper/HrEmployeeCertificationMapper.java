package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrEmployeeCertification;

/**
 * Employee certification issuance Mapper Interface
 *
 * @author ys
 * @date 2025-06-24
 */
public interface HrEmployeeCertificationMapper extends BaseMapper<HrEmployeeCertification>
{
    /**
     * Query Employee certification issuance
     *
     * @param id Employee certification issuance primary key
     * @return Employee certification issuance
     */
    public HrEmployeeCertification selectHrEmployeeCertificationById(String id);
    
    /**
     * Query Employee certification issuance list
     *
     * @param hrEmployeeCertification Employee certification issuance
     * @return Employee certification issuance collection
     */
    public List<HrEmployeeCertification> selectHrEmployeeCertificationList(HrEmployeeCertification hrEmployeeCertification);

}