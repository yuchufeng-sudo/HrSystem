package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrOnboardingResultMapper;
import com.ys.hr.domain.HrOnboardingResult;
import com.ys.hr.service.IHrOnboardingResultService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import com.ys.common.core.exception.DatabaseOperationException;
import com.ys.common.core.exception.ServiceException;

/**
 * Onboarding Result Service Implementation
 *
 * @author ys
 * @date 2025-10-14
 */
@Slf4j
@Service
public class HrOnboardingResultServiceImpl extends ServiceImpl<HrOnboardingResultMapper, HrOnboardingResult> implements IHrOnboardingResultService
{

    /**
     * Query Onboarding Result
     *
     * @param id Onboarding Result primary key
     * @return Onboarding Result
     */
    @Override
    public HrOnboardingResult selectHrOnboardingResultById(String id)
    {
        // Parameter validation: primary key cannot be null or empty
        if (id == null || id.trim().isEmpty()) {
            log.error("Query onboarding result failed: id is null or empty");
            throw new IllegalArgumentException("Onboarding result ID cannot be null or empty");
        }

        try {
            HrOnboardingResult result = baseMapper.selectHrOnboardingResultById(id);
            if (result == null) {
                log.warn("Onboarding result not found with id: {}", id);
            }
            return result;
        } catch (Exception e) {
            log.error("Failed to query onboarding result by id: {}", id, e);
            throw new DatabaseOperationException("Query onboarding result failed", e);
        }
    }

    /**
     * Query Onboarding Result list
     *
     * @param hrOnboardingResult Onboarding Result
     * @return Onboarding Result list
     */
    @Override
    public List<HrOnboardingResult> selectHrOnboardingResultList(HrOnboardingResult hrOnboardingResult)
    {
        try {
            // Allow null query conditions, return all data
            return baseMapper.selectHrOnboardingResultList(hrOnboardingResult);
        } catch (Exception e) {
            log.error("Failed to query onboarding result list", e);
            throw new DatabaseOperationException("Query onboarding result list failed", e);
        }
    }

    /**
     * Add Onboarding Result
     *
     * @param hrOnboardingResult Onboarding Result
     * @return Result
     */
    @Override
    public int insertHrOnboardingResult(HrOnboardingResult hrOnboardingResult)
    {
        // Parameter validation: entity cannot be null
        if (hrOnboardingResult == null) {
            log.error("Insert onboarding result failed: entity is null");
            throw new IllegalArgumentException("Onboarding result entity cannot be null");
        }

        // Business validation: mandatory field check (supplement according to actual business)
        if (hrOnboardingResult.getEmployeesId() == null || hrOnboardingResult.getEmployeesId().trim().isEmpty()) {
            log.error("Insert onboarding result failed: employeeId is null or empty");
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }

        try {
            hrOnboardingResult.setCreateTime(DateUtils.getNowDate());
            return baseMapper.insert(hrOnboardingResult);
        } catch (DuplicateKeyException e) {
            log.error("Insert onboarding result failed: duplicate key for employee [{}]", hrOnboardingResult.getEmployeesId(), e);
            throw new DuplicateKeyException("Onboarding result already exists for employee: " + hrOnboardingResult.getEmployeesId());
        } catch (DataIntegrityViolationException e) {
            log.error("Insert onboarding result failed: data integrity violation for employee [{}]",
                    hrOnboardingResult.getEmployeesId(), e);
            throw new DataIntegrityViolationException("Invalid result data");
        } catch (Exception e) {
            log.error("Insert onboarding result failed", e);
            throw new ServiceException("Create onboarding result failed");
        }
    }

    /**
     * Update Onboarding Result
     *
     * @param hrOnboardingResult Onboarding Result
     * @return Result
     */
    @Override
    public int updateHrOnboardingResult(HrOnboardingResult hrOnboardingResult)
    {
        // Parameter validation
        if (hrOnboardingResult == null) {
            log.error("Update onboarding result failed: entity is null");
            throw new IllegalArgumentException("Onboarding result entity cannot be null");
        }
        if (hrOnboardingResult.getId() == null || hrOnboardingResult.getId().trim().isEmpty()) {
            log.error("Update onboarding result failed: id is null or empty");
            throw new IllegalArgumentException("Onboarding result ID cannot be null or empty");
        }

        // Business validation: check if record exists
        HrOnboardingResult existingResult = selectHrOnboardingResultById(hrOnboardingResult.getId());
        if (existingResult == null) {
            log.error("Update onboarding result failed: result not found with id [{}]", hrOnboardingResult.getId());
            throw new ServiceException("Onboarding result not found: " + hrOnboardingResult.getId());
        }

        try {
            log.info("Updating onboarding result: {}", hrOnboardingResult.getId());
            hrOnboardingResult.setUpdateTime(DateUtils.getNowDate());

            int result = baseMapper.updateById(hrOnboardingResult);
            if (result == 0) {
                log.warn("No onboarding result updated with id: {}", hrOnboardingResult.getId());
            }
            return result;
        } catch (DataIntegrityViolationException e) {
            log.error("Update onboarding result failed: data integrity violation for id [{}]",
                    hrOnboardingResult.getId(), e);
            throw new DataIntegrityViolationException("Invalid update data");
        } catch (Exception e) {
            log.error("Update onboarding result failed for id: {}", hrOnboardingResult.getId(), e);
            throw new ServiceException("Update onboarding result failed");
        }
    }
}
