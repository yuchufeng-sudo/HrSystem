package com.ys.hr.service.impl;

import java.util.Collections;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrOnboardingQuestionMapper;
import com.ys.hr.domain.HrOnboardingQuestion;
import com.ys.hr.service.IHrOnboardingQuestionService;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.exception.DatabaseOperationException;
import com.ys.common.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;

/**
 * Job listings questions table Service Implementation
 *
 * @author ys
 * @date 2025-10-13
 */
@Slf4j
@Service
public class HrOnboardingQuestionServiceImpl extends ServiceImpl<HrOnboardingQuestionMapper, HrOnboardingQuestion> implements IHrOnboardingQuestionService
{

    /**
     * Query Job listings questions table
     *
     * @param id Job listings questions table primary key
     * @return Job listings questions table
     */
    @Override
    public HrOnboardingQuestion selectHrOnboardingQuestionById(String id)
    {
        // Parameter validation: primary key cannot be null or empty
        if (id == null || id.trim().isEmpty()) {
            log.error("Query onboarding question failed: id is null or empty");
            throw new IllegalArgumentException("Onboarding question ID cannot be null or empty");
        }

        try {
            HrOnboardingQuestion question = baseMapper.selectHrOnboardingQuestionById(id);
            if (question == null) {
                log.warn("Onboarding question not found with id: {}", id);
            }
            return question;
        } catch (Exception e) {
            log.error("Failed to query onboarding question by id: {}", id, e);
            throw new DatabaseOperationException("Query onboarding question failed", e);
        }
    }

    /**
     * Query Job listings questions table list
     *
     * @param hrOnboardingQuestion Job listings questions table
     * @return Job listings questions table
     */
    @Override
    public List<HrOnboardingQuestion> selectHrOnboardingQuestionList(HrOnboardingQuestion hrOnboardingQuestion)
    {
        try {
            // Allow null query conditions, return all data
            return baseMapper.selectHrOnboardingQuestionList(hrOnboardingQuestion);
        } catch (Exception e) {
            log.error("Failed to query onboarding question list", e);
            throw new DatabaseOperationException("Query onboarding question list failed", e);
        }
    }

    /**
     * Add Job listings questions table
     *
     * @param hrOnboardingQuestion Job listings questions table
     * @return Result
     */
    @Override
    public int insertHrOnboardingQuestion(HrOnboardingQuestion hrOnboardingQuestion)
    {
        // Parameter validation: entity cannot be null
        if (hrOnboardingQuestion == null) {
            log.error("Insert onboarding question failed: entity is null");
            throw new IllegalArgumentException("Onboarding question entity cannot be null");
        }

        // Business validation: required fields check
        if (hrOnboardingQuestion.getEmployeesId() == null || hrOnboardingQuestion.getEmployeesId().trim().isEmpty()) {
            log.error("Insert onboarding question failed: employeeId is null or empty");
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        if (hrOnboardingQuestion.getQuestionMsg() == null || hrOnboardingQuestion.getQuestionMsg().trim().isEmpty()) {
            log.error("Insert onboarding question failed: question content is null or empty");
            throw new IllegalArgumentException("Question content cannot be null or empty");
        }

        try {
            hrOnboardingQuestion.setCreateTime(DateUtils.getNowDate());
            return baseMapper.insert(hrOnboardingQuestion);
        } catch (DuplicateKeyException e) {
            log.error("Insert onboarding question failed: duplicate key for question [{}]", hrOnboardingQuestion.getQuestionMsg(), e);
            throw new DuplicateKeyException("Question already exists: " + hrOnboardingQuestion.getQuestionMsg());
        } catch (DataIntegrityViolationException e) {
            log.error("Insert onboarding question failed: data integrity violation for employee [{}]",
                    hrOnboardingQuestion.getEmployeesId(), e);
            throw new DataIntegrityViolationException("Invalid question data");
        } catch (Exception e) {
            log.error("Insert onboarding question failed", e);
            throw new ServiceException("Create onboarding question failed");
        }
    }

    /**
     * Update Job listings questions table
     *
     * @param hrOnboardingQuestion Job listings questions table
     * @return Result
     */
    @Override
    public int updateHrOnboardingQuestion(HrOnboardingQuestion hrOnboardingQuestion)
    {
        // Parameter validation
        if (hrOnboardingQuestion == null) {
            log.error("Update onboarding question failed: entity is null");
            throw new IllegalArgumentException("Onboarding question entity cannot be null");
        }
        if (hrOnboardingQuestion.getId() == null || hrOnboardingQuestion.getId().trim().isEmpty()) {
            log.error("Update onboarding question failed: id is null or empty");
            throw new IllegalArgumentException("Onboarding question ID cannot be null or empty");
        }

        // Business validation: check if record exists
        HrOnboardingQuestion existingQuestion = selectHrOnboardingQuestionById(hrOnboardingQuestion.getId());
        if (existingQuestion == null) {
            log.error("Update onboarding question failed: question not found with id [{}]", hrOnboardingQuestion.getId());
            throw new ServiceException("Onboarding question not found: " + hrOnboardingQuestion.getId());
        }

        try {
            log.info("Updating onboarding question: {}", hrOnboardingQuestion.getId());
            hrOnboardingQuestion.setUpdateTime(DateUtils.getNowDate());

            int result = baseMapper.updateById(hrOnboardingQuestion);
            if (result == 0) {
                log.warn("No onboarding question updated with id: {}", hrOnboardingQuestion.getId());
            }
            return result;
        } catch (DataIntegrityViolationException e) {
            log.error("Update onboarding question failed: data integrity violation for id [{}]",
                    hrOnboardingQuestion.getId(), e);
            throw new DataIntegrityViolationException("Invalid update data");
        } catch (Exception e) {
            log.error("Update onboarding question failed for id: {}", hrOnboardingQuestion.getId(), e);
            throw new ServiceException("Update onboarding question failed");
        }
    }

    @Override
    public List<HrOnboardingQuestion> selectHrOnboardingQuestionListByEmp(HrOnboardingQuestion hrOnboardingQuestion) {
        // Parameter validation
        if (hrOnboardingQuestion == null || hrOnboardingQuestion.getEmployeesId() == null || hrOnboardingQuestion.getEmployeesId().trim().isEmpty()) {
            log.error("Query by employee failed: employeeId is null or empty");
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }

        try {
            return baseMapper.selectHrOnboardingQuestionListByEmp(hrOnboardingQuestion);
        } catch (Exception e) {
            log.error("Failed to query onboarding question list by employee [{}]", hrOnboardingQuestion.getEmployeesId(), e);
            throw new DatabaseOperationException("Query questions by employee failed", e);
        }
    }
}
