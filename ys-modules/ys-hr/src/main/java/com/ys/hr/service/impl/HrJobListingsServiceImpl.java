package com.ys.hr.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.exception.DataValidationException;
import com.ys.common.core.exception.DatabaseOperationException;
import com.ys.common.core.exception.ResourceNotFoundException;
import com.ys.hr.domain.HrJobListingsQuestion;
import com.ys.hr.service.IHrJobListingsQuestionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrJobListingsMapper;
import com.ys.hr.domain.HrJobListings;
import com.ys.hr.service.IHrJobListingsService;
import com.ys.common.core.utils.DateUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Job Listings Service Implementation
 *
 * @author ys
 * @date 2025-09-25
 */
@Service
public class HrJobListingsServiceImpl extends ServiceImpl<HrJobListingsMapper, HrJobListings>
        implements IHrJobListingsService {

    private static final Logger log = LoggerFactory.getLogger(HrJobListingsServiceImpl.class);

    @Resource
    private IHrJobListingsQuestionService hrJobListingsQuestionService;

    /**
     * Query Job Listings by ID
     *
     * @param id Job Listings primary key
     * @return Job Listings
     * @throws IllegalArgumentException if id is null or empty
     * @throws ResourceNotFoundException if job listing not found
     */
    @Override
    public HrJobListings selectHrJobListingsById(String id) {
        // Input validation
        if (StringUtils.isBlank(id)) {
            log.error("Attempt to query job listing with blank id");
            throw new IllegalArgumentException("Job listing ID cannot be null or empty");
        }

        try {
            log.debug("Querying job listing with id: {}", id);

            HrJobListings hrJobListings = baseMapper.selectHrJobListingsById(id);

            if (hrJobListings == null) {
                log.warn("Job listing not found with id: {}", id);
                throw new ResourceNotFoundException("Job Listing", id);
            }

            // Load associated questions
            loadQuestionsForJobListing(hrJobListings);

            log.info("Successfully retrieved job listing with id: {}", id);
            return hrJobListings;

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error querying job listing with id: {}", id, e);
            throw new DatabaseOperationException("select job listing by id", e);
        }
    }

    /**
     * Load associated questions for job listing
     *
     * @param hrJobListings Job listing to load questions for
     */
    private void loadQuestionsForJobListing(HrJobListings hrJobListings) {
        try {
            HrJobListingsQuestion questionQuery = new HrJobListingsQuestion();
            questionQuery.setJobListingsId(hrJobListings.getId());

            List<HrJobListingsQuestion> questions =
                    hrJobListingsQuestionService.selectHrJobListingsQuestionList(questionQuery);

            hrJobListings.setQuestions(questions);
            log.debug("Loaded {} questions for job listing {}",
                    questions.size(), hrJobListings.getId());

        } catch (Exception e) {
            log.error("Error loading questions for job listing: {}",
                    hrJobListings.getId(), e);
            // Don't throw exception, allow returning job data without questions
            hrJobListings.setQuestions(new ArrayList<>());
        }
    }

    /**
     * Query Job Listings list
     *
     * @param hrJobListings Job Listings query criteria
     * @return Job Listings collection
     * @throws IllegalArgumentException if query criteria is null
     */
    @Override
    public List<HrJobListings> selectHrJobListingsList(HrJobListings hrJobListings) {
        if (hrJobListings == null) {
            log.error("Query criteria cannot be null");
            throw new IllegalArgumentException("Query criteria cannot be null");
        }

        try {
            log.debug("Querying job listings list with criteria: {}", hrJobListings);

            List<HrJobListings> result = baseMapper.selectHrJobListingsList(hrJobListings);

            log.info("Found {} job listings", result.size());
            return result;

        } catch (Exception e) {
            log.error("Error querying job listings list", e);
            throw new DatabaseOperationException("select job listings list", e);
        }
    }

    /**
     * Add Job Listings
     *
     * @param hrJobListings Job Listings to add
     * @return Number of rows affected
     * @throws IllegalArgumentException if input is invalid
     * @throws DataValidationException if business validation fails
     * @throws DatabaseOperationException if database operation fails
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertHrJobListings(HrJobListings hrJobListings) {
        // 1. Input validation
        validateJobListingForInsert(hrJobListings);

        try {
            log.info("Inserting new job listing: {}", hrJobListings.getTitle());

            // 2. Set creation time
            hrJobListings.setCreateTime(DateUtils.getNowDate());

            // 3. Insert main record
            int insertCount = baseMapper.insert(hrJobListings);

            if (insertCount == 0) {
                log.error("Failed to insert job listing, no rows affected");
                throw new DatabaseOperationException("insert job listing",
                        "No rows were inserted");
            }

            log.debug("Successfully inserted job listing with id: {}", hrJobListings.getId());

            // 4. Batch save associated questions
            if (hrJobListings.getQuestions() != null && !hrJobListings.getQuestions().isEmpty()) {
                batchSaveQuestions(hrJobListings);
            }

            log.info("Successfully created job listing {} with {} questions",
                    hrJobListings.getId(),
                    hrJobListings.getQuestions() != null ? hrJobListings.getQuestions().size() : 0);

            return insertCount;

        } catch (DataValidationException | DatabaseOperationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error inserting job listing", e);
            throw new DatabaseOperationException("insert job listing", e);
        }
    }

    /**
     * Validate job listing data for insertion
     *
     * @param hrJobListings Job listing to validate
     * @throws IllegalArgumentException if job listing is null
     * @throws DataValidationException if validation fails
     */
    private void validateJobListingForInsert(HrJobListings hrJobListings) {
        if (hrJobListings == null) {
            throw new IllegalArgumentException("Job listing cannot be null");
        }

        // Validate required fields
        if (StringUtils.isBlank(hrJobListings.getTitle())) {
            throw new DataValidationException("title", "Job title is required");
        }

        if (StringUtils.isBlank(hrJobListings.getEnterpriseId())) {
            throw new DataValidationException("enterpriseId", "Enterprise ID is required");
        }

        // Validate salary range
        if (hrJobListings.getPayMin() != null && hrJobListings.getPayMax() != null) {
            if (hrJobListings.getPayMin().compareTo(hrJobListings.getPayMax()) > 0) {
                throw new DataValidationException("payRange",
                        "Minimum pay cannot be greater than maximum pay");
            }
        }

        log.debug("Job listing validation passed for: {}", hrJobListings.getTitle());
    }

    /**
     * Update Job Listings
     *
     * @param hrJobListings Job Listings to update
     * @return Number of rows affected
     * @throws IllegalArgumentException if input is invalid
     * @throws ResourceNotFoundException if job listing not found
     * @throws DatabaseOperationException if database operation fails
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateHrJobListings(HrJobListings hrJobListings) {
        // 1. Input validation
        validateJobListingForUpdate(hrJobListings);

        try {
            log.info("Updating job listing: {}", hrJobListings.getId());

            // 2. Verify record exists
            HrJobListings existing = baseMapper.selectHrJobListingsById(hrJobListings.getId());
            if (existing == null) {
                log.warn("Attempt to update non-existent job listing: {}", hrJobListings.getId());
                throw new ResourceNotFoundException("Job Listing", hrJobListings.getId());
            }

            // 3. Set update time
            hrJobListings.setUpdateTime(DateUtils.getNowDate());

            // 4. Update associated questions
            updateAssociatedQuestions(hrJobListings);

            // 5. Update main record
            int updateCount = baseMapper.updateById(hrJobListings);

            if (updateCount == 0) {
                log.error("Failed to update job listing {}, no rows affected",
                        hrJobListings.getId());
                throw new DatabaseOperationException("update job listing",
                        "No rows were updated. The record may have been modified by another user.");
            }

            log.info("Successfully updated job listing: {}", hrJobListings.getId());
            return updateCount;

        } catch (ResourceNotFoundException | DatabaseOperationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error updating job listing: {}", hrJobListings.getId(), e);
            throw new DatabaseOperationException("update job listing", e);
        }
    }

    /**
     * Validate job listing data for update
     *
     * @param hrJobListings Job listing to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateJobListingForUpdate(HrJobListings hrJobListings) {
        if (hrJobListings == null) {
            throw new IllegalArgumentException("Job listing cannot be null");
        }

        if (StringUtils.isBlank(hrJobListings.getId())) {
            throw new IllegalArgumentException("Job listing ID is required for update");
        }

        // Other validation logic...
        validateJobListingForInsert(hrJobListings);
    }

    /**
     * Update associated questions
     *
     * @param hrJobListings Job listing with questions to update
     * @throws DatabaseOperationException if update fails
     */
    private void updateAssociatedQuestions(HrJobListings hrJobListings) {
        try {
            // Delete existing questions
            QueryWrapper<HrJobListingsQuestion> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_listings_id", hrJobListings.getId());

            boolean removeSuccess = hrJobListingsQuestionService.remove(queryWrapper);
            log.debug("Removed existing questions for job listing {}: {}",
                    hrJobListings.getId(), removeSuccess);

            // Save new questions
            if (hrJobListings.getQuestions() != null && !hrJobListings.getQuestions().isEmpty()) {
                batchSaveQuestions(hrJobListings);
            }

        } catch (Exception e) {
            log.error("Error updating associated questions for job listing: {}",
                    hrJobListings.getId(), e);
            throw new DatabaseOperationException("update job listing questions", e);
        }
    }

    /**
     * Batch save questions
     *
     * @param hrJobListings Job listing object containing questions list
     * @throws DataValidationException if questions are invalid
     * @throws DatabaseOperationException if save operation fails
     */
    private void batchSaveQuestions(HrJobListings hrJobListings) {
        List<HrJobListingsQuestion> questions = hrJobListings.getQuestions();

        if (questions == null || questions.isEmpty()) {
            log.debug("No questions to save for job listing: {}", hrJobListings.getId());
            return;
        }

        try {
            log.debug("Saving {} questions for job listing: {}",
                    questions.size(), hrJobListings.getId());

            // Set question properties
            int questionIndex = 0;
            for (HrJobListingsQuestion question : questions) {
                // Validate question data
                if (StringUtils.isBlank(question.getQuestionMsg())) {
                    throw new DataValidationException("question",
                            "Question text cannot be empty");
                }

                question.setEnterpriseId(hrJobListings.getEnterpriseId());
                question.setJobListingsId(hrJobListings.getId());
                question.setCreateTime(DateUtils.getNowDate());
                question.setQuestionIndex(questionIndex++);
            }

            // Batch save
            boolean saveSuccess = hrJobListingsQuestionService.saveBatch(questions);

            if (!saveSuccess) {
                throw new DatabaseOperationException("batch save questions",
                        "Failed to save questions for job listing");
            }

            log.info("Successfully saved {} questions for job listing: {}",
                    questions.size(), hrJobListings.getId());

        } catch (DataValidationException | DatabaseOperationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error saving questions for job listing: {}", hrJobListings.getId(), e);
            throw new DatabaseOperationException("batch save questions", e);
        }
    }
}
