package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrCandidateInfo;
import com.ys.hr.domain.HrEnterprise;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *  Candidate INFORMATIONMapper Interface
 *
 * @author ys
 * @date 2025-05-20
 */
public interface HrCandidateInfoMapper extends BaseMapper<HrCandidateInfo>
{
    /**
     * Query Candidate Information list
     *
     * @param hrCandidateInfo  Candidate Information
     * @return Candidate INFORMATIONSet
     */
    public List<HrCandidateInfo> selectHrCandidateInfoList(HrCandidateInfo hrCandidateInfo);

    Map<String,Object> candidateCount(HrCandidateInfo hrCandidateInfo);

    Map<String,Object> candidateCountByLastMonth(HrCandidateInfo hrCandidateInfo);

    HrEnterprise seleEid(@Param("userEnterpriseId") String userEnterpriseId);
}
