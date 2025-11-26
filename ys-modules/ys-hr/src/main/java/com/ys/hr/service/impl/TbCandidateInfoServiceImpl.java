package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.*;
import com.ys.hr.mapper.HrQuotaMapper;
import com.ys.hr.mapper.TbCandidateInfoMapper;
import com.ys.hr.service.*;
import com.ys.system.api.domain.SysUser;
import com.ys.utils.email.EmailUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 *  Candidate INFORMATIONServiceBusiness layer processing
 *
 * @author ys
 * @date 2025-05-20
 */
@Service
public class TbCandidateInfoServiceImpl extends ServiceImpl<TbCandidateInfoMapper, HrCandidateInfo> implements ITbCandidateInfoService
{
    @Resource
    private IHrQuestionAnswerService questionAnswerService;

    @Resource
    private IHrJobListingsService jobListingsService;
    /**
     * Query Candidate Information list
     *
     *
     * @param hrCandidateInfo  Candidate Information
     * @return Candidate Information
     */
    @Override
    public List<HrCandidateInfo> selectTbCandidateInfoList(HrCandidateInfo hrCandidateInfo)
    {
        return baseMapper.selectTbCandidateInfoList(hrCandidateInfo);
    }

    @Override
    @Transactional
    public Map<String, Object> candidateCount(HrCandidateInfo hrCandidateInfo) {

        // Step 1: Get basic candidate statistics
        Map<String, Object> baseStats = getBaseCandidateStats(hrCandidateInfo);
        Map<String, Object> resultMap = new HashMap<>(baseStats);

        // Step 2: Get last month's statistics
        Map<String, Object> lastMonthStats = getLastMonthStats(hrCandidateInfo);
        resultMap.putAll(lastMonthStats);

        // Step 3: Get current month's statistics
        Map<String, Object> currentMonthStats = getCurrentMonthStats(hrCandidateInfo);
        resultMap.putAll(currentMonthStats);

        // Step 4: Calculate month-to-month differences
        calculateMonthlyDifferences(resultMap);

        // Step 5: Calculate overall conversion rate
        calculateConversionRate(resultMap);

        // Step 6: Calculate month-to-month conversion rate change
        calculateMonthlyRateChange(resultMap);

        return resultMap;
    }

    /**
     * Retrieves basic candidate count statistics
     * @param hrCandidateInfo Candidate information filter
     * @return Map containing total candidate count and hired count
     */
    private Map<String, Object> getBaseCandidateStats(HrCandidateInfo hrCandidateInfo) {
        Map<String, Object> map = baseMapper.candidateCount(hrCandidateInfo);
        Map<String, Object> result = new HashMap<>();
        result.put("candidatecount", map.get("candidatecount"));
        result.put("hiredcount", map.get("hiredcount"));
        return Collections.unmodifiableMap(result);
    }

    /**
     * Retrieves statistics for the previous month
     * @param hrCandidateInfo Candidate information filter
     * @return Map containing last month's statistics
     */
    private Map<String, Object> getLastMonthStats(HrCandidateInfo hrCandidateInfo) {
        LocalDate today = LocalDate.now();
        YearMonth lastYearMonth = YearMonth.from(today).minusMonths(1);
        return getTimeRangeStats(hrCandidateInfo,
                lastYearMonth.atDay(1),
                lastYearMonth.atEndOfMonth(),
                "LastMonth"
        );
    }

    /**
     * Retrieves statistics for the current month
     * @param hrCandidateInfo Candidate information filter
     * @return Map containing current month's statistics
     */
    private Map<String, Object> getCurrentMonthStats(HrCandidateInfo hrCandidateInfo) {
        YearMonth currentYearMonth = YearMonth.from(LocalDate.now());
        return getTimeRangeStats(hrCandidateInfo,
                currentYearMonth.atDay(1),
                currentYearMonth.atEndOfMonth(),
                "NowMonth"
        );
    }

    /**
     * Generic method to retrieve statistics for a given date range
     * @param hrCandidateInfo Candidate information filter
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @param prefix Prefix for the result keys
     * @return Map containing statistics for the specified period
     */
    private Map<String, Object> getTimeRangeStats(HrCandidateInfo hrCandidateInfo,
                                                  LocalDate startDate, LocalDate endDate, String prefix) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        hrCandidateInfo.setStartTime(startDate.format(formatter));
        hrCandidateInfo.setEndTime(endDate.format(formatter));
        Map<String, Object> stats = baseMapper.candidateCountByLastMonth(hrCandidateInfo);

        // JDK 1.8 compatible version without Map.of()
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(prefix + "CandidateCount", stats.get("candidatecount"));
        resultMap.put(prefix + "InterviewCount", stats.get("interviewcount"));
        resultMap.put(prefix + "ScreeningCount", stats.get("screeningcount"));
        resultMap.put(prefix + "HiredCount", stats.get("hiredcount"));
        return resultMap;
    }

    /**
     * Calculates differences between current and previous month statistics
     * @param resultMap Reference to the result map where differences will be stored
     */
    private void calculateMonthlyDifferences(Map<String, Object> resultMap) {
        Long candidateNow = (Long) resultMap.get("NowMonthCandidateCount");
        Long candidateLast = (Long) resultMap.get("LastMonthCandidateCount");
        Long interviewNow = (Long) resultMap.get("NowMonthInterviewCount");
        Long interviewLast = (Long) resultMap.get("LastMonthInterviewCount");
        Long screeningNow = (Long) resultMap.get("NowMonthScreeningCount");
        Long screeningLast = (Long) resultMap.get("LastMonthScreeningCount");

        resultMap.put("MonthCountByCandidateCount", candidateNow - candidateLast);
        resultMap.put("MonthCountByInterviewCount", interviewNow - interviewLast);
        resultMap.put("MonthCountByScreeningCount", screeningNow - screeningLast);
    }

    /**
     * Calculates overall conversion rate (hired candidates / total candidates)
     * @param resultMap Reference to the result map where rate will be stored
     */
    private void calculateConversionRate(Map<String, Object> resultMap) {
        Long candidateCount = (Long) resultMap.get("candidatecount");
        Long hiredCount = (Long) resultMap.get("hiredcount");

        if (candidateCount == 0) {
            resultMap.put("ConversionRate", "0.00%");
        } else {
            BigDecimal rate = new BigDecimal(hiredCount)
                    .multiply(new BigDecimal(100))
                    .divide(new BigDecimal(candidateCount), 4, RoundingMode.HALF_UP);
            resultMap.put("ConversionRate", rate.setScale(2, RoundingMode.HALF_UP) + "%");
        }
    }

    /**
     * Calculates month-to-month conversion rate change and trend
     * @param resultMap Reference to the result map where change will be stored
     */
    private void calculateMonthlyRateChange(Map<String, Object> resultMap) {
        Long candidateLast = (Long) resultMap.get("LastMonthCandidateCount");
        Long hiredLast = (Long) resultMap.get("LastMonthHiredCount");
        Long candidateNow = (Long) resultMap.get("NowMonthCandidateCount");
        Long hiredNow = (Long) resultMap.get("NowMonthHiredCount");

        String trend;
        double absoluteChange = 0.0;

        if (candidateLast != 0 && candidateNow != 0) {
            double lastMonthRate = (hiredLast.doubleValue() / candidateLast) * 100;
            double currentMonthRate = (hiredNow.doubleValue() / candidateNow) * 100;
            double rateChange = currentMonthRate - lastMonthRate;

            absoluteChange = Math.abs(rateChange);
            trend = rateChange > 0 ? "+" : (rateChange < 0 ? "-" : "+");
        } else {
            trend = " ";
        }

        DecimalFormat df = new DecimalFormat("#.##");
        resultMap.put("MonthConversionRate", trend + df.format(absoluteChange));
    }

    @Override
    public HrEnterprise selectEid(String userEnterpriseId) {
        return baseMapper.seleEid(userEnterpriseId);
    }

    @Override
    @Transactional
    public boolean applyCandidateInfo(HrCandidateInfo candidateInfo) {
        candidateInfo.setCreateTime(DateUtils.getNowDate());
        candidateInfo.setCandidateStatus("0");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(candidateInfo.getFilesValue());
            candidateInfo.setFilesJson(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        boolean save = save(candidateInfo);
        batchHrDocument(candidateInfo,null);
        List<HrQuestionAnswer> answers = candidateInfo.getAnswers();
        for (HrQuestionAnswer answer : answers) {
            answer.setCandidateId(candidateInfo.getCandidateId());
            answer.setEnterpriseId(candidateInfo.getEnterpriseId());
        }
        questionAnswerService.saveBatch(answers);
        candidateInfo.setCreateTime(DateUtils.getNowDate());
        if(ObjectUtils.isNotEmpty(candidateInfo.getJobListingsId())){
            HrJobListings hrJobListings = jobListingsService.selectHrJobListingsById(candidateInfo.getJobListingsId());
            if(ObjectUtils.isNotEmpty(hrJobListings.getEnterpriseId())){
                List<SysUser> hrUsers = questionAnswerService.selectHrList(hrJobListings.getEnterpriseId());
                if(ObjectUtils.isNotEmpty(hrUsers)){
                    for (SysUser hrUser : hrUsers) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("HrName", hrUser.getUserName());
                        map.put("JobTitle", hrJobListings.getTitle());
                        map.put("FullName", candidateInfo.getCandidateName());
                        map.put("CandidateEmail", candidateInfo.getContactEmail());
                        emailUtils.sendEmailByTemplate(map,hrUser.getEmail(),"Candidate-Application");
                    }
                }
            }
        }
        return save;
    }

    @Autowired
    private IHrDocumentService documentService;

    @Autowired
    private IHrDocumentShareService documentShareService;

    @Transactional
    @Override
    public boolean insertHrCandidateInfo(HrCandidateInfo hrCandidateInfo) {
        hrCandidateInfo.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        hrCandidateInfo.setCreateTime(DateUtils.getNowDate());
        hrCandidateInfo.setCandidateStatus("0");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(hrCandidateInfo.getFilesValue());
            hrCandidateInfo.setFilesJson(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        boolean save = save(hrCandidateInfo);
        batchHrDocument(hrCandidateInfo,SecurityUtils.getUserId());
        return save;
    }

    @Autowired
    private IHrEmployeesService hrEmployeesService;

    @Autowired
    private HrQuotaMapper hrQuotaMapper;

    @Resource
    private EmailUtils emailUtils;

    @Transactional
    @Override
    public boolean updateHrCandidateInfo(HrCandidateInfo hrCandidateInfo) {
        HrCandidateInfo candidateInfo = new HrCandidateInfo();
        candidateInfo.setCandidateId(hrCandidateInfo.getCandidateId());
        List<HrCandidateInfo> hrCandidateInfos1 = selectTbCandidateInfoList(candidateInfo);
        if(ObjectUtils.isEmpty(hrCandidateInfos1)){
            throw new ServiceException("The Candidate anomalies!");
        }
        if("1".equals(hrCandidateInfo.getCandidateStatus())){
                Map<String, Object> map = new HashMap<>();
                HrEnterprise Company = selectEid(SecurityUtils.getUserEnterpriseId());
                HrEmployees hrEmployees = hrEmployeesService.selectHrEmployeesByUserId(SecurityUtils.getUserId());
                if(ObjectUtils.isNotEmpty(Company)){
                    map.put("CompanyName",Company.getEnterpriseName());
                }else{
                    map.put("CompanyName","No details yet");
                }
                map.put("JobTitle",hrCandidateInfo.getPostName());
                map.put("FirstName",hrCandidateInfo.getCandidateName());
                emailUtils.sendEmailByTemplate(map,hrCandidateInfo.getContactEmail(),"Candidate-Shortlisted");
        }
        if("2".equals(hrCandidateInfo.getCandidateStatus())){
            if(hrCandidateInfo.getContactEmail()!=null){
                Map<String, Object> map = new HashMap<>();
                HrCandidateInfo info = new HrCandidateInfo();
                info.setCandidateId(hrCandidateInfo.getCandidateId());
                List<HrCandidateInfo> hrCandidateInfos = selectTbCandidateInfoList(info);
                HrEnterprise Company = selectEid(SecurityUtils.getUserEnterpriseId());
                HrEmployees hrEmployees = hrEmployeesService.selectHrEmployeesByUserId(SecurityUtils.getUserId());
                if(ObjectUtils.isNotEmpty( Company)){
                    map.put("CompanyName",Company.getEnterpriseName());
                }else{
                    map.put("CompanyName","No details yet");
                }
                if(ObjectUtils.isNotEmpty(hrCandidateInfo.getPostName())){
                    map.put("JobTitle",hrCandidateInfo.getPostName());
                }else{
                    map.put("JobTitle","No details yet");
                }
                Date interviewTime = hrCandidateInfo.getInterviewTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String datePart = dateFormat.format(interviewTime);
                String timePart = timeFormat.format(interviewTime);
                map.put("FirstName",hrCandidateInfo.getCandidateName());
                map.put("InterviewDate",datePart);
                map.put("InterviewTime",timePart);
                map.put("InterviewLocation",hrCandidateInfo.getInterviewLocation());
                emailUtils.sendEmailByTemplate(map,hrCandidateInfo.getContactEmail(),"Interview");
            }
        }
        if("3".equals(hrCandidateInfo.getCandidateStatus())){
            if(hrCandidateInfo.getIsEmail()!=null&&hrCandidateInfo.getIsEmail()){
                Map<String, Object> map = new HashMap<>();
                HrEnterprise Company = selectEid(SecurityUtils.getUserEnterpriseId());
                HrEmployees hrEmployees = hrEmployeesService.selectHrEmployeesByUserId(SecurityUtils.getUserId());
                if(ObjectUtils.isNotEmpty( Company)){
                    map.put("CompanyName",Company.getEnterpriseName());
                    map.put("SupportEmail",Company.getContactEmail());
                }else{
                    map.put("CompanyName","No details yet");
                    map.put("SupportEmail","No details yet");
                }
                if(ObjectUtils.isNotEmpty( hrEmployees)){
                    map.put("HrName",hrEmployees.getFullName());
                    map.put("HrEmail",hrEmployees.getEmail());
                }else{
                    map.put("HrName","No details yet");
                    map.put("HrEmail","No details yet");
                }
                map.put("FirstName",hrCandidateInfo.getCandidateName());
                emailUtils.sendEmailByTemplate(map,hrCandidateInfo.getContactEmail(),"CandidateHired");
            }
        }
        hrCandidateInfo.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (hrCandidateInfo.getFilesValue()!=null) {
                String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(hrCandidateInfo.getFilesValue());
                hrCandidateInfo.setFilesJson(json);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        batchHrDocument(hrCandidateInfo,SecurityUtils.getUserId());
        return updateById(hrCandidateInfo);
    }

    @Override
    public void sendEmailHired(HrCandidateInfo hrCandidateInfo) {
        Map<String, Object> map = new HashMap<>();
        HrEnterprise Company = selectEid(SecurityUtils.getUserEnterpriseId());
        HrEmployees hrEmployees = hrEmployeesService.selectHrEmployeesByUserId(SecurityUtils.getUserId());
        if(ObjectUtils.isNotEmpty( Company)){
            map.put("CompanyName",Company.getEnterpriseName());
            map.put("SupportEmail",Company.getContactEmail());
        }else{
            map.put("CompanyName","No details yet");
            map.put("SupportEmail","No details yet");
        }
        if(ObjectUtils.isNotEmpty( hrEmployees)){
            map.put("HrName",hrEmployees.getFullName());
            map.put("HrEmail",hrEmployees.getEmail());
        }else{
            map.put("HrName","No details yet");
            map.put("HrEmail","No details yet");
        }
        map.put("FirstName",hrCandidateInfo.getCandidateName());
        emailUtils.sendEmailByTemplate(map,hrCandidateInfo.getContactEmail(),"CandidateHired");
    }

    @Override
    public void sendEmail(HrCandidateInfo hrCandidateInfo) {
        Map<String, Object> map = new HashMap<>();
        HrEnterprise Company = selectEid(SecurityUtils.getUserEnterpriseId());
        if(ObjectUtils.isNotEmpty( Company)){
            map.put("CompanyName",Company.getEnterpriseName());
        }else{
            map.put("CompanyName","No details yet");
        }
        map.put("FirstName",hrCandidateInfo.getCandidateName());
        map.put("InviteUrl",hrCandidateInfo.getInviteUrl());
        map.put("HrisToolName","Shiftcare HR");
        emailUtils.sendEmailByTemplate(map, hrCandidateInfo.getContactEmail(), "Invite");
    }

    private void batchHrDocument(HrCandidateInfo hrCandidateInfo,Long userId) {
        List<HrDocument> filesValue = hrCandidateInfo.getFilesValue();
        Long candidateId = hrCandidateInfo.getCandidateId();
        if (filesValue!=null){
            documentShareService.deleteByUploadCandidateId(candidateId);
            QueryWrapper<HrDocument> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("upload_candidate_id",candidateId);
            documentService.remove(queryWrapper);
            if (!filesValue.isEmpty()) {
                for (HrDocument hrDocument : filesValue) {
                    hrDocument.setEnterpriseId(hrCandidateInfo.getEnterpriseId());
                    hrDocument.setUploadDate(DateUtils.getNowDate());
                    hrDocument.setUploadCandidateId(candidateId);
                    hrDocument.setUploadUserId(userId);
                }
                documentService.saveBatch(filesValue);
            }
        }
    }
}
