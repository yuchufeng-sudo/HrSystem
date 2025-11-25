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
public interface TbCandidateInfoMapper extends BaseMapper<HrCandidateInfo>
{
    /**
     * Query Candidate Information list
     *
     * @param tbCandidateInfo  Candidate Information
     * @return  Candidate INFORMATIONSet
     */
    public List<HrCandidateInfo> selectTbCandidateInfoList(HrCandidateInfo tbCandidateInfo);

    Map<String,Object> candidateCount(HrCandidateInfo tbCandidateInfo);

    Map<String,Object> candidateCountByLastMonth(HrCandidateInfo tbCandidateInfo);

    List<HrCandidateInfo> selectTbCandidateInfoListByStatus(HrCandidateInfo hrCandidateInfo);

    List<HrCandidateInfo> selectCandidateInfoList(String userEnterpriseId);

    HrEnterprise seleEid(@Param("userEnterpriseId") String userEnterpriseId);
}
