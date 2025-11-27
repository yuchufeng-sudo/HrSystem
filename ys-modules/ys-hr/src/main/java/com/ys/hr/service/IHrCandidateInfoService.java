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
public interface IHrCandidateInfoService extends IService<HrCandidateInfo>
{

    /**
     * Query Candidate Information list
     *
     * @param hrCandidateInfo  Candidate Information
     * @return Candidate INFORMATIONSet
     */
    public List<HrCandidateInfo> selectHrCandidateInfoList(HrCandidateInfo hrCandidateInfo);

    Map<String,Object> candidateCount(HrCandidateInfo hrCandidateInfo);

    HrEnterprise selectEid(String userEnterpriseId);

    boolean applyCandidateInfo(HrCandidateInfo candidateInfo);

    @Transactional
    boolean insertHrCandidateInfo(HrCandidateInfo candidateInfo);

    @Transactional
    boolean updateHrCandidateInfo(HrCandidateInfo candidateInfo);

    @Transactional
    void sendEmailHired(HrCandidateInfo hrCandidateInfo);

    @Transactional
    void sendEmail(HrCandidateInfo hrCandidateInfo);
}
