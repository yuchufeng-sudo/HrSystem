package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrCandidateInfo;
import com.ys.hr.domain.HrEnterprise;
import com.ys.hr.domain.HrPosition;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;



/**
 *  Candidate INFORMATIONService Interface
 *
 * @author ys
 * @date 2025-05-20
 */
public interface ITbCandidateInfoService extends IService<HrCandidateInfo>
{

    /**
     * Query Candidate Information list
     *
     * @param tbCandidateInfo  Candidate Information
     * @return  Candidate INFORMATIONSet
     */
    public List<HrCandidateInfo> selectTbCandidateInfoList(HrCandidateInfo tbCandidateInfo);

    Map<String,Object> candidateCount(HrCandidateInfo tbCandidateInfo);

    List<HrCandidateInfo> selectTbCandidateInfoListByStatus(HrCandidateInfo hrCandidateInfo);

    List<HrCandidateInfo> getCandidateInfoList(String userEnterpriseId);

    HrEnterprise selectEid(String userEnterpriseId);

    boolean applyCandidateInfo(HrCandidateInfo candidateInfo);

    @Transactional
    boolean insertHrCandidateInfo(HrCandidateInfo candidateInfo);

    @Transactional
    boolean updateHrCandidateInfo(HrCandidateInfo candidateInfo);
}
