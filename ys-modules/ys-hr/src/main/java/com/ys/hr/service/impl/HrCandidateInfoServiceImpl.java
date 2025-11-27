package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.*;
import com.ys.hr.enums.CandidateStatus;
import com.ys.hr.mapper.HrCandidateInfoMapper;
import com.ys.hr.mapper.HrQuotaMapper;
import com.ys.hr.service.*;
import com.ys.hr.service.email.CandidateEmailService;
import com.ys.system.api.domain.SysUser;
import com.ys.utils.email.EmailUtils;
import com.ys.utils.json.JsonUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Candidate Information Service Implementation
 *
 * @author ys
 * @date 2025-05-20
 */
@Service
public class HrCandidateInfoServiceImpl extends ServiceImpl<HrCandidateInfoMapper, HrCandidateInfo>
        implements IHrCandidateInfoService {

    private static final Logger log = LoggerFactory.getLogger(HrCandidateInfoServiceImpl.class);

    @Resource
    private IHrQuestionAnswerService questionAnswerService;

    @Resource
    private IHrJobListingsService jobListingsService;

    @Autowired
    private IHrDocumentService documentService;

    @Autowired
    private IHrDocumentShareService documentShareService;

    @Autowired
    private IHrEmployeesService hrEmployeesService;

    @Autowired
    private HrQuotaMapper hrQuotaMapper;

    @Resource
    private EmailUtils emailUtils;

    @Autowired
    private CandidateEmailService candidateEmailService;

    /**
     * Query Candidate Information list
     *
     * @param hrCandidateInfo Candidate Information filter
     * @return List of Candidate Information
     */
    @Override
    public List<HrCandidateInfo> selectHrCandidateInfoList(HrCandidateInfo hrCandidateInfo) {
        return baseMapper.selectHrCandidateInfoList(hrCandidateInfo);
    }

    /**
     * Get candidate statistics with monthly comparisons
     *
     * @param hrCandidateInfo Candidate Information filter
     * @return Statistics map
     */
    @Override
    @Transactional(readOnly = true)
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
     *
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
     *
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
     *
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
     *
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

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(prefix + "CandidateCount", stats.get("candidatecount"));
        resultMap.put(prefix + "InterviewCount", stats.get("interviewcount"));
        resultMap.put(prefix + "ScreeningCount", stats.get("screeningcount"));
        resultMap.put(prefix + "HiredCount", stats.get("hiredcount"));
        return resultMap;
    }

    /**
     * Calculates differences between current and previous month statistics
     *
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
     * Calculates the overall conversion rate from candidates to hired
     *
     * @param resultMap Reference to the result map where conversion rate will be stored
     */
    private void calculateConversionRate(Map<String, Object> resultMap) {
        Long totalCandidates = (Long) resultMap.get("candidatecount");
        Long totalHired = (Long) resultMap.get("hiredcount");

        if (totalCandidates == null || totalCandidates == 0) {
            resultMap.put("ConversionRate", "0.00%");
            return;
        }

        BigDecimal conversionRate = BigDecimal.valueOf(totalHired)
                .divide(BigDecimal.valueOf(totalCandidates), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        resultMap.put("ConversionRate", conversionRate.setScale(2, RoundingMode.HALF_UP) + "%");
    }

    /**
     * Calculates the month-to-month change in conversion rate
     *
     * @param resultMap Reference to the result map where rate change will be stored
     */
    private void calculateMonthlyRateChange(Map<String, Object> resultMap) {
        Long candidateNow = (Long) resultMap.get("NowMonthCandidateCount");
        Long candidateLast = (Long) resultMap.get("LastMonthCandidateCount");
        Long hiredNow = (Long) resultMap.get("NowMonthHiredCount");
        Long hiredLast = (Long) resultMap.get("LastMonthHiredCount");

        BigDecimal currentRate = (candidateNow == null || candidateNow == 0) ? BigDecimal.ZERO :
                BigDecimal.valueOf(hiredNow).divide(BigDecimal.valueOf(candidateNow), 4, RoundingMode.HALF_UP);
        BigDecimal lastRate = (candidateLast == null || candidateLast == 0) ? BigDecimal.ZERO :
                BigDecimal.valueOf(hiredLast).divide(BigDecimal.valueOf(candidateLast), 4, RoundingMode.HALF_UP);

        BigDecimal rateChange = currentRate.subtract(lastRate);
        double absoluteChange = Math.abs(rateChange.doubleValue() * 100);

        String trend;
        if (absoluteChange == 0) {
            trend = " ";
        } else {
            trend = rateChange.doubleValue() < 0 ? "-" : "+";
        }

        DecimalFormat df = new DecimalFormat("#.##");
        resultMap.put("MonthConversionRate", trend + df.format(absoluteChange));
    }

    @Override
    public HrEnterprise selectEid(String userEnterpriseId) {
        return baseMapper.seleEid(userEnterpriseId);
    }

    /**
     * Apply candidate information (external application)
     *
     * @param candidateInfo Candidate information
     * @return Success status
     */
    @Override
    @Transactional
    public boolean applyCandidateInfo(HrCandidateInfo candidateInfo) {
        log.info("Processing candidate application: name={}, email={}",
                candidateInfo.getCandidateName(), candidateInfo.getContactEmail());

        // Initialize candidate information
        initializeCandidateInfo(candidateInfo);

        // Serialize files to JSON
        serializeFilesJson(candidateInfo);

        // Save candidate
        boolean saved = save(candidateInfo);
        if (!saved) {
            log.error("Failed to save candidate information");
            throw new ServiceException("Failed to save candidate information");
        }

        // Handle related data
        handleDocuments(candidateInfo, null);
        handleQuestionAnswers(candidateInfo);

        // Send notification emails to HR
        sendHrNotificationEmails(candidateInfo);

        log.info("Candidate application processed successfully: candidateId={}",
                candidateInfo.getCandidateId());

        return saved;
    }

    /**
     * Initialize candidate basic information
     *
     * @param candidateInfo Candidate information
     */
    private void initializeCandidateInfo(HrCandidateInfo candidateInfo) {
        candidateInfo.setCreateTime(DateUtils.getNowDate());
        candidateInfo.setCandidateStatus(CandidateStatus.SCREENING.getCode());
    }

    /**
     * Serialize files value to JSON string
     *
     * @param candidateInfo Candidate information
     */
    private void serializeFilesJson(HrCandidateInfo candidateInfo) {
        if (candidateInfo.getFilesValue() != null) {
            String json = JsonUtils.toJsonPretty(candidateInfo.getFilesValue());
            candidateInfo.setFilesJson(json);
        }
    }

    /**
     * Handle candidate question answers
     *
     * @param candidateInfo Candidate information
     */
    private void handleQuestionAnswers(HrCandidateInfo candidateInfo) {
        List<HrQuestionAnswer> answers = candidateInfo.getAnswers();
        if (ObjectUtils.isEmpty(answers)) {
            return;
        }

        for (HrQuestionAnswer answer : answers) {
            answer.setCandidateId(candidateInfo.getCandidateId());
            answer.setEnterpriseId(candidateInfo.getEnterpriseId());
        }
        questionAnswerService.saveBatch(answers);
    }

    /**
     * Send notification emails to HR users
     *
     * @param candidateInfo Candidate information
     */
    private void sendHrNotificationEmails(HrCandidateInfo candidateInfo) {
        if (ObjectUtils.isEmpty(candidateInfo.getJobListingsId())) {
            return;
        }

        HrJobListings hrJobListings = jobListingsService.selectHrJobListingsById(
                candidateInfo.getJobListingsId());

        if (ObjectUtils.isEmpty(hrJobListings) ||
                ObjectUtils.isEmpty(hrJobListings.getEnterpriseId())) {
            return;
        }

        List<SysUser> hrUsers = questionAnswerService.selectHrList(hrJobListings.getEnterpriseId());
        if (ObjectUtils.isEmpty(hrUsers)) {
            return;
        }

        for (SysUser hrUser : hrUsers) {
            Map<String, Object> map = new HashMap<>();
            map.put("HrName", hrUser.getUserName());
            map.put("JobTitle", hrJobListings.getTitle());
            map.put("FullName", candidateInfo.getCandidateName());
            map.put("CandidateEmail", candidateInfo.getContactEmail());
            emailUtils.sendEmailByTemplate(map, hrUser.getEmail(), "Candidate-Application");
        }
    }

    /**
     * Insert HR candidate information (internal creation)
     *
     * @param hrCandidateInfo Candidate information
     * @return Success status
     */
    @Transactional
    @Override
    public boolean insertHrCandidateInfo(HrCandidateInfo hrCandidateInfo) {
        log.info("Inserting HR candidate information: name={}", hrCandidateInfo.getCandidateName());

        hrCandidateInfo.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        hrCandidateInfo.setCreateTime(DateUtils.getNowDate());
        hrCandidateInfo.setCandidateStatus(CandidateStatus.SCREENING.getCode());

        serializeFilesJson(hrCandidateInfo);

        boolean saved = save(hrCandidateInfo);
        handleDocuments(hrCandidateInfo, SecurityUtils.getUserId());

        return saved;
    }

    /**
     * Update HR candidate information
     *
     * @param hrCandidateInfo Candidate information
     * @return Success status
     */
    @Transactional
    @Override
    public boolean updateHrCandidateInfo(HrCandidateInfo hrCandidateInfo) {
        log.info("Updating candidate information: candidateId={}, status={}",
                hrCandidateInfo.getCandidateId(), hrCandidateInfo.getCandidateStatus());

        // Validate candidate exists
        validateCandidateExists(hrCandidateInfo.getCandidateId());

        // Get company and HR employee information
        HrEnterprise company = selectEid(SecurityUtils.getUserEnterpriseId());
        HrEmployees hrEmployee = hrEmployeesService.selectHrEmployeesByUserId(SecurityUtils.getUserId());

        // Send status-specific emails
        candidateEmailService.sendEmailByStatus(hrCandidateInfo, company, hrEmployee,
                SecurityUtils.getUserId());

        // Update candidate information
        hrCandidateInfo.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
        serializeFilesJson(hrCandidateInfo);
        handleDocuments(hrCandidateInfo, SecurityUtils.getUserId());

        return updateById(hrCandidateInfo);
    }

    /**
     * Validate that candidate exists
     *
     * @param candidateId Candidate ID
     */
    private void validateCandidateExists(Long candidateId) {
        HrCandidateInfo query = new HrCandidateInfo();
        query.setCandidateId(candidateId);
        List<HrCandidateInfo> candidates = selectHrCandidateInfoList(query);

        if (ObjectUtils.isEmpty(candidates)) {
            log.error("Candidate not found: candidateId={}", candidateId);
            throw new ServiceException("The Candidate anomalies!");
        }
    }

    /**
     * Send hired notification email
     *
     * @param hrCandidateInfo Candidate information
     */
    @Override
    public void sendEmailHired(HrCandidateInfo hrCandidateInfo) {
        HrEnterprise company = selectEid(SecurityUtils.getUserEnterpriseId());
        HrEmployees hrEmployee = hrEmployeesService.selectHrEmployeesByUserId(SecurityUtils.getUserId());

        candidateEmailService.sendHiredEmail(hrCandidateInfo, company, hrEmployee);
    }

    /**
     * Send invite email
     *
     * @param hrCandidateInfo Candidate information
     */
    @Override
    public void sendEmail(HrCandidateInfo hrCandidateInfo) {
        HrEnterprise company = selectEid(SecurityUtils.getUserEnterpriseId());
        candidateEmailService.sendInviteEmail(hrCandidateInfo, company);
    }

    /**
     * Batch process HR documents
     *
     * @param hrCandidateInfo Candidate information
     * @param userId User ID
     */
    private void handleDocuments(HrCandidateInfo hrCandidateInfo, Long userId) {
        List<HrDocument> filesValue = hrCandidateInfo.getFilesValue();
        Long candidateId = hrCandidateInfo.getCandidateId();

        if (filesValue == null) {
            return;
        }

        // Delete existing documents
        documentShareService.deleteByUploadCandidateId(candidateId);
        QueryWrapper<HrDocument> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("upload_candidate_id", candidateId);
        documentService.remove(queryWrapper);

        // Save new documents
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
